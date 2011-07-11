package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.LocationLocal;
import dk.itu.frigga.device.model.Point3;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-09
 */
public class LocationFilter extends Filter
{
    private static Pattern PATTERN = Pattern.compile("^(\\-?[0-9]*(?:\\.[0-9]+)?)(km|m|cm|mm)$");

    private String type = "global";
    private String room = "";
    private String position = "0,0,0";
    private String minRadius = "0m";
    private String maxRadius = "999999m";
    private boolean matchCoords = false;

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("type"))
        {
            type = attributes.get("type");
        }

        if (attributes.containsKey("room"))
        {
            room = attributes.get("room");
        }

        if (attributes.containsKey("position"))
        {
            position = attributes.get("position");
            matchCoords = true;
        }

        if (attributes.containsKey("minRadius"))
        {
            minRadius = attributes.get("minRadius");
        }

        if (attributes.containsKey("maxRadius"))
        {
            maxRadius = attributes.get("maxRadius");
        }
    }

    private Point3<Double> parseToDouble(final String str)
    {
        Point3<Double> pos = new Point3<Double>(0.0, 0.0, 0.0);

        String[] components = str.split(",");
        if (components.length == 3)
        {
            pos.setX(Double.parseDouble(components[0]));
            pos.setY(Double.parseDouble(components[1]));
            pos.setZ(Double.parseDouble(components[2]));
        }

        return pos;
    }

    private double calculateDistance(Point3<Double> pos1, Point3<Double> pos2)
    {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2.0) +
                         Math.pow(pos1.getY() - pos2.getY(), 2.0) +
                         Math.pow(pos1.getZ() - pos2.getZ(), 2.0));
    }

    private double normalizeValue(double number, String unit, String originalUnit)
    {
        double normalizedNumber = number;

        if ("km".equals(originalUnit))
        {
            normalizedNumber = normalizedNumber * 1000.0;
        }
        else if ("cm".equals(originalUnit))
        {
            normalizedNumber = normalizedNumber / 100.0;
        }
        else if ("mm".equals(originalUnit))
        {
            normalizedNumber = normalizedNumber / 1000.0;
        }

        if ("km".equals(unit))
        {
            return normalizedNumber / 1000.0;
        }

        if ("m".equals(unit))
        {
            return normalizedNumber;
        }

        if ("cm".equals(unit))
        {
            return normalizedNumber * 100.0;
        }

        if ("mm".equals(unit))
        {
            return normalizedNumber * 1000.0;
        }

        return number;
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        String localType = context.prepare(type);
        String localRoom = context.prepare(room);
        Point3<Double> localPosition = parseToDouble(context.prepare(position));
        String localMinRadiusStr = context.prepare(minRadius);
        String localMaxRadiusStr = context.prepare(maxRadius);
        double minRadiusNumber = 1.0;
        String minUnit = "m";
        double maxRadiusNumber = 1.0;
        String maxUnit = "m";

        Matcher matcher = PATTERN.matcher(localMinRadiusStr);
        if (matcher.matches())
        {
            minRadiusNumber = Double.parseDouble(matcher.group(1));
            minUnit = matcher.group(2);
        }

        matcher.reset(localMaxRadiusStr);
        if (matcher.matches())
        {
            maxRadiusNumber = Double.parseDouble(matcher.group(1));
            maxUnit = matcher.group(2);
        }

        minRadiusNumber = normalizeValue(minRadiusNumber, minUnit, "local".equals(localType) ? "mm" : "m");
        maxRadiusNumber = normalizeValue(maxRadiusNumber, maxUnit, "local".equals(localType) ? "mm" : "m");

        for (FilterDeviceState state : input)
        {
            if ("local".equals(localType))
            {
                for (LocationLocal l : state.getDevice().getLocalLocations())
                {
                    if (localRoom.equals(l.getRoom()))
                    {
                        if (matchCoords)
                        {
                            double dist = calculateDistance(localPosition, l.getPosition());

                            if (dist >= minRadiusNumber && dist <= maxRadiusNumber)
                            {
                                output.addDevice(state);
                            }
                        }
                        else
                        {
                            output.addDevice(state);
                        }
                    }
                }
            }
            else
            {
                double dist = calculateDistance(localPosition, state.getDevice().getGlobalLocation().getPosition());

                if (dist >= minRadiusNumber && dist <= maxRadiusNumber)
                {
                    output.addDevice(state);
                }
            }
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

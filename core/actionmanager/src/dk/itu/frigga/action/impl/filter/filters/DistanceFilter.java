package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Location;
import dk.itu.frigga.device.model.LocationLocal;
import dk.itu.frigga.device.model.Point3;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-12
 */
public class DistanceFilter extends Filter
{
    private static Pattern PATTERN = Pattern.compile("^(\\-?[0-9]*(?:\\.[0-9]+)?)(km|m|cm|mm)$");

    private String source = "";
    private String minRadius = "0m";
    private String maxRadius = "999999m";
    private String tagName = "";
    private String type = "local";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("source"))
        {
            source = attributes.get("source");
        }

        if (attributes.containsKey("minRadius"))
        {
            minRadius = attributes.get("minRadius");
        }

        if (attributes.containsKey("maxRadius"))
        {
            maxRadius = attributes.get("maxRadius");
        }

        if (attributes.containsKey("tagName"))
        {
            tagName = attributes.get("tagName");
        }

        if (attributes.containsKey("type"))
        {
            type = attributes.get("type");
        }
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
        FilterOutput remoteOutput = context.getStoredOutput(context.prepare(source));
        double localMinDistance = 0.0;
        double localMaxDistance = 0.0;
        String minDistanceUnit = "m";
        String maxDistanceUnit = "m";

        Matcher matcher = PATTERN.matcher(context.prepare(minRadius));
        if (matcher.matches())
        {
            localMinDistance = Double.parseDouble(matcher.group(1));
            minDistanceUnit = matcher.group(2);
        }

        matcher.reset(context.prepare(maxRadius));
        if (matcher.matches())
        {
            localMaxDistance = Double.parseDouble(matcher.group(1));
            maxDistanceUnit = matcher.group(2);
        }

        localMinDistance = normalizeValue(localMinDistance, minDistanceUnit, "mm");
        localMaxDistance = normalizeValue(localMaxDistance, maxDistanceUnit, "mm");

        String localTagName = context.prepare(tagName);

        for (FilterDeviceState state : input)
        {
            List<LocationLocal> locations = state.getDevice().getLocalLocations();

            for (FilterDeviceState remoteState : remoteOutput)
            {
                List<LocationLocal> remoteLocations = state.getDevice().getLocalLocations();

                for (LocationLocal localLocation : locations)
                {
                    for (LocationLocal remoteLocation : remoteLocations)
                    {
                        if (localLocation.getRoom().equals(remoteLocation.getRoom()))
                        {
                            double dist = calculateDistance(localLocation.getPosition(), remoteLocation.getPosition());

                            if (dist >= localMinDistance && dist <= localMaxDistance)
                            {
                                state.storeTag(localTagName, remoteState);
                                output.addDevice(state);
                            }
                        }
                    }
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

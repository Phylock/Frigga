package dk.itu.frigga.device;

import dk.itu.frigga.device.model.Location;

import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class Device
{
    private final Map<String, DeviceVariable> variables = Collections.synchronizedMap(new HashMap<String, DeviceVariable>());
    private final Set<DeviceCategory> categories = Collections.synchronizedSet(new HashSet<DeviceCategory>());

    private String name;
    private String symbolic;
    private Date lastUpdate;
    private boolean online;
    private String driver;
    private DeviceGlobalLocation globalLocation;
    private Map<String, DeviceLocalLocation> localLocations = Collections.synchronizedMap(new HashMap<String, DeviceLocalLocation>());

    public Device(final String name)
    {
        this.name = name;
        this.symbolic = UUID.randomUUID().toString();
        this.driver = "";
        this.online = false;
        this.lastUpdate = Calendar.getInstance().getTime();
    }

    @Override
    public String toString()
    {
        String str = "Device: " + name + "\n" +
               "  symbolic:    " + symbolic + "\n" +
               "  last update: " + lastUpdate + "\n" +
               "  online:      " + online + "\n" +
               "  driver:      " + driver + "\n" +
               "  Variables:\n";

        for (Map.Entry<String, DeviceVariable> variable : variables.entrySet())
        {
            str = str + "    " + variable.getKey() + ": " + variable.getValue().getValue() + "\n";
        }

        return str;
    }

    public boolean hasVariable(final String variableName)
    {
        return variables.containsKey(variableName);
    }

    public boolean hasVariableMatch(final Pattern name, final Pattern type)
    {
        Matcher matcherName = name.matcher("");
        Matcher matcherType = type.matcher("");

        for (Map.Entry<String, DeviceVariable> variableEntry : variables.entrySet())
        {
            if (matcherName.reset(variableEntry.getKey()).matches())
            {
                if (matcherType.reset(variableEntry.getValue().getType()).matches())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public DeviceVariable getVariable(final String variableName) throws UnknownDeviceVariableException
    {
        if (!variables.containsKey(variableName))
        {
            throw new UnknownDeviceVariableException(variableName);
        }

        return variables.get(variableName);
    }

    public boolean isCategory(final String categoryName)
    {
        return categories.contains(categoryName);
    }

    public boolean hasCategoryMatch(final Pattern categoryPattern)
    {
        Matcher matcher = categoryPattern.matcher("");

        for (DeviceCategory category : categories)
        {
            if (matcher.reset(category.getName()).matches()) return true;
        }

        return false;
    }

    public boolean isOnline()
    {
        return online;
    }

    public String getSymbolic()
    {
        return symbolic;
    }

    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    public String getName()
    {
        return name;
    }

    public String getDriver()
    {
        return driver;
    }
}

package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.device.model.Device;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-08
 */
public class FilterDeviceState
{
    private final Device device;
    private final Map<String, FilterDeviceState> storedTags = Collections.synchronizedMap(new HashMap<String, FilterDeviceState>());
    private final Map<String, String> localVariables = Collections.synchronizedMap(new HashMap<String, String>());

    public FilterDeviceState(Device device)
    {
        this.device = device;
    }

    public Device getDevice()
    {
        return device;
    }

    public Set<String> getStoredTags()
    {
        return storedTags.keySet();
    }

    public FilterDeviceState getStoredDevice(final String tag)
    {
        return storedTags.get(tag);
    }

    public void storeTag(final String tag, final FilterDeviceState device)
    {
        storedTags.put(tag, device);
    }

    public void setLocalVariable(final String name, final String value)
    {
        localVariables.put(name, value);
    }

    public String getLocalVariable(final String name)
    {
        return localVariables.get(name);
    }

    public boolean hasLocalVariable(final String name)
    {
        return localVariables.containsKey(name);
    }

    public Set<String> getLocalVariableNames()
    {
        return localVariables.keySet();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterDeviceState that = (FilterDeviceState) o;

        if (!device.equals(that.device)) return false;
        if (!storedTags.equals(that.storedTags)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = device.hashCode();
        result = 31 * result + storedTags.hashCode();
        return result;
    }
}

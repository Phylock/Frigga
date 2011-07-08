package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.device.model.Device;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class FilterOutput implements Iterable<FilterDeviceState>
{
    protected final Set<FilterDeviceState> devices = Collections.synchronizedSet(new LinkedHashSet<FilterDeviceState>());

    public FilterOutput()
    {
    }

    public FilterOutput(final FilterInput input)
    {
        devices.addAll(input.devices);
    }

    public void addDevice(final FilterDeviceState device)
    {
        devices.add(device);
    }

    public void removeDevice(final FilterDeviceState device)
    {
        devices.remove(device);
    }

    public void merge(final FilterOutput output)
    {
        devices.addAll(output.devices);
    }

    public Collection<FilterDeviceState> matchingDevices()
    {
        return devices;
    }

    public void useInput(final FilterInput input)
    {
        devices.addAll(input.devices);
    }

    @Override
    public Iterator<FilterDeviceState> iterator()
    {
        return devices.iterator();
    }
}

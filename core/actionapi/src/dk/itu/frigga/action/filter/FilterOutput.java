package dk.itu.frigga.action.filter;

import dk.itu.frigga.device.Device;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class FilterOutput implements Iterable<Device>
{
    protected final Set<Device> devices = Collections.synchronizedSet(new LinkedHashSet<Device>());

    public FilterOutput()
    {
    }

    public FilterOutput(final FilterInput input)
    {
        devices.addAll(input.devices);
    }

    public void addDevice(final Device device)
    {
        devices.add(device);
    }

    public void removeDevice(final Device device)
    {
        devices.remove(device);
    }

    public void merge(final FilterOutput output)
    {
        devices.addAll(output.devices);
    }

    @Override
    public Iterator<Device> iterator()
    {
        return devices.iterator();
    }
}

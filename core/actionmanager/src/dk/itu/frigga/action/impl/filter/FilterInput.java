package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.device.model.Device;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-01
 */
public class FilterInput implements Iterable<FilterDeviceState>
{
    protected final List<FilterDeviceState> devices = Collections.synchronizedList(new LinkedList<FilterDeviceState>());
    private boolean limitDevices = false;

    public FilterInput()
    {
    }

    public FilterInput(final FilterOutput output)
    {
        devices.addAll(output.devices);
        limitDevices = true;
    }

    public boolean isLimitDevices()
    {
        return limitDevices;
    }

    public Collection<FilterDeviceState> getDevices()
    {
        return devices;
    }

    @Override
    public Iterator<FilterDeviceState> iterator()
    {
        return devices.iterator();
    }
}

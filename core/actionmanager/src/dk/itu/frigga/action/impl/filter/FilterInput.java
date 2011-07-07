package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.device.model.Device;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-01
 */
public class FilterInput implements Iterable<Device>
{
    protected final List<Device> devices = Collections.synchronizedList(new LinkedList<Device>());
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

    @Override
    public Iterator<Device> iterator()
    {
        return devices.iterator();
    }
}

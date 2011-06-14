package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.runtime.Selection;
import dk.itu.frigga.device.model.Device;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-07
 */
public class DeviceSelection extends Selection
{
    private final Device device;

    public DeviceSelection(final Device device)
    {
        this.device = device;
    }

    @Override
    public Comparison compare(final Selection other)
    {
        if (other instanceof DeviceSelection)
        {
            if (((DeviceSelection) other).getDevice().equals(device))
            {
                return Comparison.EQUAL_TO;
            }
            return Comparison.LESS_THAN;
        }

        return Comparison.NOT_COMPARABLE;
    }

    public Device getDevice()
    {
        return device;
    }
}

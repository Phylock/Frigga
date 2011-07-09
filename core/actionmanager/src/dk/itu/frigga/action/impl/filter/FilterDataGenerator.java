package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.DeviceManager;

import java.sql.SQLException;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class FilterDataGenerator
{
    private final DeviceManager deviceManager;
    private final FilterInput filterInput = new FilterInput();


    public FilterDataGenerator(DeviceManager deviceManager) throws SQLException
    {
        this.deviceManager = deviceManager;
        buildData();
    }

    private void buildData() throws SQLException
    {
        if (deviceManager != null)
        {
            Iterable<Device> devices = deviceManager.getDevices();

            for (Device device : devices)
            {
                filterInput.devices.add(new FilterDeviceState(device));
            }
        }
    }

    public FilterInput getByCategory(final String category)
    {
        FilterInput input = new FilterInput();

        if (deviceManager != null)
        {
            Iterable<Device> devices = deviceManager.getDevicesByType(category);
            for (Device device : devices)
            {
                input.devices.add(new FilterDeviceState(device));
            }
        }

        return input;
    }

    public FilterInput getFilterInput()
    {
        return filterInput;
    }
}

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
    // iPOJO fields
    //@Requires
    private final FilterInput filterInput = new FilterInput();


    public FilterDataGenerator(DeviceManager deviceManager) throws SQLException
    {
        buildData(deviceManager);
    }

    private void buildData(DeviceManager deviceManager) throws SQLException
    {
        if (deviceManager != null)
        {
            Iterable<Device> devices = deviceManager.getDevices();

            for (Device device : devices)
            {
                filterInput.devices.add(device);
            }
        }
    }

    public FilterInput getFilterInput()
    {
        return filterInput;
    }
}

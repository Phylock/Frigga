package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceData;
import java.util.LinkedList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phylock
 */
public class Transaction {
    private final List<DeviceData> devices = new LinkedList<DeviceData>();
    private final List<DeviceCategory> categories = new LinkedList<DeviceCategory>();
    
    public void addDevice(DeviceData data)
    {
        devices.add(data);
    }
    
    public void addCategory(DeviceCategory data)
    {
        categories.add(data);
    }

    public List<DeviceCategory> getCategories() {
        return categories;
    }

    public List<DeviceData> getDevices() {
        return devices;
    }

}

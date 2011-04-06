package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
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
    private final List<DeviceDescriptor> devices = new LinkedList<DeviceDescriptor>();
    private final List<CategoryDescriptor> categories = new LinkedList<CategoryDescriptor>();
    
    public void addDevice(DeviceDescriptor data)
    {
        devices.add(data);
    }
    
    public void addCategory(CategoryDescriptor data)
    {
        categories.add(data);
    }

    public List<CategoryDescriptor> getCategories() {
        return categories;
    }

    public List<DeviceDescriptor> getDevices() {
        return devices;
    }

}

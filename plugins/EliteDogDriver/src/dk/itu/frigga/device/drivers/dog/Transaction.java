package dk.itu.frigga.device.drivers.dog;


import dk.itu.frigga.device.CategoryData;
import dk.itu.frigga.device.DeviceData;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private final List<CategoryData> categories = new LinkedList<CategoryData>();
    
    public void addDevice(DeviceData data)
    {
        devices.add(data);
    }
    
    public void addCategory(CategoryData data)
    {
        categories.add(data);
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public List<DeviceData> getDevices() {
        return devices;
    }

}

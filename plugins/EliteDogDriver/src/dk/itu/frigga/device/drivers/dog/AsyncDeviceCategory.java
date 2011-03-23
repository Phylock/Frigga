/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceCategory;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is used by the configmessage parser when we learn about a device,
 * before we know about its category. Wile parsing it collects all devices with
 * missing informations and in the end configmessage makes a new request to
 * retrieve the information.
 * 
 * @author phylock
 */
public class AsyncDeviceCategory {

    private HashMap<String, LinkedList<Device>> async_unknown;

    public AsyncDeviceCategory() {
        async_unknown = new HashMap<String, LinkedList<Device>>();
    }

    public boolean hasUnknown() {
        return !async_unknown.isEmpty();
    }

    public String[] getUnknownDeviceCategories() {
        if (async_unknown.isEmpty()) {
            return null;
        }

        String[] keys = new String[async_unknown.size()];
        keys = async_unknown.keySet().toArray(keys);
        return keys;
    }

    public void addUnknownDeviceCategory(String devicecategory, Device device) {
        LinkedList<Device> devices;
        if (async_unknown.containsKey(devicecategory)) {
            devices = async_unknown.get(devicecategory);
        } else {
            devices = new LinkedList<Device>();
            async_unknown.put(devicecategory, devices);
        }
        devices.add(device);
    }

    public void makeDeviceCategoryKnown(DeviceCategory devicecategory) {
        if (async_unknown.containsKey(devicecategory.getTypeString())) {
            LinkedList<Device> devices =
                    async_unknown.get(devicecategory.getTypeString());

            int i = 0;
            for(Device device : devices)
            {
                Device newdevice= new Device(
                        device.getId(), devicecategory, device.getVariables(),
                        device.getLocation());
                //TODO: devicemanager.registerDevice(newdevice);
            }
            async_unknown.remove(devicecategory.getTypeString());
        }
    }

    public boolean isDeviceCategoryUnknown(String devicecategory)
    {
        return async_unknown.containsKey(devicecategory);
    }
}

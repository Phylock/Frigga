/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import java.util.List;

/**
 *
 * @author phylock
 */
public class DeviceUpdateEvent {

    private final String responsible;
    private final List<DeviceData> devices;
    private final List<DeviceCategory> categories;

    public DeviceUpdateEvent(String responsible, List<DeviceData> devices, List<DeviceCategory> categories) {
        this.responsible = responsible;
        this.devices = devices;
        this.categories = categories;
    }

    public List<DeviceCategory> getCategories() {
        return categories;
    }

    public List<DeviceData> getDevices() {
        return devices;
    }

    public String getResponsible() {
        return responsible;
    }

    public boolean hasCategories() {
        return ((categories != null) && categories.size() > 0);
    }

    public boolean hasDevices() {
        return ((categories != null) && categories.size() > 0);
    }
}

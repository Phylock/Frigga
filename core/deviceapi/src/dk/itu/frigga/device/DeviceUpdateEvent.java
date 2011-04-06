/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.model.Category;
import java.util.List;

/**
 *
 * @author phylock
 */
public class DeviceUpdateEvent {

    private final String responsible;
    private final List<DeviceDescriptor> devices;
    private final List<CategoryDescriptor> categories;

    public DeviceUpdateEvent(String responsible, List<DeviceDescriptor> devices, List<CategoryDescriptor> categories) {
        this.responsible = responsible;
        this.devices = devices;
        this.categories = categories;
    }

    public List<CategoryDescriptor> getCategories() {
        return categories;
    }

    public List<DeviceDescriptor> getDevices() {
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

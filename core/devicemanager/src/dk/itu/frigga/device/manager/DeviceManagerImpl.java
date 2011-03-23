/*
 * Copyright (c) 2011 Tommy Andersen and Mikkel Wendt-Larsen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dk.itu.frigga.device.manager;

import dk.itu.frigga.Singleton;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.utility.Filtering;
import dk.itu.frigga.utility.Applicable;
import dk.itu.frigga.utility.ReflectionHelper;
import java.util.HashMap;
import java.util.List;
import org.osgi.service.log.LogService;

/**
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public final class DeviceManagerImpl extends Singleton implements DeviceManager {

    private final static String DATAGROUP = "device";

    /* iPOJO services */
    private LogService log;
    private DataManager datamanager;
    private List<Driver> drivers;

    /* private */
    private final HashMap<DeviceId, Device> devices = new HashMap<DeviceId, Device>();
    private final HashMap<String, DeviceCategory> categories = new HashMap<String, DeviceCategory>();
    private DeviceDatabase connection;

    /**
     * We do not wish to have multiple instances, so the constructor is private.
     */
    public DeviceManagerImpl() {
        
    }

    public final boolean deviceIsOnline(final Device device) {
        //todo: implement
        return false;
    }

    public final void registerDevice(final Device device) {
        devices.put(device.getId(), device);
    }

    public final void unregisterDevice(final Device device) {
        devices.remove(device.getId());
    }

    public final void registerDeviceCategory(final DeviceCategory category) {
        categories.put(category.getTypeString(), category);
    }

    public final void unregisterDeviceCategory(final DeviceCategory category) {
        categories.remove(category.getTypeString());
    }

    public final DeviceCategory getDeviceCategory(String id) {
        return categories.get(id);
    }

    public void onDeviceEvent(DeviceUpdateEvent event)
    {
        System.out.println("event");
    }

    /**
     * use this function to retrieve a specific device.
     *
     * @param id The unique id of the device.
     *
     * @return The device found, or null if no device was found.
     */
    public final Device getDeviceById(final DeviceId id) {
        return devices.get(id);
    }

    /**
     * Calls the getDevicesByType(String) internally with the type string of the
     * category.
     *
     * @see getDevicesByType(String)
     *
     * @param type A DeviceCategory object identifying the type whose devices to
     *             fetch.
     *
     * @return Returns an array of devices of the given type.
     */
    public final Iterable<Device> getDevicesByType(final DeviceCategory category) {
        Applicable<Device> typeCheck = new Applicable<Device>() {

            @Override
            public boolean apply(final Device device) {
                return device.isOfType(category);
            }
        };

        return Filtering.filter(devices.values(), typeCheck);
    }

    /**
     * Returns an array of devices having a specified type. This function is
     * generally a bit heavy to use, since it performs an O(n) search to find
     * devices of the given type. As compared to retrieving a device by ID which
     * takes advantage of the hash map and uses a O(1) search.
     *
     * @param type A string identifying the type whose devices to fetch.
     *
     * @return Returns an array of devices of the given type.
     */
    public final Iterable<Device> getDevicesByType(final String type) {
        Applicable<Device> typeCheck = new Applicable<Device>() {

            @Override
            public boolean apply(final Device device) {
                return device.isOfType(type);
            }
        };

        return Filtering.filter(devices.values(), typeCheck);
    }

    public void deviceDriverAdded(Driver driver) {
        log.log(LogService.LOG_INFO, "Device Driver Added: ");
        driver.update();
    }

    public void deviceDriverRemoved(Driver driver) {
        log.log(LogService.LOG_INFO, "Device Driver Removed: ");
        //TODO: set the devices that the driver is responsible of offline
    }

    public void validate() {
        log.log(LogService.LOG_INFO, "Device Manager: Validate");
        connection = new DeviceDatabase(DATAGROUP, "devices.db");
        try {
            ReflectionHelper.updateSubclassFields("log", connection, log);
            ReflectionHelper.updateSubclassFields("datamanager", connection, datamanager);
        } catch (Exception ex) {
        }

        connection.initialize();
    }
}

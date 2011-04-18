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
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.utility.ReflectionHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  /* private */
  private final Map<String, Driver> drivers = new HashMap<String, Driver>();
  private final Map<String, Driver> responsebility = new HashMap<String, Driver>();
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

  public final Category getDeviceCategory(String id) {
    return null;//categories.get(id);
  }

  public void onDeviceEvent(final DeviceUpdateEvent event) {
    Driver responsible = drivers.get(event.getResponsible());
    for (DeviceDescriptor data : event.getDevices()) {
      responsebility.put(data.getSymbolic(), responsible);
    }
    new Thread(new Runnable() {

      public void run() {
        try {
          connection.update(event);
        } catch (SQLException ex) {
          log.log(LogService.LOG_WARNING, "Device Update SQL Error", ex);
        } catch (Exception ex) {
          log.log(LogService.LOG_WARNING, "Device Update Error", ex);
        }
      }
    }).start();
  }

  /**
   * use this function to retrieve a specific device.
   *
   * @param id The unique id of the device.
   *
   * @return The device found, or null if no device was found.
   */
  public final Device getDeviceById(final DeviceId id) {
    DeviceDAO d = connection.getDeviceDao();
    return d.findBySymbolic(id.toString());
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
  public final Iterable<Device> getDevicesByType(final Category category) {
    DeviceDAO d = connection.getDeviceDao();
    return d.findByCategory(category);
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
    CategoryDAO c = connection.getCategoryDao();
    Category category = c.findByName(type);
    return getDevicesByType(category);
  }

  public final Iterable<Device> getDevices() {
    DeviceDAO d = connection.getDeviceDao();
    return d.findAll();
  }

  public void deviceDriverAdded(Driver driver) {
    log.log(LogService.LOG_INFO, "Device Driver Added: ");
    synchronized (drivers) {
      drivers.put(driver.getDriverId(), driver);
    }
    driver.update();
  }

  public void deviceDriverRemoved(Driver driver) {
    synchronized (drivers) {
      drivers.remove(driver.getDriverId());
    }
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

  public void invalidate() {
    connection.close();
    connection = null;
  }

  /**
   * Call a function on multiple devices
   * @param function
   * @param devices
   * @param parameters
   * @return
   */
  public FunctionResult callFunction(String function, String[] devices, Parameter... parameters) {
    List<String> failed_block = new ArrayList<String>();
    //if there is only one device, there can be only one responsible driver
    if (devices.length > 1) {
      List<String> calldevices = Arrays.asList(devices);
      List<String> current_block = new ArrayList<String>();
      boolean done = false;
      while (!done) {
        //pop the first and find all devices which uses the same driver
        if (responsebility.containsKey(calldevices.get(0))) {
          Driver current = responsebility.get(calldevices.get(0));
          for (String device : devices) {
            if (responsebility.get(device).equals(current)) {
              current_block.add(device);
            }
          }

          try {
            current.callFunction(current_block.toArray(new String[current_block.size()]), function, parameters);
          } catch (Exception ex) {
            failed_block.add(devices[0]);
          }
          calldevices.removeAll(current_block);
          current_block.clear();

        } else {
          failed_block.add(calldevices.get(0));
          calldevices.remove(0);
        }
        if (calldevices.isEmpty()) {
          done = true;
        }

      }
    } else {
      if (responsebility.containsKey(devices[0])) {
        Driver d = responsebility.get(devices[0]);
        try {
          d.callFunction(devices, function, parameters);
        } catch (Exception ex) {
          failed_block.add(devices[0]);
        }
      }
    }
    return new FunctionResult(failed_block.isEmpty() ? "OK" : "Failed");
  }
}

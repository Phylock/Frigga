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
import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.device.*;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.model.*;
import dk.itu.frigga.utility.ReflectionHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.service.log.LogService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author Tommy Andersen (toan@itu.dk)
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public final class DeviceManagerImpl extends Singleton implements DeviceManager {

  enum DeviceChangedType {

    Variable, Online, LocalLocation, GlobalLocation
  }
  private final static String DATAGROUP = "device";

  /* iPOJO services */
  private LogService log;
  private DataManager datamanager;
  /* private */
  private final Map<String, Driver> drivers = new HashMap<String, Driver>();
  private DeviceDatabase connection;
  private ConnectionPool pool;
  private ThreadPoolExecutor event_handler_pool;
  private BundleContext context;

  /**
   * We do not wish to have multiple instances, so the constructor is private.
   */
  private DeviceManagerImpl(BundleContext context) {
    this.context = context;
  }

  void fireDeviceChanged(String device, DeviceChangedType type, String value) {
    //TODO: cache this for a short time, because there is a minimal change that EventAdmin will go offline, for performence
    ServiceReference ref = context.getServiceReference(EventAdmin.class.getName());
    if (ref != null) {
      EventAdmin eventAdmin = (EventAdmin) context.getService(ref);

      Map properties = new HashMap<String, String>();
      properties.put("device", device);
      properties.put("value", value);
      properties.put("time", System.currentTimeMillis());

      Event reportGeneratedEvent = new Event("dk/itu/frigga/device/status/" + type.toString(), properties);

      eventAdmin.postEvent(reportGeneratedEvent);
    }
  }

  public final boolean deviceIsOnline(final Device device) {
    if (drivers.containsKey(device.getSymbolic())) {
      //TODO: check the real value from the database ...
      return true;
    }
    return false;
  }

  public final Category getDeviceCategory(String id) {
    return null;//categories.get(id);
  }

  public void onDeviceEvent(final DeviceUpdateEvent event) {
    log.log(LogService.LOG_INFO, "Device Update Event recieved: " + event.getResponsible());
    event_handler_pool.execute(new Runnable() {
      public void run() {
        try {
          connection.update(event);
        } catch (SQLException ex) {
          log.log(LogService.LOG_WARNING, "Variable Changed Event - SQL Error", ex);
        }
      }
    });
  }

  public void onVariableChangeEvent(final VariableChangedEvent event) {

    event_handler_pool.execute(new Runnable() {
      public void run() {
        try {
          connection.update(event);
        } catch (SQLException ex) {
          log.log(LogService.LOG_WARNING, "Variable Changed Event - SQL Error", ex);
        }
      }
    });
  }

  /**
   * use this function to retrieve a specific device.
   *
   * @param id The unique id of the device.
   *
   * @return The device found, or null if no device was found.
   */
  public final Device getDeviceById(final DeviceId id) {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO d = DeviceDaoFactorySql.instance().getDeviceDao(conn);
      return d.findBySymbolic(id.toString());
    } catch (SQLException ex) {
    } finally {
      pool.releaseConnection(conn);
    }
    return null;
  }

  /**
   * Calls the getDevicesByType(String) internally with the type string of the category.
   *
   * @param category A DeviceCategory object identifying the type whose devices to fetch.
   *
   * @return Returns an array of devices of the given type.
   *
   * @see
   */
  public final Iterable<Device> getDevicesByType(final Category category) {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO d = DeviceDaoFactorySql.instance().getDeviceDao(conn);

      return d.findByCategory(category);
    } catch (SQLException ex) {
    } finally {
      pool.releaseConnection(conn);
    }
    return null;
  }

  /**
   * Returns an array of devices having a specified type. This function is generally a bit heavy to use, since it
   * performs an O(n) search to find devices of the given type. As compared to retrieving a device by ID which takes
   * advantage of the hash map and uses a O(1) search.
   *
   * @param type A string identifying the type whose devices to fetch.
   *
   * @return Returns an array of devices of the given type.
   */
  public final Iterable<Device> getDevicesByType(final String type) {
    Category category = new Category(type);
    return getDevicesByType(category);
  }

  public final Iterable<Device> getDevices() {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO d = DeviceDaoFactorySql.instance().getDeviceDao(conn);

      return d.findAll();
    } catch (SQLException ex) {
    } finally {
      pool.releaseConnection(conn);
    }
    return null;
  }

  public void deviceDriverAdded(Driver driver) {
    log.log(LogService.LOG_INFO, "Device Driver Added: " + driver.getDriverId());
    synchronized (drivers) {
      drivers.put(driver.getDriverId(), driver);
    }
    driver.update();
  }

  public void deviceDriverRemoved(Driver driver) {
    synchronized (drivers) {
      drivers.remove(driver.getDriverId());
    }

    log.log(LogService.LOG_INFO, "Device Driver Removed: " + driver.getDriverId());

    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO d = DeviceDaoFactorySql.instance().getDeviceDao(conn);
      d.setStateByDriver(driver.getDriverId(), false);
    } catch (SQLException ex) {
    } finally {
      pool.releaseConnection(conn);
    }
  }

  public void validate() {
    log.log(LogService.LOG_INFO, "Device Manager: Validate");
    connection = new DeviceDatabase(DATAGROUP, "devices.db", this);
    try {
      ReflectionHelper.updateSubclassFields("log", connection, log);
      ReflectionHelper.updateSubclassFields("datamanager", connection, datamanager);
    } catch (Exception ex) {
    }

    pool = connection.initialize();
    event_handler_pool = new ThreadPoolExecutor(pool.totalConnections(), pool.totalConnections(), 15, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO deviceDao = DeviceDaoFactorySql.instance().getDeviceDao(conn);
      deviceDao.setStateByDriver("", false);
    } catch (SQLException ex) {
      Logger.getLogger(DeviceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (conn != null) {
        pool.releaseConnection(conn);
      }
    }
  }

  public void invalidate() {
    connection.close();
    connection = null;

    event_handler_pool.shutdown();
    event_handler_pool = null;

    pool = null;

  }

  /**
   * Call a function on multiple devices
   *
   * @param function
   * @param devices
   * @param parameters
   *
   * @return
   */
  public FunctionResult callFunction(String function, Device[] devices, Parameter... parameters) {
    List<Device> failed_block = new ArrayList<Device>();
    //if there is only one device, there can be only one responsible driver
    if (devices.length > 1) {
      Queue<Device> calldevices = new ArrayDeque<Device>(Arrays.asList(devices));
      List<String> current_block = new ArrayList<String>();
      boolean done = false;
      while (!done) {
        //pop the first and find all devices which uses the same driver
        if (drivers.containsKey(calldevices.peek().getDriver())) {
          Device current = calldevices.poll();
          Driver current_driver = drivers.get(current.getDriver());
          for (Device device : devices) {
            if (device.getDriver().equals(current.getDriver())) {
              current_block.add(device.getSymbolic());
            }
          }

          try {
            current_driver.callFunction(current_block.toArray(new String[current_block.size()]), function, parameters);
          } catch (Exception ex) {
            failed_block.add(current);
          }
          calldevices.removeAll(current_block);
          current_block.clear();

        } else {
          failed_block.add(calldevices.poll());
        }
        if (calldevices.isEmpty()) {
          done = true;
        }
      }
    } else {
      if (drivers.containsKey(devices[0].getDriver())) {
        Driver d = drivers.get(devices[0].getDriver());
        try {
          d.callFunction(new String[]{devices[0].getSymbolic()}, function, parameters);
        } catch (Exception ex) {
          failed_block.add(devices[0]);
        }
      }
    }
    return new FunctionResult(failed_block.isEmpty() ? "OK" : "Failed");
  }

  public DeviceDaoFactory getDeviceDaoFactory() {
    return DeviceDaoFactorySql.instance();
  }
}

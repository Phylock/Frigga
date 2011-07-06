/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.model.Category;

/**
 *
 * @author phylock
 */
public interface DeviceManager {

  boolean deviceIsOnline(final Device device);

  /**
   * use this function to retrieve a specific device.
   *
   * @param id The unique id of the device.
   *
   * @return The device found, or null if no device was found.
   */
  Device getDeviceById(final DeviceId id);

  Category getDeviceCategory(String id);

  /**
   * Calls the getDevicesByType(String) internally with the type string of the
   * category.
   *
   * @see getDevicesByType(String)
   *
   * @param type A DeviceCategory object identifying the type whose devices to
   * fetch.
   *
   * @return Returns an array of devices of the given type.
   */
  Iterable<Device> getDevicesByType(final Category category);

  /**
   * Returns an array of devices having a specified type. 
   *
   * @param type A string identifying the type whose devices to fetch.
   *
   * @return Returns an array of devices of the given type.
   */
  Iterable<Device> getDevicesByType(final String type);

/**
 * Returns an array of all devices known by the system, in a large system this
 * can be a very heavy function.
 * @return
 */
  Iterable<Device> getDevices();

  FunctionResult callFunction(String function, Device[] devices, Parameter... parameters);

  DeviceDaoFactory getDeviceDaoFactory();

}

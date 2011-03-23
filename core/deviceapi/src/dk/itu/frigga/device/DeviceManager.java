/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

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

  DeviceCategory getDeviceCategory(String id);

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
  Iterable<Device> getDevicesByType(final DeviceCategory category);

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
  Iterable<Device> getDevicesByType(final String type);

}

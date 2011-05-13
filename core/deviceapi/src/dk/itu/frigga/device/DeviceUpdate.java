/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

/**
 *
 * @author phylock
 */
public class DeviceUpdate {
  private String device;
  private boolean online;

  public DeviceUpdate(String device, boolean online) {
    this.device = device;
    this.online = online;
  }

  public String getDevice() {
    return device;
  }

  public boolean isOnline() {
    return online;
  }
}

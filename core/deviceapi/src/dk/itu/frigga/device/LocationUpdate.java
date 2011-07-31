/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import dk.itu.frigga.device.model.Point3;

/**
 *
 * @author phylock
 */
public class LocationUpdate {

  public enum Type {

    Local, Global
  }

  private final Type type;
  private final Point3 point;
  private final String room;
  private final String device;
  private final boolean static_location;

  /**
   * update the global position of a device
   * @param device
   * @param point
   */
  public LocationUpdate(String device, Point3 point) {
    this.type = Type.Global;
    this.device = device;
    this.point = point;
    this.room = "";
    this.static_location = false;
  }
/**
 * update the local position of the device
 * @param device
 * @param point
 * @param room
 */
  public LocationUpdate(String device, Point3 point, String room) {
    type = Type.Local;
    this.device = device;
    this.room = room;
    this.point = point;
    this.static_location = false;
  }

  public LocationUpdate(String device, Point3 point, String room, boolean static_location) {
    type = Type.Local;
    this.device = device;
    this.room = room;
    this.point = point;
    this.static_location = static_location;
  }

  public Point3 getPoint() {
    return point;
  }

  public String getRoom() {
    return room;
  }

  public Type getType() {
    return type;
  }

  public String getDevice() {
    return device;
  }

  public boolean isStaticLocation() {
    return static_location;
  }

}

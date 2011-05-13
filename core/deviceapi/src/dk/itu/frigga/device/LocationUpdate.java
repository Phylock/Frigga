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

    Local, Global, Room
  }

  private Type type;
  private Point3 point;
  private String room;

  public LocationUpdate(Type type, Point3 point) {
    this.type = type;
    this.point = point;
  }

  public LocationUpdate(String room) {
    type = Type.Room;
    this.room = room;
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
}

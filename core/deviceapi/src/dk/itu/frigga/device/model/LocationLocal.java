/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.model;

import java.util.Date;

/**
 *
 * @author phylock
 */
public class LocationLocal extends Location{
  private String room;

  public LocationLocal(Long device_id, String room, Point3<Double> position, String sender, Date updated) {
    super(device_id, position,sender, updated);
    this.room = room;
  }

  public LocationLocal(Long device_id, String room, Point3<Double> position, Point3<Double> velocity, String sender, Date updated) {
    super(device_id, position, velocity,sender, updated);
    this.room = room;
  }
  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }
}

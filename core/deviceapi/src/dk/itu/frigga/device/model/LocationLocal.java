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

  public LocationLocal(String device, Point3<Double> position, String sender, Date updated, String room) {
    super(device, position, sender, updated);
    this.room = room;
  }

  public LocationLocal(String device, Point3<Double> position, Point3<Double> velocity, String sender, Date updated, String room) {
    super(device, position, velocity, sender, updated);
    this.room = room;
  }

  public LocationLocal(Long id, String device, Point3<Double> position, String sender, Date updated, String room) {
    super(id, device, position,  sender, updated);
    this.room = room;
  }

  public LocationLocal(Long id, String device, Point3<Double> position, Point3<Double> velocity, String sender, Date updated, String room) {
    super(id, device, position, velocity, sender, updated);
    this.room = room;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }
}

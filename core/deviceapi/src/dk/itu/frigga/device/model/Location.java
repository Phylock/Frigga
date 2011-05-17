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
public class Location {

  protected Long device_id;
  protected Point3 position;
  protected Point3 velocity;
  protected String sender;
  protected Date updated;

  public Location(Long device_id, Point3 position, Point3 velocity, String sender, Date updated) {
    this.device_id = device_id;
    this.position = position;
    this.velocity = velocity;
    this.sender = sender;
    this.updated = updated;
  }

  public Location(Long device_id, Point3 position, String sender, Date updated) {
    this.device_id = device_id;
    this.position = position;
    this.sender = sender;
    this.updated = updated;
  }


  public void setDeviceId(Long device_id) {
    this.device_id = device_id;
  }

  public void setPosition(Point3 position) {
    this.position = position;
  }

  public void setVelocity(Point3 velocity) {
    this.velocity = velocity;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public Long getDeviceId() {
    return device_id;
  }

  public Point3 getPosition() {
    return position;
  }

  public Point3 getVelocity() {
    return velocity;
  }

  public String getSender() {
    return sender;
  }

  public Date getUpdated() {
    return updated;
  }


}

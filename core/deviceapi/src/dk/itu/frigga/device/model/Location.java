/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.model;

import java.io.Serializable;

//@Entity
//@Table(name = "location")
public class Location implements Serializable{
  /** ID **/
  //@Id
  //@GeneratedValue
  //@Column(name = "id", unique = true, nullable = false)
  private long id = 1L;
  /** Point **/
  //@Embedded
  private Point3 point;

  public Location() {
  }

  public Location(Point3 point) {
    this.point = point;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Point3 getPoint() {
    return point;
  }

  public void setPoint(Point3 point) {
    this.point = point;
  }

  @Override
  public String toString() {
    return "Location{" + "id=" + id + ", point=" + point + '}';
  }

}

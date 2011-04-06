/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.model;

import java.io.Serializable;

/**
 *
 * @author phylock
 */
//@Embeddable
public class Point3 implements Serializable{
  //@Column(name = "x")
  private long x;
  //@Column(name = "y")
  private long y;
  //@Column(name = "z")
  private long z;

  public Point3(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point3() {
  }

  public long getX() {
    return x;
  }

  public void setX(long x) {
    this.x = x;
  }

  public long getY() {
    return y;
  }

  public void setY(long y) {
    this.y = y;
  }

  public long getZ() {
    return z;
  }

  public void setZ(long z) {
    this.z = z;
  }

  @Override
  public String toString() {
    return "Point3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}

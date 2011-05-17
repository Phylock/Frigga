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
public class Point3<T> implements Serializable{
  //@Column(name = "x")
  private T x;
  //@Column(name = "y")
  private T y;
  //@Column(name = "z")
  private T z;

  public Point3(T x, T y, T z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point3() {
  }

  public T getX() {
    return x;
  }

  public void setX(T x) {
    this.x = x;
  }

  public T getY() {
    return y;
  }

  public void setY(T y) {
    this.y = y;
  }

  public T getZ() {
    return z;
  }

  public void setZ(T z) {
    this.z = z;
  }

  @Override
  public String toString() {
    return "Point3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}

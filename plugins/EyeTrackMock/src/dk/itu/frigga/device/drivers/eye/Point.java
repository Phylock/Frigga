/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.eye;

/**
 *
 * @author phylock
 */
public class Point {
  private int x;
  private int y;
  private String lookat;

  public Point(int x, int y, String lookat) {
    this.x = x;
    this.y = y;
    this.lookat = lookat;
  }

  public String getLookat() {
    return lookat;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}

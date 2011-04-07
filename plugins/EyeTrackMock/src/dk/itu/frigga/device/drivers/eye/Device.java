/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.eye;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class Device {

  public enum Type {

    None, Point, Area
  }
  private String lookat;
  private List<Point> points;

  public Device(String lookat) {
    this.lookat = lookat;
    this.points = new ArrayList<Point>();
  }

  public void addPoint(int x, int y) {
    points.add(new Point(x, y));
  }

  public void addPoint(Point p) {
    points.add(p);
  }

  public String getLookat() {
    return lookat;
  }

  public List<Point> getPoints() {
    return points;
  }

  public Type getType() {
    if (points.isEmpty() || points.size() == 2) {
      return Type.None;
    } else if (points.size() == 1) {
      return Type.Point;
    } else {
      return Type.Area;
    }
  }
}

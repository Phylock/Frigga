/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.eye;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class Device {

  public static final int POINT_SIZE = 20;
  public static final int POINT_HALFSIZE = POINT_SIZE / 2;

  public enum Type {

    None, Point, Area
  }
  private String lookat;
  private List<Point> points;
  private Polygon polygon;
  private Type type;

  public Device(String lookat) {
    this.lookat = lookat;
    this.points = new ArrayList<Point>();
    polygon = new Polygon();
    cacheType();
  }

  public void addPoint(int x, int y) {
    addPoint(new Point(x, y));
  }

  public void addPoint(Point p) {
    points.add(p);
    polygon.addPoint(p.getX(), p.getY());
    cacheType();
  }

  public String getLookat() {
    return lookat;
  }

  public List<Point> getPoints() {
    return points;
  }

  public boolean contains(int x, int y) {
    switch (type) {
      case Point:

        Point p = points.get(0);
        int dx = Math.abs(x - p.getX());
        int dy = Math.abs(y - p.getY());

        return (dx <= POINT_HALFSIZE && dy <= POINT_HALFSIZE);
      case Area:
        return polygon.contains(x, y);
      default:
        return false;
    }
  }

  public Point toLocal(int x, int y) {
    int px;
    int py;
    switch (type) {
      case Point:
        Point p = points.get(0);
        px = x - (p.getX() - POINT_HALFSIZE);
        py = y - (p.getY() - POINT_HALFSIZE);
        return new Point(px, py);
      case Area:
        Rectangle r = polygon.getBounds();
        px = x - (int)r.getX();
        py = y - (int)r.getY();
        return new Point(px, py);
    }
    return null;
  }

  public Type getType() {
    return type;
  }

  private void cacheType() {
    if (points.isEmpty() || points.size() == 2) {
      type = Type.None;
    } else if (points.size() == 1) {
      type = Type.Point;
    } else {
      type = Type.Area;
    }
  }
}

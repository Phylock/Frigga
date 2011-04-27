package dk.itu.frigga.device.drivers.eye;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author phylock
 */
public interface EyeChange {
  void selectionChanged(String selection);
  void localChanged(Point p);
}

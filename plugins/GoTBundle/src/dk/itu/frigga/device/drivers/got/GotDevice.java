/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.got;

import java.util.Date;

/**
 * private container class
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
class GotDevice {

  long id;
  String attachedto;
  long lastSeen;
  long valid_packages;
  long lost_packages;

  public GotDevice(long id) {
    this.id = id;
    this.attachedto = "";
    this.lastSeen = 0;
    this.valid_packages = 0;
    this.lost_packages = 0;
  }

  public void resetPackageCount() {
    this.valid_packages = 0;
    this.lost_packages = 0;
  }
}

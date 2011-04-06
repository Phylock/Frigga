/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.descriptor;

/**
 *
 * @author phylock
 */
public class DeviceDescriptor {
  private String name;
  private String symbolic;
  private String[] categories;

  public DeviceDescriptor(String name, String symbolic, String[] categories) {
    this.name = name;
    this.symbolic = symbolic;
    this.categories = categories;
  }

  public String[] getCategories() {
    return categories;
  }

  public String getName() {
    return name;
  }

  public String getSymbolic() {
    return symbolic;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.descriptor;

/**
 *
 * @author phylock
 */
public class VariableDescriptor {
  private String name;
  private String type;

  public VariableDescriptor(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }
}

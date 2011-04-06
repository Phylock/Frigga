/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.descriptor;

/**
 *
 * @author phylock
 */
public class FunctionDescriptor {
  private String name;
  private String[] param;

  public FunctionDescriptor(String name, String[] param) {
    this.name = name;
    this.param = param;
  }

  public String getName() {
    return name;
  }

  public String[] getParam() {
    return param;
  }
}

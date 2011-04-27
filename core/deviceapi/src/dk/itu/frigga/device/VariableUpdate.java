/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

/**
 *
 * @author phylock
 */
public class VariableUpdate {
  private String device;
  private String variable;
  private String value;

  public VariableUpdate(String device, String variable, String value) {
    this.device = device;
    this.variable = variable;
    this.value = value;
  }

  public String getDevice() {
    return device;
  }

  public String getValue() {
    return value;
  }

  public String getVariable() {
    return variable;
  }
}

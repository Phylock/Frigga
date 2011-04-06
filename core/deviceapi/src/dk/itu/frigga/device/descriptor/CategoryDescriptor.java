/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.descriptor;

/**
 *
 * @author phylock
 */
public class CategoryDescriptor {
  private String name;
  private String[] variables;
  private String[] functions;

  public CategoryDescriptor(String name, String[] variables, String[] functions) {
    this.name = name;
    this.variables = variables;
    this.functions = functions;
  }

  public String[] getFunctions() {
    return functions;
  }

  public String getName() {
    return name;
  }

  public String[] getVariables() {
    return variables;
  }
}

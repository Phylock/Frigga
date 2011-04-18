/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

/**
 *
 * @author phylock
 */
public class Replacement {
  private String name;
  private String description;
  private String type;

  public Replacement(String name, String description, String type) {
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Replacement{" + "name=" + name + ", description=" + description + ", type=" + type + '}';
  }
  
}

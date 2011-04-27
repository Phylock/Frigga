/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.model;

import java.io.Serializable;

/**
 *
 * @author phylock
 */
//@Entity
//@Table(name = "variabletype")
public class VariableType implements Serializable {

  /** ID **/
  //@Id
  //@GeneratedValue
  //@Column(name = "id", unique = true, nullable = false)
  private Long id = null;
  /** Name **/
  //@Column(name = "name", unique = true, nullable = false, length = 30)
  private String name;
  /** Name **/
  //@Column(name = "type", nullable = false, length = 30)
  private String type;

  public VariableType() {
    this.id = null;
    this.name = "";
    this.type = "";
  }

  public VariableType(Long id) {
    this.id = id;
  }

  public VariableType(String name, String type) {
    this.id = null;
    this.name = name;
    this.type = type;
  }

  public VariableType(Long id, String name, String type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final VariableType other = (VariableType) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "VariableType{" + "id=" + id + ", name=" + name + ", type=" + type + '}';
  }
}

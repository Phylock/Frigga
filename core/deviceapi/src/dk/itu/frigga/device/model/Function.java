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
//@Table(name = "function")
public class Function implements Serializable {

  /** ID **/
  //@Id
  //@GeneratedValue
  //@Column(name = "id", unique = true, nullable = false)
  private long id = 1L;
  /** Name **/
  //@Column(name = "name", unique = true, nullable = false, length = 30)
  private String name;

  public Function() {
  }

  public Function(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Function{" + "id=" + id + ", name=" + name + '}';
  }
}

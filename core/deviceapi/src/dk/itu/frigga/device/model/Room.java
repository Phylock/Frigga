/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.model;

/**
 *
 * @author phylock
 */
//@Entity
//@Table(name = "room")
public class Room {

  /** ID **/
  //@Id
  //@GeneratedValue
  //@Column(name = "id", unique = true, nullable = false)
  private Long id = null;
  /** Name **/
  //@Column(name = "name", unique = true, nullable = false, length = 30)
  private String name;

  public Room() {
  }

  public Room(String name) {
    this.name = name;
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

  @Override
  public String toString() {
    return "Room{" + "id=" + id + ", name=" + name + '}';
  }


}

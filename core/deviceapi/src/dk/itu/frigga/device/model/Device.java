/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author phylock
 */
//@Entity
//@Table(name = "device")
public class Device implements Serializable {

  private static final long serialVersionUID = 1L;
  //@Id
  //@GeneratedValue
  private Long id;
  //@Column(name = "name", nullable = false)
  private String name;
  //@Column(name = "symbolic", nullable = false, unique = true)
  private String symbolic;
  //@Temporal(TemporalType.TIME)
  //@Column(name = "last_update", nullable = false)
  private Date last_update;
  //@Column(name = "online", nullable = false)
  private boolean online;

  private String driver;
  //@ManyToMany(cascade=CascadeType.ALL)
  //@JoinTable(name="device_category", joinColumns= { @JoinColumn(name = "device_id")}, inverseJoinColumns={@JoinColumn(name="category_id")} )
  private List<Category> categories = new ArrayList<Category>(0);

  //@OneToMany(cascade=CascadeType.ALL)
  //@JoinColumn(name="device_id")
  private Set<Variable> variables = new HashSet<Variable>(0);

  public Device() {
    this.id = null;
    this.name = "";
    this.symbolic = "";
    this.last_update = null;
    this.online = false;
    this.driver = "";
  }

  public Device(Long id) {
    this.id = id;
    this.name = "";
    this.symbolic = "";
    this.last_update = null;
    this.online = false;
    this.driver = "";
  }

  public Device(String name, String symbolic, Date last_update, boolean online, String driver) {
    this.id = null;
    this.name = name;
    this.symbolic = symbolic;
    this.last_update = last_update;
    this.online = online;
    this.driver = driver;
  }

  public Device(Long id, String name, String symbolic, Date last_update, boolean online, String driver) {
    this.id = id;
    this.name = name;
    this.symbolic = symbolic;
    this.last_update = last_update;
    this.online = online;
    this.driver = driver;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getLastUpdate() {
    return last_update;
  }

  public void setLastUpdate(Date last_update) {
    this.last_update = last_update;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public String getSymbolic() {
    return symbolic;
  }

  public void setSymbolic(String symbolic) {
    this.symbolic = symbolic;
  }

  public List<Category>getCategories()
  {
    return categories;
  }

  public void setCategories(List<Category> categories)
  {
    this.categories = categories;
  }

  public Set<Variable> getVariables() {
    return variables;
  }

  public void setVariables(Set<Variable> variables) {
    this.variables = variables;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  @Override
  public String toString() {
    return "Device{" + "id=" + id + ", name=" + name + ", symbolic=" + symbolic + ", last_update=" + last_update + ", online=" + online + '}';
  }
}

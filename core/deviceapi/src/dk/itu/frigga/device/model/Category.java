/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phylock
 */
//@Entity
//@Table(name = "category")
public class Category implements Serializable {

  private static final Long serialVersionUID = 1L;
  /** ID **/
  //@Id
  //@GeneratedValue
  //@Column(name = "id", unique = true, nullable = false)
  private Long id = null;
  /** Name **/
  //@Column(name = "name", unique = true, nullable = false, length = 30)
  private String name;
  /** Devices **/
  //@ManyToMany(cascade = CascadeType.ALL)
  //@JoinTable(name = "device_category", joinColumns = {
  //  @JoinColumn(name = "category_id")}, inverseJoinColumns = {
  //  @JoinColumn(name = "device_id")})
  private List<Device> devices = new ArrayList<Device>(0);
  /** Functions **/
  //@ManyToMany(cascade = CascadeType.ALL)
  //@JoinTable(name = "category_function", joinColumns = {
  //  @JoinColumn(name = "category_id")}, inverseJoinColumns = {
  //  @JoinColumn(name = "function_id")})
  private List<Function> functions = new ArrayList<Function>(0);
  /** Variables **/
  //@ManyToMany(cascade = CascadeType.ALL)
  //@JoinTable(name = "category_variabletype", joinColumns = {
  //  @JoinColumn(name = "category_id")}, inverseJoinColumns = {
  //  @JoinColumn(name = "variabletype_id")})
  private List<VariableType> variabletypes = new ArrayList<VariableType>(0);


  public Category() {
  }

  public Category(String name) {
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

  public List<Device> getDevices() {
    return devices;
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }

  public List<Function> getFunctions() {
    return functions;
  }

  public void setFunctions(List<Function> functions) {
    this.functions = functions;
  }

  public List<VariableType> getVariabletypes() {
    return variabletypes;
  }

  public void setVariabletypes(List<VariableType> variabletypes) {
    this.variabletypes = variabletypes;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Category other = (Category) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", name=" + name + '}';
  }
}

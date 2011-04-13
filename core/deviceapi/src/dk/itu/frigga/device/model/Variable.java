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
//@Table(name = "variable")
public class Variable implements Serializable{
  /** ID **/
  //@Id
  private VariablePK primaryKey = new VariablePK();

  /** Value **/
  //@Column(name = "value")
  private String value;

  public Variable() {
  }

  public Variable(VariablePK primaryKey, String value) {
    this.primaryKey = primaryKey;
    this.value = value;
  }

  public VariablePK getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(VariablePK primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Variable other = (Variable) obj;
    if (this.primaryKey != other.primaryKey && (this.primaryKey == null || !this.primaryKey.equals(other.primaryKey))) {
      return false;
    }
    if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + (this.primaryKey != null ? this.primaryKey.hashCode() : 0);
    hash = 41 * hash + (this.value != null ? this.value.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "Variable{" + "primaryKey=" + primaryKey + ", value=" + value + '}';
  }
}

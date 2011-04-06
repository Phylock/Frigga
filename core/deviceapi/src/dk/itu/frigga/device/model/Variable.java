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
}

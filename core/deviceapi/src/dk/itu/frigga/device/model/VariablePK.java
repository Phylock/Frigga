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
  //@Embeddable
public class VariablePK implements Serializable
  {
    //@ManyToOne
    private Device device;

    //@ManyToOne
    private VariableType variabletype;

  public VariablePK() {
  }

  public VariablePK(Device device, VariableType variabletype) {
    this.device = device;
    this.variabletype = variabletype;
  }



  @Override
  public String toString() {
    return "VariablePK{" + "device_id=" + device + ", variabletype_id=" + variabletype + '}';
  }
}

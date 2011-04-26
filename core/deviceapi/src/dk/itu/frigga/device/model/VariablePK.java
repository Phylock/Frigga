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
    private Long device;

    //@ManyToOne
    private Long variabletype;

  public VariablePK() {
  }

  public VariablePK(Long device, Long variabletype) {
    this.device = device;
    this.variabletype = variabletype;
  }

  public Long getDevice() {
    return device;
  }

  public Long getVariabletype() {
    return variabletype;
  }

  @Override
  public String toString() {
    return "VariablePK{" + "device_id=" + device + ", variabletype_id=" + variabletype + '}';
  }
}

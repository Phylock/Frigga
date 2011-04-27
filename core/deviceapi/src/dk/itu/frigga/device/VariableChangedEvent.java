/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import dk.itu.frigga.device.descriptor.VariableDescriptor;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class VariableChangedEvent {

  protected final List<VariableUpdate> variables;

  public VariableChangedEvent() {
    variables = new LinkedList<VariableUpdate>();
  }

  public List<VariableUpdate> getVariables() {
    return variables;
  }
}

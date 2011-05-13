/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class VariableChangedEvent {

  protected final List<VariableUpdate> variables;
  protected final List<DeviceUpdate> state;
  protected final List<LocationUpdate> location;

  public VariableChangedEvent() {
    variables = new LinkedList<VariableUpdate>();
    state = new LinkedList<DeviceUpdate>();
    location = new LinkedList<LocationUpdate>();
  }

  public List<VariableUpdate> getVariables() {
    return variables;
  }

  public List<DeviceUpdate> getState() {
    return state;
  }

  public List<LocationUpdate> getLocation() {
    return location;
  }

  public boolean hasVariables() {
    return !variables.isEmpty();
  }

  public boolean hasState() {
    return !state.isEmpty();
  }

  public boolean hasLocation() {
    return !location.isEmpty();
  }
}

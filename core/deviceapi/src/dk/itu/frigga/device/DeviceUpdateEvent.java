/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device;

import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import java.util.List;

/**
 *
 * @author phylock
 */
public class DeviceUpdateEvent {

  private final String responsible;
  protected final List<DeviceDescriptor> devices;
  protected final List<CategoryDescriptor> categories;
  protected final List<FunctionDescriptor> functions;
  protected final List<VariableDescriptor> variables;

  public DeviceUpdateEvent(String responsible, List<DeviceDescriptor> devices, List<CategoryDescriptor> categories, List<FunctionDescriptor> functions, List<VariableDescriptor> variable) {
    this.responsible = responsible;
    this.devices = devices;
    this.categories = categories;
    this.functions = functions;
    this.variables = variable;
  }

  public List<CategoryDescriptor> getCategories() {
    return categories;
  }

  public List<DeviceDescriptor> getDevices() {
    return devices;
  }

  public String getResponsible() {
    return responsible;
  }

  public boolean hasCategories() {
    return ((categories != null) && categories.size() > 0);
  }

  public boolean hasDevices() {
    return ((devices != null) && devices.size() > 0);
  }

  public List<FunctionDescriptor> getFunctions() {
    return functions;
  }

  public List<VariableDescriptor> getVariable() {
    return variables;
  }

  public boolean hasFunctions() {
    return ((functions != null) && functions.size() > 0);
  }

  public boolean hasVariables() {
    return ((variables != null) && variables.size() > 0);
  }
}

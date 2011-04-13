package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import java.util.LinkedList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author phylock
 */
public class StructureUpdate extends DeviceUpdateEvent {

  public StructureUpdate(String responsible) {
    super(responsible, new LinkedList<DeviceDescriptor>(), new LinkedList<CategoryDescriptor>(), new LinkedList<FunctionDescriptor>(), new LinkedList<VariableDescriptor>());
  }

  public void addFunction(FunctionDescriptor function) {
    functions.add(function);
  }

  public void addVariable(VariableDescriptor variable) {
    variables.add(variable);
  }

  public void addDevice(DeviceDescriptor device) {
    devices.add(device);
  }

  public void addCategory(CategoryDescriptor category) {
    categories.add(category);
  }
}

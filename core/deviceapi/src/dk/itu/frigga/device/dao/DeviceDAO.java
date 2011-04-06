/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;
import java.util.List;

/**
 *
 * @author phylock
 */
public interface DeviceDAO extends GenericDAO<Device, Long> {

  List<Device> findByCategory(Category category);
  
  Device findBySymbolic(String symbolic);

  void addToCategory(Device device, Category category);

  void removeFromCategory(Device device, Category category);

  boolean isOfCategory(Device device, Category category);

  List<Category> getCategories(Device device);

  List<Variable> getVariables(Device device);
  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.VariableType;
import java.util.List;

/**
 *
 * @author phylock
 */
public interface VariableTypeDao extends GenericDAO<VariableType, Long> {
  VariableType findByName(String name);

  List<VariableType> findByCategory(Category category);
  List<VariableType> findByDevice(Device device);

}

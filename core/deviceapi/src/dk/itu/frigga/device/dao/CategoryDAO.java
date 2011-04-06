/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Function;

/**
 *
 * @author phylock
 */
public interface CategoryDAO extends GenericDAO<Category, Long> {
  
  Category findByName(String name);

  void addFunction(Category category, Function function);

  void removeFunction(Device device, Category category);
 
}

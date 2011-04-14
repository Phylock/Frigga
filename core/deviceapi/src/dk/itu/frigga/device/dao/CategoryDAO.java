/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Function;
import dk.itu.frigga.device.model.VariableType;

/**
 *
 * @author phylock
 */
public interface CategoryDAO extends GenericDAO<Category, Long> {

  Category findByName(String name);

  void addFunction(Category category, Function function);

  void removeFunction(Category category, Function device);

  boolean hasFunction(Category category, Function function);

  void addVariableType(Category category, VariableType function);

  void removeVariableType(Category category, VariableType device);

  boolean hasVariableType(Category category, VariableType function);
}

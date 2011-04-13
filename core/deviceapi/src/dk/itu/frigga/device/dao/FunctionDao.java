/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Function;
import java.util.List;

/**
 *
 * @author phylock
 */
public interface FunctionDao extends GenericDAO<Function, Long> {

  List<Function> findByCategory(Category category);

  Function findByName(String name);

  boolean isInCategory(Function function, Category category);
}

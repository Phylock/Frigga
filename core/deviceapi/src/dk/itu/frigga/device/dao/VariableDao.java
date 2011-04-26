/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.dao;

import dk.itu.frigga.data.dao.GenericDAO;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;
import dk.itu.frigga.device.model.VariablePK;
import java.util.List;

/**
 *
 * @author phylock
 */
public interface VariableDao extends GenericDAO<Variable, VariablePK> {

  List<Variable> findByDevice(Device device);
  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.dao.FunctionDao;
import dk.itu.frigga.device.dao.LocationGlobalDao;
import dk.itu.frigga.device.dao.LocationLocalDao;
import dk.itu.frigga.device.dao.VariableDao;
import dk.itu.frigga.device.dao.VariableTypeDao;
import java.sql.Connection;

/**
 *
 * @author phylock
 */
public interface DeviceDaoFactory {

  CategoryDAO getCategoryDao(Connection conn);

  DeviceDAO getDeviceDao(Connection conn);

  FunctionDao getFunctionDao(Connection conn);

  VariableDao getVariableDao(Connection conn);

  VariableTypeDao getVariableTypeDao(Connection conn);

  LocationGlobalDao getLocationGlobalDao(Connection conn);
  
  LocationLocalDao getLocationLocalDao(Connection conn);
}

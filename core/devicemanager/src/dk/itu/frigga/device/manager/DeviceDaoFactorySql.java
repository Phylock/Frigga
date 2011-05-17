/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager;

import dk.itu.frigga.device.DeviceDaoFactory;
import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.dao.FunctionDao;
import dk.itu.frigga.device.dao.LocationGlobalDao;
import dk.itu.frigga.device.dao.LocationLocalDao;
import dk.itu.frigga.device.dao.VariableDao;
import dk.itu.frigga.device.dao.VariableTypeDao;
import dk.itu.frigga.device.manager.dao.CategoryDaoSql;
import dk.itu.frigga.device.manager.dao.DeviceDaoSql;
import dk.itu.frigga.device.manager.dao.FunctionDaoSql;
import dk.itu.frigga.device.manager.dao.LocationDaoSql;
import dk.itu.frigga.device.manager.dao.VariableDaoSql;
import dk.itu.frigga.device.manager.dao.VariableTypeDaoSql;
import java.sql.Connection;

/**
 *
 * @author phylock
 */
public class DeviceDaoFactorySql implements DeviceDaoFactory {
  private static final DeviceDaoFactory instance = new DeviceDaoFactorySql();

  private DeviceDaoFactorySql()
  {
    
  }

  public static DeviceDaoFactory instance()
  {
    return instance;
  }

  public CategoryDAO getCategoryDao(Connection conn) {
    CategoryDAO cat = new CategoryDaoSql();
    cat.setConnection(conn);
    return cat;
  }

  public DeviceDAO getDeviceDao(Connection conn) {
    DeviceDAO dev = new DeviceDaoSql();
    dev.setConnection(conn);
    return dev;
  }

  public FunctionDao getFunctionDao(Connection conn) {
    FunctionDao fun = new FunctionDaoSql();
    fun.setConnection(conn);
    return fun;
  }

  public VariableTypeDao getVariableTypeDao(Connection conn) {
    VariableTypeDao var = new VariableTypeDaoSql();
    var.setConnection(conn);
    return var;
  }

  public VariableDao getVariableDao(Connection conn) {
    VariableDao var = new VariableDaoSql();
    var.setConnection(conn);
    return var;
  }

  public LocationGlobalDao getLocationGlobalDao(Connection conn) {
    LocationGlobalDao loc = new LocationDaoSql();
    loc.setConnection(conn);
    return loc;
  }

  public LocationLocalDao getLocationLocalDao(Connection conn) {
    LocationLocalDao loc = null;//new LocationLocalDaoSql();
    loc.setConnection(conn);
    return loc;
  }
}

package dk.itu.frigga.device.manager;

import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.DeviceUpdate;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.FriggaDeviceException;
import dk.itu.frigga.device.LocationUpdate;
import dk.itu.frigga.device.VariableChangedEvent;
import dk.itu.frigga.device.VariableUpdate;
import dk.itu.frigga.device.dao.*;
import dk.itu.frigga.device.descriptor.*;
import dk.itu.frigga.device.model.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.service.log.LogService;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author phylock
 */
public class DeviceDatabase {

  private String groupname;
  private String filename;
  private static final String DRIVERNAME = "org.sqlite.JDBC";
  private DataManager datamanager;
  private LogService log;
  private ConnectionPool pool = null;

  public DeviceDatabase(String groupname, String filename) {
    this.groupname = groupname;
    this.filename = filename;
  }

  public ConnectionPool initialize() {
    Connection conn = null;
    try {
      if (!datamanager.hasConnection(groupname)) {
        datamanager.addConnection(groupname, new DataConnection("jdbc", "sqlite", filename, DRIVERNAME));
      }
      pool = datamanager.requestConnection(groupname);
      InputStream fs = DeviceDatabase.class.getResourceAsStream("devicetables.sql");
      conn = pool.getConnection();
      runStream(conn, fs, ";");

    } catch (SQLException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    } catch (DataGroupNotFoundException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnknownDataDriverException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      pool.releaseConnection(conn);
    }
    return pool;
  }

  private void runStream(Connection conn, InputStream data, String delimiter) {

    Scanner s = new Scanner(data);
    s.useDelimiter(delimiter);
    int stmnum = 0;
    String line = "";
    while (s.hasNext()) {
      try {
        Statement st = conn.createStatement();
        line = s.next();
        if (line.trim().length() > 0) {
          st.execute(line);
        }
      } catch (SQLException ex) {
        log.log(LogService.LOG_WARNING, "Error in line " + stmnum + ": " + line, ex);
      }
    }
  }

  public void update(VariableChangedEvent event) throws SQLException {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      if (event.hasVariables()) {
        VariableDao vdao = DeviceDaoFactorySql.instance().getVariableDao(conn);
        for (VariableUpdate v : event.getVariables()) {
          vdao.updateVariable(v.getDevice(), v.getVariable(), v.getValue());
        }
      }

      if (event.hasState()) {
        DeviceDAO ddao = DeviceDaoFactorySql.instance().getDeviceDao(conn);
        for (DeviceUpdate d : event.getState()) {
          ddao.setState(d.getDevice(), d.isOnline());
        }
      }

      if (event.hasLocation()) {
        LocationLocalDao ll = DeviceDaoFactorySql.instance().getLocationLocalDao(conn);
        LocationGlobalDao lg = DeviceDaoFactorySql.instance().getLocationGlobalDao(conn);
        updateDeviceLocation(lg, ll, event.getLocation());
      }

    } catch (FriggaDeviceException ex) {
      Logger.getLogger(DeviceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      pool.releaseConnection(conn);
    }
  }

  public void update(DeviceUpdateEvent event) throws SQLException {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO devicedao = DeviceDaoFactorySql.instance().getDeviceDao(conn);
      CategoryDAO categorydao = DeviceDaoFactorySql.instance().getCategoryDao(conn);
      FunctionDao functiondao = DeviceDaoFactorySql.instance().getFunctionDao(conn);
      VariableTypeDao vtypedao = DeviceDaoFactorySql.instance().getVariableTypeDao(conn);
      VariableDao vdao = DeviceDaoFactorySql.instance().getVariableDao(conn);

      if (event.hasFunctions()) {
        updateFunctions(functiondao, event.getFunctions());
      }
      if (event.hasVariables()) {
        updateVariables(vtypedao, event.getVariable());
      }
      if (event.hasCategories()) {
        updateCategory(categorydao, event.getCategories());
        updateCategoryLinks(categorydao, event.getCategories());
      }
      if (event.hasDevices()) {
        updateDevice(devicedao, event.getDevices(), event.getResponsible());
        updateDeviceCategory(devicedao, event.getDevices());
        updateDeviceVariables(devicedao, vtypedao, vdao, event.getDevices());
      }
    } finally {
      pool.releaseConnection(conn);
    }
  }

  private void updateDevice(DeviceDAO devicedao, List<DeviceDescriptor> devices, String driver) {
    for (DeviceDescriptor dd : devices) {
      Device device = new Device(dd.getName(), dd.getSymbolic(), new Date(), false, driver);
      devicedao.makePersistent(device);
    }
  }

  private void updateCategory(CategoryDAO categorydao, List<CategoryDescriptor> categories) {
    for (CategoryDescriptor cd : categories) {
      Category category = new Category(cd.getName());
      categorydao.makePersistent(category);
    }
  }

  private void updateFunctions(FunctionDao functiondao, List<FunctionDescriptor> functions) {
    for (FunctionDescriptor fd : functions) {
      Function function = new Function(fd.getName());
      functiondao.makePersistent(function);
    }
  }

  private void updateVariables(VariableTypeDao vtypedao, List<VariableDescriptor> variable) {
    for (VariableDescriptor vd : variable) {
      VariableType variabletype = new VariableType(vd.getName(), vd.getType());
      vtypedao.makePersistent(variabletype);
    }
  }

  private void updateDeviceCategory(DeviceDAO devicedao, List<DeviceDescriptor> devices) {

    //Categories <-> Devices
    for (DeviceDescriptor dd : devices) {
      Device device = devicedao.findBySymbolic(dd.getSymbolic());
      for (String cd : dd.getCategories()) {
        Category category = new Category(cd);
        devicedao.addToCategory(device, category);
      }
    }
  }

  private void updateCategoryLinks(CategoryDAO categorydao, List<CategoryDescriptor> categories) {
    for (CategoryDescriptor cd : categories) {
      Category category = categorydao.findByName(cd.getName());
      for (String fd : cd.getFunctions()) {
        Function function = new Function(fd);
        categorydao.addFunction(category, function);
      }
      for (String vd : cd.getVariables()) {
        VariableType variable = new VariableType(vd, null);
        categorydao.addVariableType(category, variable);
      }
    }
  }

  private void updateDeviceVariables(DeviceDAO devicedao, VariableTypeDao vtypedao, VariableDao variabledao, List<DeviceDescriptor> devices) {
    for (DeviceDescriptor dd : devices) {
      Device device = devicedao.findBySymbolic(dd.getSymbolic());
      List<VariableType> variables = vtypedao.findByDevice(device);
      for (VariableType vtype : variables) {
        Variable v = new Variable(new VariablePK(device, vtype), "");
        variabledao.makePersistent(v);
      }
    }
  }

  private void updateDeviceLocation(LocationGlobalDao global, LocationLocalDao local, List<LocationUpdate> updates) {
    for (LocationUpdate update : updates) {
      switch (update.getType()) {
        case Global:
          break;
        case Local:
          break;
      }
    }
  }

  public void close() {
  }
}

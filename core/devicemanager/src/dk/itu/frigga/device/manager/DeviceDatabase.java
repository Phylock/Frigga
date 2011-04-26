package dk.itu.frigga.device.manager;

import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.DeviceUpdateEvent;
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

  public void update(DeviceUpdateEvent event) throws SQLException {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      DeviceDAO devicedao = DaoFactory.getDeviceDao(conn);
      CategoryDAO categorydao = DaoFactory.getCategoryDao(conn);
      FunctionDao functiondao = DaoFactory.getFunctionDao(conn);
      VariableTypeDao vtypedao = DaoFactory.getVariableTypeDao(conn);

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
        updateDevice(devicedao, event.getDevices());
        updateDeviceCategory(devicedao, event.getDevices());
        updateDeviceVariables(devicedao, vtypedao, event.getDevices());
      }
    } finally {
      pool.releaseConnection(conn);
    }
  }

  private void updateDevice(DeviceDAO devicedao, List<DeviceDescriptor> devices) {
    for (DeviceDescriptor dd : devices) {
      Device device = new Device(dd.getName(), dd.getSymbolic(), new Date(), true);
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

  private void updateDeviceVariables(DeviceDAO devicedao, VariableTypeDao vtypedao, List<DeviceDescriptor> devices) {
    for (DeviceDescriptor dd : devices) {
      Device device = devicedao.findBySymbolic(dd.getSymbolic());
      List<VariableType> variables = vtypedao.findByDevice(device);
      for (VariableType vtype : variables) {
        devicedao.addVariable(device, vtype);
      }
    }
  }

  public void close() {
  }
}

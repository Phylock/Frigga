package dk.itu.frigga.device.manager;

import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.manager.dao.CategoryDaoSql;
import dk.itu.frigga.device.manager.dao.DeviceDaoSql;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
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
  private Connection conn;
  private DeviceDAO devicedao;
  private CategoryDAO categorydao;

  public DeviceDatabase(String groupname, String filename) {
    this.groupname = groupname;
    this.filename = filename;
  }

  public void initialize() {
    try {
      if (!datamanager.hasConnection(groupname)) {
        datamanager.addConnection(groupname, new DataConnection("jdbc", "sqlite", filename, DRIVERNAME));
      }
      conn = datamanager.requestConnection(groupname);
      InputStream fs = DeviceDatabase.class.getResourceAsStream("devicetables.sql");
      runStream(conn, fs, ";");

      devicedao = new DeviceDaoSql();
      devicedao.setConnection(conn);


      categorydao = new CategoryDaoSql();
      categorydao.setConnection(conn);
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    } catch (DataGroupNotFoundException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnknownDataDriverException ex) {
      Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public DeviceDAO getDeviceDao() {
    return devicedao;
  }

  public CategoryDAO getCategoryDao() {
    return categorydao;
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

  public void update(List<DeviceDescriptor> devices, List<CategoryDescriptor> categories) throws SQLException {

    updateCategory(categories);
    updateDevice(devices);

    //Categories <-> Devices
    if (devices != null) {
      for (DeviceDescriptor dd : devices) {
        Device device = devicedao.findBySymbolic(dd.getName());
        for (String cd : dd.getCategories()) {
          Category category = new Category(cd);
          devicedao.addToCategory(device, category);
        }
      }
    }
  }

  private void updateDevice(List<DeviceDescriptor> devices) {
    if (devices != null) {
      for (DeviceDescriptor dd : devices) {
        Device device = new Device(dd.getName(), dd.getSymbolic(), new Date(), true);
        devicedao.makePersistent(device);
      }
    }
  }

  private void updateCategory(List<CategoryDescriptor> categories) {
    if (categories != null) {
      for (CategoryDescriptor cd : categories) {
        Category category = new Category(cd.getName());
        categorydao.makePersistent(category);
      }
    }
  }
}

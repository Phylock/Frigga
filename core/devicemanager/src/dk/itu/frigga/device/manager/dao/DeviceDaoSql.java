/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class DeviceDaoSql extends GenericSqlDao<Device, Long> implements DeviceDAO {

  public static final String TABLE = "device";
  private PreparedStatement SELECT_BY_CATEGORY;
  private PreparedStatement SELECT_BY_SYMBOLIC;
  private PreparedStatement ADD_TO_CATEGORY;
  private PreparedStatement REMOVE_FROM_CATEGORY;
  private PreparedStatement IS_OF_CATEGORY;
  private PreparedStatement INSERT;
  private PreparedStatement UPDATE;

  @SuppressWarnings("unchecked")
  public List findByCategory(Category category) {
    List<Device> list = new ArrayList<Device>();
    if (category == null) {
      return list;
    }

    try {
      SELECT_BY_CATEGORY.setString(/*Category*/1, category.getName());
      ResultSet rs = SELECT_BY_CATEGORY.executeQuery();

      return parseAll(rs, list);
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<Device> findByExample(Device exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @SuppressWarnings("unchecked")
  public Device makePersistent(Device entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    if (entity.getName() == null) {
      throw new IllegalArgumentException("Category.Name can not be null");
    }
    try {
      //Insert, Update or Ignore
      Device device;
      if (entity.getId() != null) {
        device = findById(entity.getId(), false);
        if (device != null && !device.equals(entity)) {
          update = true;
        }
      } else {
        device = findBySymbolic(entity.getName());
      }

      //exists?
      if (device == null) {
        insert = true;
      }

      if (insert) {
        INSERT.setString(/*name*/1, entity.getName());
        INSERT.setString(/*symbolic*/2, entity.getSymbolic());
        INSERT.setString(/*last_update*/3, null);
        INSERT.setBoolean(/*online*/4, entity.isOnline());
        INSERT.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }

        device = findBySymbolic(entity.getName());
      } else if (update) {
        UPDATE.setLong(/*id*/5, entity.getId());
        UPDATE.setString(/*name*/1, entity.getName());
        UPDATE.setString(/*symbolic*/2, entity.getSymbolic());
        UPDATE.setString(/*last_update*/3, null);
        UPDATE.setBoolean(/*online*/4, entity.isOnline());
        UPDATE.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }

      return device;
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public void makeTransient(Device entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addToCategory(Device device, Category category) {
    try {
      if (!isOfCategory(device, category)) {
        ADD_TO_CATEGORY.setString(/*Symbolic*/1, device.getSymbolic());
        ADD_TO_CATEGORY.setString(/*Category Name*/2, category.getName());
        ADD_TO_CATEGORY.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @SuppressWarnings("unchecked")
  public void removeFromCategory(Device device, Category category) {
    try {
      if (isOfCategory(device, category)) {
        REMOVE_FROM_CATEGORY.setLong(1, device.getId());
        REMOVE_FROM_CATEGORY.setLong(2, category.getId());
        REMOVE_FROM_CATEGORY.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @SuppressWarnings("unchecked")
  public List<Variable> getVariables(Device device) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void prepareStatements() {
    try {
      SELECT_BY_CATEGORY = connection.prepareStatement("SELECT d.* FROM device as d, category as c, device_category as dc WHERE d.id = dc.device_id and dc.category_id = c.id and c.catname = ?");
      SELECT_BY_SYMBOLIC = connection.prepareStatement("SELECT * FROM device WHERE symbolic = ?");
      IS_OF_CATEGORY = connection.prepareStatement("SELECT d.* FROM device as d, category as c, device_category as dc WHERE d.id = dc.device_id and dc.category_id = c.id and c.catname = ? and d.devname = ?");
      ADD_TO_CATEGORY = connection.prepareStatement("INSERT INTO device_category (device_id, category_id) SELECT "
              + "device.id, category.id FROM device, category WHERE device.symbolic = ? and category.catname = ?");
      REMOVE_FROM_CATEGORY = connection.prepareStatement("DELETE FROM device_category "
              + "WHERE device_id=? AND category_id=?");

      INSERT = connection.prepareStatement("INSERT INTO device(devname, symbolic, last_update, online) VALUES(?,?,?,?)");
      UPDATE = connection.prepareStatement("UPDATE device SET devname=?, symbolic = ?, last_update  = ?, online = ? WHERE ID = ?");
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected Device parseCurrent(ResultSet rs) throws SQLException {
    Long id = rs.getLong("id");
    String name = rs.getString("devname");
    String symbolic = rs.getString("symbolic");
    Date last = rs.getDate("last_update");
    boolean online = rs.getBoolean("online");

    Device d = new Device(name, symbolic, last, online);
    d.setId(id);
    return d;
  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  public boolean isOfCategory(Device device, Category category) {
    try {
      IS_OF_CATEGORY.setString(2, device.getSymbolic());
      IS_OF_CATEGORY.setString(1, category.getName());
      ResultSet exists = IS_OF_CATEGORY.executeQuery();
      return exists.next();
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  public List<Category> getCategories(Device device) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Device findBySymbolic(String symbolic) {
    try {
      SELECT_BY_SYMBOLIC.setString(/*Symbolic*/1, symbolic);
      ResultSet rs = SELECT_BY_SYMBOLIC.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}

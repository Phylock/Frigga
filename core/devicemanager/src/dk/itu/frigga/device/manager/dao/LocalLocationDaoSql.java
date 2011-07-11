/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.LocationLocalDao;
import dk.itu.frigga.device.model.LocationLocal;
import dk.itu.frigga.device.model.Point3;
import dk.itu.frigga.utility.StringHelper;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class LocalLocationDaoSql extends GenericSqlDao<LocationLocal, Long> implements LocationLocalDao {

  private static final String ID = "device_id";
  private static final String TABLE = "position_local";
  private static final String[] FIELDS_ALL = new String[]{"device_id", "pos_x", "pos_y", "pos_z", "vel_x", "vel_y", "vel_z", "updated", "sender", "room"};
  private static final String[] FIELDS = new String[]{"pos_x", "pos_y", "pos_z", "vel_x", "vel_y", "vel_z", "updated", "sender", "room",};
  private final PreparedStatementProxy FIND_BY_DEVICE;

  public LocalLocationDaoSql() {
    SELECT_ALL = new PreparedStatementProxy("SELECT l.*, d.symbolic as device FROM position_local as l, device as d");
    SELECT_BY_ID = new PreparedStatementProxy("SELECT l.*, d.symbolic as device FROM position_local as l, device as d WHERE %s = ?", ID);
    FIND_BY_DEVICE = new PreparedStatementProxy("SELECT l.*, d.symbolic as device FROM device as d, position_local as l WHERE d.symbolic = ? AND l.device_id = d.id");
    INSERT = new PreparedStatementProxy(
            "INSERT INTO %s(%s) VALUES(%s, %s)", TABLE,
            StringHelper.implodeString(FIELDS_ALL, ","), "(select d.id from device as d where d.symbolic = ?)",
            StringHelper.repeatImplodeString("?", FIELDS.length, ","));


    String set_string = StringHelper.implodeString(FIELDS, SET_EXTENSION + ",") + SET_EXTENSION;
    UPDATE = new PreparedStatementProxy("UPDATE %s SET %s WHERE device_id = (select id from device where symbolic = ?)", TABLE, set_string);
  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  @Override
  protected String[] getFields() {
    return FIELDS_ALL;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  @Override
  protected LocationLocal parseCurrent(ResultSet rs) throws SQLException {
    Long id = rs.getLong(ID);

    double px = rs.getDouble("pos_x");
    double py = rs.getDouble("pos_y");
    double pz = rs.getDouble("pos_z");

    double vx = rs.getDouble("vel_x");
    double vy = rs.getDouble("vel_y");
    double vz = rs.getDouble("vel_z");

    Date updated = new Date(rs.getDate("updated").getTime());
    String sender = rs.getString("sender");
    String room = rs.getString("room");
    String device = rs.getString("device");

    return new LocationLocal(id, device, new Point3(px, py, pz), new Point3(vx, vy, vz), sender, updated, room);
  }

  public List<LocationLocal> findByExample(LocationLocal exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public LocationLocal makePersistent(LocationLocal entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    if (entity.getDevice() == null) {
      throw new IllegalArgumentException("device can not be null");
    }
    try {
      //Insert, Update or Ignore
      LocationLocal location = findByDevice(entity.getDevice());

      //exists?
      if (location == null) {
        insert = true;
      } else if (!location.equals(entity)) {
        update = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
        stmt_insert.setString(/*device_id*/1, entity.getDevice());
        stmt_insert.setDouble(/*pos_x*/2, entity.getPosition().getX());
        stmt_insert.setDouble(/*pos_y*/3, entity.getPosition().getY());
        stmt_insert.setDouble(/*pos_z*/4, entity.getPosition().getZ());
        if (entity.getVelocity() != null) {
          stmt_insert.setDouble(/*vel_x*/5, entity.getVelocity().getX());
          stmt_insert.setDouble(/*vel_y*/6, entity.getVelocity().getY());
          stmt_insert.setDouble(/*vel_z*/7, entity.getVelocity().getZ());
        } else {
          stmt_insert.setDouble(/*vel_x*/5, 0);
          stmt_insert.setDouble(/*vel_y*/6, 0);
          stmt_insert.setDouble(/*vel_z*/7, 0);
        }
        stmt_insert.setDate(/*updated*/8, new Date(entity.getUpdated().getTime()));
        stmt_insert.setString(/*sender*/9, entity.getSender());
        stmt_insert.setString(/*room*/10, entity.getRoom());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }

        location = findByDevice(entity.getDevice());
      } else if (update) {
        PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
        stmt_update.setString(/*id*/10, location.getDevice());
        stmt_update.setDouble(/*pos_x*/1, entity.getPosition().getX());
        stmt_update.setDouble(/*pos_y*/2, entity.getPosition().getY());
        stmt_update.setDouble(/*pos_z*/3, entity.getPosition().getZ());

        if (entity.getVelocity() != null) {
          stmt_update.setDouble(/*vel_x*/4, entity.getVelocity().getX());
          stmt_update.setDouble(/*vel_y*/5, entity.getVelocity().getY());
          stmt_update.setDouble(/*vel_z*/6, entity.getVelocity().getZ());
        } else {
          stmt_update.setDouble(/*vel_x*/4, 0);
          stmt_update.setDouble(/*vel_y*/5, 0);
          stmt_update.setDouble(/*vel_z*/6, 0);
        }
        stmt_update.setDate(/*updated*/7, new Date(entity.getUpdated().getTime()));
        stmt_update.setString(/*sender*/8, entity.getSender());
        stmt_update.setString(/*room*/9, entity.getRoom());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }

      return location;
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public void makeTransient(LocationLocal entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public LocationLocal findByDevice(String symbolic) {
    LocationLocal location = null;
    ResultSet rs = null;
    try {
      PreparedStatement stmt_select = FIND_BY_DEVICE.createPreparedStatement(connection);
      stmt_select.setString(/*Symbolic*/1, symbolic);
      rs = stmt_select.executeQuery();
      if (rs.next()) {
        location = parseCurrent(rs);
      }
      rs.close();
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } catch (SQLException ex) {
        // It's dead.
      }
    }
    return location;
  }
}

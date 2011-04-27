/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.FriggaDeviceException;
import dk.itu.frigga.device.dao.VariableDao;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;
import dk.itu.frigga.device.model.VariablePK;
import dk.itu.frigga.device.model.VariableType;
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
public class VariableDaoSql extends GenericSqlDao<Variable, VariablePK> implements VariableDao {

  private static final String TABLE = "device_variable";
  private static final String[] FIELDS = new String[]{"device_id", "variable_id", "variable_value"};
  private static final String ID = "device_id = ? and variable_id"; // I know its a little cheat, ill fix in a future version
  private final PreparedStatementProxy STRING_UPDATE;
  private final PreparedStatementProxy STRING_INSERT;
  private final PreparedStatementProxy SELECT_BY_STRING;

  public VariableDaoSql() {
    STRING_UPDATE = new PreparedStatementProxy("UPDATE device_variable SET variable_value=? WHERE "
            + "device_id = (SELECT id FROM device WHERE device.symbolic = ?) AND "
            + "variable_id = (SELECT id FROM variabletype WHERE variabletype.varname = ?)", TABLE);
    STRING_INSERT = new PreparedStatementProxy("INSERT INTO %s (device_id, variable_id, variable_value) SELECT "
            + "device.id, variabletype.id, ? FROM variabletype, device WHERE device.symbolic = ? AND variabletype.varname = ?", TABLE);
    SELECT_BY_STRING = new PreparedStatementProxy("SELECT %s.* FROM %s, device, variabletype WHERE "
            + "device.id = device_id AND variabletype.id = variable_id AND device.symbolic = ? AND variabletype.varname = ?", TABLE, TABLE);
  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  @Override
  protected Variable parseCurrent(ResultSet rs) throws SQLException {
    Long device_id = rs.getLong("device_id");
    Long variable_id = rs.getLong("variable_id");
    String value = rs.getString("variable_value");

    Variable v = new Variable(new VariablePK(new Device(device_id), new VariableType(variable_id)), value);
    return v;
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  @Override
  public Variable findById(VariablePK id, boolean lock) {
    Variable obj = null;
    try {
      if (id.getDevice().getId() != null && id.getVariabletype().getId() != null) {
        PreparedStatement stmt = SELECT_BY_ID.createPreparedStatement(connection);
        stmt.setLong(1, id.getDevice().getId());
        stmt.setLong(2, id.getVariabletype().getId());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          obj = parseCurrent(rs);
        }
        rs.close();
      } else {
        PreparedStatement stmt = SELECT_BY_STRING.createPreparedStatement(connection);
        stmt.setString(1, id.getDevice().getSymbolic());
        stmt.setString(2, id.getVariabletype().getName());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          obj = parseCurrent(rs);
        }
        rs.close();
      }
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return obj;
  }

  public List<Variable> findByExample(Variable exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Variable makePersistent(Variable entity) {
    if (entity.getPrimaryKey() == null) {
      throw new IllegalArgumentException("Primary Key can not be null");
    }

    if (entity.getPrimaryKey().getDevice().getId() != null && entity.getPrimaryKey().getVariabletype().getId() != null) {
      return makePersistentID(entity);
    } else {
      return makePersistentString(entity);
    }
  }

  private Variable makePersistentString(Variable entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    Variable variable = null;
    try {

      //Insert, Update or Ignore
      variable = findById(entity.getPrimaryKey(), false);
      if (variable != null && !variable.equals(entity)) {
        update = true;
      } //exists?
      if (variable == null) {
        insert = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = STRING_INSERT.createPreparedStatement(connection);
        stmt_insert.setString(2, /*device_id*/ entity.getPrimaryKey().getDevice().getSymbolic());
        stmt_insert.setString(3, /*variabletype_id*/ entity.getPrimaryKey().getVariabletype().getName());
        stmt_insert.setString(1, /*value*/ entity.getValue());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        variable = entity;
      } else if (update) {
        PreparedStatement stmt_update = STRING_UPDATE.createPreparedStatement(connection);
        stmt_update.setString(2, /*device_id*/ entity.getPrimaryKey().getDevice().getSymbolic());
        stmt_update.setString(3, /*variabletype_id*/ entity.getPrimaryKey().getVariabletype().getName());
        stmt_update.setString(/*value*/1, entity.getValue());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return variable;
  }

  private Variable makePersistentID(Variable entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    Variable variable = null;
    try {

      //Insert, Update or Ignore
      variable = findById(entity.getPrimaryKey(), false);
      if (variable != null && !variable.equals(entity)) {
        update = true;
      } //exists?
      if (variable == null) {
        insert = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
        stmt_insert.setLong(1, /*device_id*/ entity.getPrimaryKey().getDevice().getId());
        stmt_insert.setLong(2, /*variabletype_id*/ entity.getPrimaryKey().getVariabletype().getId());
        stmt_insert.setString(3, /*value*/ entity.getValue());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        variable = entity;
      } else if (update) {
        PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
        stmt_update.setLong(2, /*device_id*/ entity.getPrimaryKey().getDevice().getId());
        stmt_update.setLong(3, /*variabletype_id*/ entity.getPrimaryKey().getVariabletype().getId());
        stmt_update.setString(/*value*/1, entity.getValue());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }

      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return variable;
  }

  public void makeTransient(Variable entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List<Variable> findByDevice(Device device) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void updateVariable(String symbolic, String variable, String value) throws FriggaDeviceException {
    try {
      PreparedStatement stmt_update = STRING_UPDATE.createPreparedStatement(connection);
      stmt_update.setString(2, symbolic);
      stmt_update.setString(3, variable);
      stmt_update.setString(1, value);
      stmt_update.executeUpdate();
      if (!connection.getAutoCommit()) {
        connection.commit();
      }
    } catch (SQLException ex) {
      throw new FriggaDeviceException(ex);
    }
  }
}

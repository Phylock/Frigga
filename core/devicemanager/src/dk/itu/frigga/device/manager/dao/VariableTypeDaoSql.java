/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.VariableTypeDao;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.VariableType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class VariableTypeDaoSql extends GenericSqlDao<VariableType, Long> implements VariableTypeDao {

  private static final String ID = "id";
  private static final String TABLE = "variabletype";
  private static final int FIELD_NAME = 0;
  private static final int FIELD_TYPE = 1;
  private static final String[] FIELDS = new String[]{"varname", "vartype"};
  private PreparedStatementProxy SELECT_BY_NAME;
  private PreparedStatementProxy SELECT_BY_DEVICE;
  private PreparedStatementProxy SELECT_BY_CATEGORY;

  public VariableTypeDaoSql() {
          SELECT_BY_NAME = new PreparedStatementProxy("SELECT * FROM %s WHERE %s = ?", TABLE, FIELDS[FIELD_NAME]);
      SELECT_BY_CATEGORY = new PreparedStatementProxy(
              "SELECT vt.* FROM "
              + "variabletype as vt, category_variable as cv, category as c "
              + "WHERE "
              + "vt.id = cv.variable_id AND cv.category_id = c.id AND c.catname = ?");
      SELECT_BY_DEVICE = new PreparedStatementProxy(
              "SELECT DISTINCT vt.* FROM "
              + "variabletype as vt, category_variable as cv, category as c, device_category as dc, device d "
              + "WHERE "
              + "vt.id = cv.variable_id AND cv.category_id = c.id AND dc.category_id AND dc.device_id = d.id AND d.symbolic = ?");




  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  public VariableType makePersistent(VariableType entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    if (entity.getName() == null) {
      throw new IllegalArgumentException("VariableType.Name can not be null");
    }
    try {
      //Insert, Update or Ignore
      VariableType vtype;
      if (entity.getId() != null) {
        vtype = findById(entity.getId(), false);
        if (vtype != null && !vtype.equals(entity)) {
          update = true;
        }
      } else {
        vtype = findByName(entity.getName());
      }

      //exists?
      if (vtype == null) {
        insert = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
        stmt_insert.setString(1, /*name*/ entity.getName());
        stmt_insert.setString(2, /*type*/ entity.getType());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        vtype = findByName(entity.getName());
      } else if (update) {
        PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
        stmt_update.setLong(/*id*/3, entity.getId());
        stmt_update.setString(/*name*/1, entity.getName());
        stmt_update.setString(/*type*/2, entity.getType());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }

      return vtype;
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  protected VariableType parseCurrent(ResultSet rs) throws SQLException {
    String name = rs.getString(FIELDS[FIELD_NAME]);
    String type = rs.getString(FIELDS[FIELD_TYPE]);
    Long id = rs.getLong(ID);

    VariableType vt = new VariableType(name, type);
    vt.setId(id);
    return vt;
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  public List<VariableType> findByExample(VariableType exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void makeTransient(VariableType entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public VariableType findByName(String name) {
    try {
      PreparedStatement stmt_select = SELECT_BY_NAME.createPreparedStatement(connection);
      stmt_select.setString(/*name*/1, name);
      ResultSet rs = stmt_select.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public List<VariableType> findByCategory(Category category) {

    List<VariableType> list = new ArrayList<VariableType>();
    try {
      PreparedStatement stmt_select = SELECT_BY_CATEGORY.createPreparedStatement(connection);
      stmt_select.setString(/*catname*/1, category.getName());
      ResultSet rs = stmt_select.executeQuery();
      return parseAll(rs, list);
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;

  }

  public List<VariableType> findByDevice(Device device) {
    List<VariableType> list = new ArrayList<VariableType>();
    try {
      PreparedStatement stmt_select = SELECT_BY_DEVICE.createPreparedStatement(connection);
      stmt_select.setString(/*symbolic*/1, device.getSymbolic());
      ResultSet rs = stmt_select.executeQuery();
      return parseAll(rs, list);
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.FunctionDao;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Function;
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
public class FunctionDaoSql extends GenericSqlDao<Function, Long> implements FunctionDao {

  private static final String ID = "id";
  private static final String TABLE = "functions";
  private static final int FIELD_NAME = 0;
  private static final String[] FIELDS = new String[]{"name"};
  private final PreparedStatementProxy SELECT_BY_NAME;

  public FunctionDaoSql() {
    SELECT_BY_NAME = new PreparedStatementProxy("SELECT * FROM %s WHERE %s = ?", TABLE, FIELDS[FIELD_NAME]);
  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  public List<Function> findByExample(Function exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void makeTransient(Function entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List<Function> findByCategory(Category category) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Function makePersistent(Function entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    if (entity.getName() == null) {
      throw new IllegalArgumentException("Category.Name can not be null");
    }
    try {
      //Insert, Update or Ignore
      Function function;
      if (entity.getId() != null) {
        function = findById(entity.getId(), false);
        if (function != null && !function.equals(entity)) {
          update = true;
        }
      } else {
        function = findByName(entity.getName());
      }

      //exists?
      if (function == null) {
        insert = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
        stmt_insert.setString(1, /*name*/ entity.getName());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        function = findByName(entity.getName());
      } else if (update) {
        PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
        stmt_update.setLong(/*id*/2, entity.getId());
        stmt_update.setString(/*Name*/1, entity.getName());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }

      return function;
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public boolean isInCategory(Function function, Category category) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected Function parseCurrent(ResultSet rs) throws SQLException {
    String name = rs.getString(FIELDS[FIELD_NAME]);
    Long id = rs.getLong(ID);

    Function f = new Function(name);
    f.setId(id);
    return f;
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  public Function findByName(String name) {
    try {
      PreparedStatement select = SELECT_BY_NAME.createPreparedStatement(connection);
      select.setString(/*name*/1, name);
      ResultSet rs = select.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}

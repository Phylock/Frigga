/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

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
  private PreparedStatement SELECT_BY_NAME;

  @Override
  protected void prepareStatements() {
    try {
      SELECT_BY_NAME = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELDS[FIELD_NAME]));
    } catch (SQLException ex) {
      Logger.getLogger(FunctionDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
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
        INSERT.setString(1, /*name*/entity.getName());
        INSERT.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        function = findByName(entity.getName());
      } else if (update) {
        UPDATE.setLong(/*id*/2, entity.getId());
        UPDATE.setString(/*Name*/1, entity.getName());
        UPDATE.executeUpdate();
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
      SELECT_BY_NAME.setString(/*name*/1, name);
      ResultSet rs = SELECT_BY_NAME.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}

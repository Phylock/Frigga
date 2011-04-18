/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Function;
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
public class CategoryDaoSql extends GenericSqlDao<Category, Long> implements CategoryDAO {

  private static final String ID = "id";
  private static final String[] FIELDS = new String[]{"catname"};
  public static final String TABLE = "category";
  private PreparedStatementProxy SELECT_BY_NAME;
  private PreparedStatementProxy HAS_FUNCTION;
  private PreparedStatementProxy ADD_FUNCTION;
  private PreparedStatementProxy REMOVE_FUNCTION;
  private PreparedStatementProxy HAS_VARIABLETYPE;
  private PreparedStatementProxy ADD_VARIABLETYPE;
  private PreparedStatementProxy REMOVE_VARIABLETYPE;

  public CategoryDaoSql() {
    SELECT_BY_NAME = new PreparedStatementProxy("SELECT * FROM category WHERE catname = ?");
      HAS_FUNCTION = new PreparedStatementProxy("SELECT count(c.id) FROM functions as f, category as c, category_function as cf WHERE f.id = cf.function_id and cf.category_id = c.id and c.catname = ? and f.name = ?");
      ADD_FUNCTION = new PreparedStatementProxy("INSERT INTO category_function (category_id, function_id) SELECT "
              + "category.id, functions.id FROM functions, category WHERE category.catname = ? and functions.name = ?");
      REMOVE_FUNCTION = new PreparedStatementProxy("DELETE FROM category_function "
              + "WHERE category_id=? AND function_id=?");
      HAS_VARIABLETYPE = new PreparedStatementProxy("SELECT count(c.id) FROM variabletype as v, category as c, category_variable as cv WHERE v.id = cv.variable_id and cv.category_id = c.id and c.catname = ? and v.varname = ?");
      ADD_VARIABLETYPE = new PreparedStatementProxy("INSERT INTO category_variable (category_id, variable_id) SELECT "
              + "category.id, variabletype.id FROM variabletype, category WHERE category.catname = ? and variabletype.varname = ?");
      REMOVE_VARIABLETYPE = new PreparedStatementProxy("DELETE FROM category_variable "
              + "WHERE category_id=? AND variable_id=?");
  }

  protected Category parseCurrent(ResultSet rs) throws SQLException {
    Long id = rs.getLong("id");
    String name = rs.getString("catname");

    Category category = new Category(name);
    category.setId(id);
    return category;
  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  public List<Category> findByExample(Category exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Category makePersistent(Category entity) {
    boolean insert = false;
    boolean update = false;
    //Is required values defined
    if (entity.getName() == null) {
      throw new IllegalArgumentException("Category.Name can not be null");
    }
    try {
      //Insert, Update or Ignore
      Category category;
      if (entity.getId() != null) {
        category = findById(entity.getId(), false);
        if (category != null && !category.equals(entity)) {
          update = true;
        }
      } else {
        category = findByName(entity.getName());
      }

      //exists?
      if (category == null) {
        insert = true;
      }

      if (insert) {
        PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
        stmt_insert.setString(1, entity.getName());
        stmt_insert.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        category = findByName(entity.getName());
      } else if (update) {
        PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
        stmt_update.setLong(/*id*/2, entity.getId());
        stmt_update.setString(/*Name*/1, entity.getName());
        stmt_update.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
      }

      return category;
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public void makeTransient(Category entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addFunction(Category category, Function function) {
    try {
      if (!hasFunction(category, function)) {
        PreparedStatement stmt_add = ADD_FUNCTION.createPreparedStatement(connection);
        stmt_add.setString(/*category name*/1, category.getName());
        stmt_add.setString(/*function name*/2, function.getName());
        stmt_add.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void removeFunction(Category category, Function function) {
    try {
      if (hasFunction(category, function)) {
        PreparedStatement stmt_remove = REMOVE_FUNCTION.createPreparedStatement(connection);
        stmt_remove.setLong(1, category.getId());
        stmt_remove.setLong(2, function.getId());
        stmt_remove.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Category findByName(String name) {
    try {
      PreparedStatement stmt_select = SELECT_BY_NAME.createPreparedStatement(connection);
      stmt_select.setString(/*Symbolic*/1, name);
      ResultSet rs = stmt_select.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  public boolean hasFunction(Category category, Function function) {
    try {
      PreparedStatement stmt_select = HAS_FUNCTION.createPreparedStatement(connection);
      stmt_select.setString(1, category.getName());
      stmt_select.setString(2, function.getName());
      ResultSet exists = stmt_select.executeQuery();
      return exists.next();
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  public void addVariableType(Category category, VariableType vtype) {
    try {
      if (!hasVariableType(category, vtype)) {
        PreparedStatement stmt_add = ADD_VARIABLETYPE.createPreparedStatement(connection);
        stmt_add.setString(/*category name*/1, category.getName());
        stmt_add.setString(/*variable name*/2, vtype.getName());
        stmt_add.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void removeVariableType(Category category, VariableType vtype) {
    try {
      if (hasVariableType(category, vtype)) {
        PreparedStatement stmt_remove = REMOVE_VARIABLETYPE.createPreparedStatement(connection);
        stmt_remove.setLong(1, category.getId());
        stmt_remove.setLong(2, vtype.getId());
        stmt_remove.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean hasVariableType(Category category, VariableType vtype) {
    try {
      PreparedStatement stmt_select = HAS_VARIABLETYPE.createPreparedStatement(connection);
      stmt_select.setString(1, category.getName());
      stmt_select.setString(2, vtype.getName());
      ResultSet exists = stmt_select.executeQuery();
      return exists.next();
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }
}

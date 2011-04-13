/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
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
public class CategoryDaoSql extends GenericSqlDao<Category, Long> implements CategoryDAO {

  private static final String ID = "id";
  private static final String[] FIELDS = new String[]{"catname"};
  public static final String TABLE = "category";
  private PreparedStatement SELECT_BY_NAME;
  private PreparedStatement HAS_FUNCTION;
  private PreparedStatement ADD_FUNCTION;
  private PreparedStatement REMOVE_FUNCTION;

  @Override
  protected void prepareStatements() {
    try {
      SELECT_BY_NAME = connection.prepareStatement("SELECT * FROM category WHERE catname = ?");
      HAS_FUNCTION = connection.prepareStatement("SELECT c.* FROM functions as f, category as c, category_function as cf WHERE f.id = cf.function_id and cf.category_id = c.id and c.catname = ? and f.name = ?");
      ADD_FUNCTION = connection.prepareStatement("INSERT INTO category_function (category_id, function_id) SELECT "
              + "category.id, functions.id FROM functions, category WHERE category.catname = ? and functions.name = ?");
      REMOVE_FUNCTION = connection.prepareStatement("DELETE FROM category_function "
              + "WHERE category_id=? AND function_id=?");

    } catch (SQLException ex) {
      Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
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
        INSERT.setString(1, entity.getName());
        INSERT.executeUpdate();
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        category = findByName(entity.getName());
      } else if (update) {
        UPDATE.setLong(/*id*/2, entity.getId());
        UPDATE.setString(/*Name*/1, entity.getName());
        UPDATE.executeUpdate();
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
        ADD_FUNCTION.setString(/*category name*/1, category.getName());
        ADD_FUNCTION.setString(/*function name*/2, function.getName());
        ADD_FUNCTION.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void removeFunction(Category category, Function function) {
    try {
      if (hasFunction(category, function)) {
        REMOVE_FUNCTION.setLong(1, category.getId());
        REMOVE_FUNCTION.setLong(2, function.getId());
        REMOVE_FUNCTION.executeUpdate();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Category findByName(String name) {
    try {
      SELECT_BY_NAME.setString(/*Symbolic*/1, name);
      ResultSet rs = SELECT_BY_NAME.executeQuery();
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
      HAS_FUNCTION.setString(1, category.getName());
      HAS_FUNCTION.setString(2, function.getName());
      ResultSet exists = HAS_FUNCTION.executeQuery();
      return exists.next();
    } catch (SQLException ex) {
      Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }
}

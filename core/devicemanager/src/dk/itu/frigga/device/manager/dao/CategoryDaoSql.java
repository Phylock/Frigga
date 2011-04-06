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
public class CategoryDaoSql extends GenericSqlDao<Category, Long> implements CategoryDAO {

  public static final String TABLE = "category";
  private PreparedStatement SELECT_BY_NAME;
  private PreparedStatement UPDATE;
  private PreparedStatement INSERT;
  private PreparedStatement ADD_FUNCTION;
  private PreparedStatement REMOVE_FUNCTION;

  @Override
  protected void prepareStatements() {
    try {
      SELECT_BY_NAME = connection.prepareStatement("SELECT * FROM category WHERE catname = ?");
      INSERT = connection.prepareStatement("INSERT INTO category(catname) VALUES(?)");
      UPDATE = connection.prepareStatement("UPDATE category SET catname=? WHERE ID = ?");
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void removeFunction(Device device, Category category) {
    throw new UnsupportedOperationException("Not supported yet.");
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
}

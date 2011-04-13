/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.data.dao;

import dk.itu.frigga.utility.StringHelper;
import java.io.Serializable;
import java.sql.Connection;
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
public abstract class GenericSqlDao<T, ID extends Serializable> implements GenericDAO<T, ID> {

  private static final String SET_EXTENSION = " = ? ";
  protected Connection connection;
  private PreparedStatement SELECT_ALL;
  private PreparedStatement SELECT_BY_ID;
  protected PreparedStatement INSERT;
  protected PreparedStatement UPDATE;

  public void setConnection(Connection conn) {
    this.connection = conn;
    String table = getTable();
    String id = getIdField();
    String[] fields = getFields();

    String set_string = StringHelper.implodeString(fields, SET_EXTENSION + ",") + SET_EXTENSION;

    try {
      SELECT_ALL = connection.prepareStatement(String.format("SELECT * FROM %s", table));
      SELECT_BY_ID = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, id));
      INSERT = connection.prepareStatement(
              String.format("INSERT INTO %s(%s) VALUES(%s)", table,
              StringHelper.implodeString(fields, ","),
              StringHelper.repeatImplodeString("?", fields.length, ",")));
      UPDATE = connection.prepareStatement(String.format("UPDATE %s SET %s WHERE %s = ?", table, set_string, id));
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }

    prepareStatements();
  }

  public T findById(ID id, boolean lock) {
    try {
      SELECT_BY_ID.setObject(1, id);
      ResultSet rs = SELECT_BY_ID.executeQuery();
      if (rs.next()) {
        return parseCurrent(rs);
      }
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public List<T> findAll() {
    List<T> list = new ArrayList<T>();
    try {
      ResultSet rs = SELECT_ALL.executeQuery();
      return parseAll(rs, list);
    } catch (SQLException ex) {
      Logger.getLogger(GenericSqlDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
  }

  protected abstract void prepareStatements();

  protected abstract String getTable();

  protected abstract T parseCurrent(ResultSet rs) throws SQLException;

  protected abstract String[] getFields();

  protected abstract String getIdField();

  /** Helper Classes **/
  protected List<T> parseAll(ResultSet rs, List<T> list) throws SQLException {
    while (rs.next()) {
      list.add(parseCurrent(rs));
    }
    return list;
  }
}

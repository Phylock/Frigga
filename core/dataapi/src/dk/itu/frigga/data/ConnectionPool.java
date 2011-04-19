/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author phylock
 */
public interface ConnectionPool {
  int freeConnections();
  int totalConnections();

  boolean isInitialized();
  void initialize() throws SQLException;

  Connection getConnection() throws SQLException;
  Connection getConnection(long timeout) throws SQLException, TimeoutException;

  void releaseConnection(Connection connection);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author phylock
 */
public class PreparedStatementProxy {
  private String statement;

  public PreparedStatementProxy(String statement, Object ... args) {
    this.statement = String.format(statement, args);
  }

  public PreparedStatementProxy(String statement) {
    this.statement = statement;
  }


  public PreparedStatement createPreparedStatement(Connection connection)
  {
    if(connection != null)
    {
      try {
        return connection.prepareStatement(statement);
      } catch (SQLException ex) {
        return null;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "PreparedStatementProxy{" + "statement=" + statement + '}';
  }


}

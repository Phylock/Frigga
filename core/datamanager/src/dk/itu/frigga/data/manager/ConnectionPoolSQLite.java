package dk.itu.frigga.data.manager;

import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.TimeoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class ConnectionPoolSQLite implements ConnectionPool {

  private PoolQueue<Connection> pool;
  private boolean initialized = false;
  private final DataConnection data;
  private static final int size = 1;

  public ConnectionPoolSQLite(DataConnection data) {
    this.data = data;
  }

  public int freeConnections() {
    return pool.poolSize();
  }

  public int totalConnections() {
    return size;
  }

  public Connection getConnection() throws SQLException {
    while (true) {
      try {
        return pool.request();
      } catch (InterruptedException ex) {
        Logger.getLogger(ConnectionPoolSQLite.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public Connection getConnection(long timeout) throws SQLException, TimeoutException {
    try {
      return pool.request(timeout);
    } catch (InterruptedException ex) {
      throw new TimeoutException();
    }
  }

  public boolean isInitialized() {
    return initialized;
  }

  public void initialize() throws SQLException {
    Connection conn = DriverManager.getConnection(data.getConnectionUrl());
    List<Connection> list = new LinkedList<Connection>();
    list.add(conn);
    pool = new PoolQueue<Connection>(list);
    initialized = true;
  }

  public void releaseConnection(Connection connection) {
    try {
      pool.release(connection);
    } catch (InterruptedException ex) {
      Logger.getLogger(ConnectionPoolSQLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

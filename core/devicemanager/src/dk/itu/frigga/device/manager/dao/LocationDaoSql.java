/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.dao.LocationGlobalDao;
import dk.itu.frigga.device.model.Location;
import dk.itu.frigga.device.model.Point3;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author phylock
 */
public class LocationDaoSql extends GenericSqlDao<Location, Long> implements LocationGlobalDao {

  private static final String ID = "device_id";
  private static final String TABLE = "position_global";
  private static final String[] FIELDS = new String[]{"pos_x","pos_y","pos_z","vel_x","vel_y","vel_z","updated"};

  public LocationDaoSql() {

  }

  @Override
  protected String getTable() {
    return TABLE;
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  @Override
  protected String getIdField() {
    return ID;
  }

  @Override
  protected Location parseCurrent(ResultSet rs) throws SQLException {
    Long id = rs.getLong(ID);

    double px = rs.getDouble("pos_x");
    double py = rs.getDouble("pos_y");
    double pz = rs.getDouble("pos_z");

    double vx = rs.getDouble("vel_x");
    double vy = rs.getDouble("vel_y");
    double vz = rs.getDouble("vel_z");

    Date updated = new Date(rs.getDate("updated").getTime());
    String sender = rs.getString("sender");

    return new Location(id, new Point3(px,py,pz), new Point3(vx,vy,vz), sender, updated);
  }

  public List<Location> findByExample(Location exampleInstance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Location makePersistent(Location entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void makeTransient(Location entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

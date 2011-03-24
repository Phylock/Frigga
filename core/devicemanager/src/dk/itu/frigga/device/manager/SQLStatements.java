package dk.itu.frigga.device.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author phylock
 */
public class SQLStatements {

    private static final String FORMAT_INSERT = "INSERT OR REPLACE INTO %s(%s) values (%s)";

    /** INSERTS **/
    private static final String _DEVICE_INSERT =
            String.format(FORMAT_INSERT, "device", "name,symbolic,last_update,online", "?,?,?,?");
    private static final String _CATEGORY_INSERT =
            String.format(FORMAT_INSERT, "category", "catname", "?");

    /** SELECT **/
    private static final String _DEVICE_SELECT_BY_SYMBOLIC =
            "SELECT id, name, symbolic, last_update, online FROM device WHERE symbolic = ?";

    private static final String _CATEGORY_SELECT_BY_CATNAME =
            "SELECT id, catname FROM category WHERE catname = ?";

    private static final String _CATEGORY_SELECT_BY_DEVICEID =
            "SELECT id, catname FROM category, devicecategory WHERE ? = devicecategory.device_id AND devicecategory.category_id = category.id ";

    private static final String _DEVICE_SELECT_BY_CATEGORY =
            "SELECT "
            +   "device.id, device.name, device.symbolic, device.last_update, device.online, category.catname "
            + "FROM "
            +   "device,category, devicecategory "
            + "WHERE device.id = devicecategory.device_id AND devicecategory.category_id = category.id AND category.catname = ?";


    /** RELATIONSHIP **/
    private static final String _CATEGORY_DEVICE_RELATIONSHIP =
            "insert into devicecategory (device_id, category_id) select "
            + "device.id, category.id from device, category WHERE device.symbolic = ? and category.catname = ?";


    /** PREPARED STATEMENTS **/
    public PreparedStatement DEVICE_INSERT;
    public PreparedStatement CATEGORY_INSERT;
    public PreparedStatement DEVICE_SELECT_BY_SYMBOLIC;
    public PreparedStatement DEVICE_SELECT_BY_CATEGORY;
    public PreparedStatement CATEGORY_SELECT_BY_CATNAME;
    public PreparedStatement CATEGORY_SELECT_BY_DEVICEID;
    public PreparedStatement CATEGORY_DEVICE_RELATIONSHIP;

    public SQLStatements(Connection conn) throws SQLException
    {
        DEVICE_INSERT = conn.prepareStatement(_DEVICE_INSERT);
        CATEGORY_INSERT = conn.prepareStatement(_CATEGORY_INSERT);
        DEVICE_SELECT_BY_SYMBOLIC = conn.prepareStatement(_DEVICE_SELECT_BY_SYMBOLIC);
        DEVICE_SELECT_BY_CATEGORY = conn.prepareStatement(_DEVICE_SELECT_BY_CATEGORY);
        CATEGORY_SELECT_BY_CATNAME = conn.prepareStatement(_CATEGORY_SELECT_BY_CATNAME);
        CATEGORY_SELECT_BY_DEVICEID = conn.prepareStatement(_CATEGORY_SELECT_BY_DEVICEID);
        CATEGORY_DEVICE_RELATIONSHIP = conn.prepareStatement(_CATEGORY_DEVICE_RELATIONSHIP);
    }
}

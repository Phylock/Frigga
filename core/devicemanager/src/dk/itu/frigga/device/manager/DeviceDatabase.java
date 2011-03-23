package dk.itu.frigga.device.manager;

import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceData;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.service.log.LogService;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author phylock
 */
public class DeviceDatabase {

    private static final String DEVICE_INSERT_UPDATE = "INSERT OR REPLACE INTO device(name,symbolic,last_update,online) values (?,?,?,?)";
    private static final String CATEGORY_INSERT_UPDATE = "INSERT OR REPLACE INTO category(name) values (?)";
    private static final String CATEGORY_DEVICE_RELATIONSHIP =
            "insert into devicecategory (device_id, category_id) select"
            + "(select device.id as device_id from device WHERE symbolic = ?),"
            + "(select category.id as category_id from CATEGORY WHERE name = ?)";
    private PreparedStatement stm_device_insert_update;
    private PreparedStatement stm_category_insert_update;
    private PreparedStatement stm_category_device_relationship;
    private String groupname;
    private String filename;
    private static final String DRIVERNAME = "org.sqlite.JDBC";
    private DataManager datamanager;
    private LogService log;
    private Connection conn;

    public DeviceDatabase(String groupname, String filename) {
        this.groupname = groupname;
        this.filename = filename;
    }

    public void initialize() {
        try {
            if (!datamanager.hasConnection(groupname)) {
                datamanager.addConnection(groupname, new DataConnection("jdbc", "sqlite", filename, DRIVERNAME));
            }
            conn = datamanager.requestConnection(groupname);
            InputStream fs = DeviceDatabase.class.getResourceAsStream("devicetables.sql");
            runStream(conn, fs, ";");

            stm_device_insert_update = conn.prepareStatement(DEVICE_INSERT_UPDATE);
            stm_category_insert_update = conn.prepareStatement(CATEGORY_INSERT_UPDATE);
            stm_category_device_relationship = conn.prepareStatement(CATEGORY_DEVICE_RELATIONSHIP);


        } catch (SQLException ex) {
            Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataGroupNotFoundException ex) {
            Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownDataDriverException ex) {
            Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runStream(Connection conn, InputStream data, String delimiter) {

        Scanner s = new Scanner(data);
        s.useDelimiter(delimiter);
        int stmnum = 0;
        String line = "";
        while (s.hasNext()) {
            try {
                Statement st = conn.createStatement();
                line = s.next();
                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            } catch (SQLException ex) {
                log.log(LogService.LOG_WARNING, "Error in line " + stmnum + ": " + line, ex);
            }
        }
    }

    public void update(List<DeviceData> devices, List<DeviceCategory> categories) throws SQLException {
        conn.setAutoCommit(false);

        //Categories
        if (categories != null) {
            for (DeviceCategory category : categories) {
                try {
                    stm_category_insert_update.setString(1, category.getTypeString());
                    stm_category_insert_update.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        conn.commit();

        //Devices
        if (devices != null) {
            for (DeviceData device : devices) {
                try {
                    stm_device_insert_update.setString(1, device.getName());
                    stm_device_insert_update.setString(2, device.getSymbolic());
                    stm_device_insert_update.setDate(3, new java.sql.Date(device.getLast_updated().getTime()));
                    stm_device_insert_update.setBoolean(4, device.isOnline());

                    stm_device_insert_update.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(DeviceDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        conn.commit();

        //Categories <-> Devices
        if (devices != null) {
            for (DeviceData device : devices) {

                for (String category : device.getCategories()) {
                    try {
                        stm_category_device_relationship.setString(1, device.getSymbolic());
                        stm_category_device_relationship.setString(2, category);

                        stm_category_device_relationship.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(LogService.LOG_WARNING, "could not link " + device.getSymbolic() + " <-> " + category);
                    }
                }
            }
        }



        conn.commit();
        conn.setAutoCommit(true);

    }
}

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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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

    private String groupname;
    private String filename;
    private static final String DRIVERNAME = "org.sqlite.JDBC";
    private DataManager datamanager;
    private LogService log;
    private Connection conn;
    private SQLStatements statement;

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

            statement = new SQLStatements(conn);
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

    public DeviceData getDeviceBySymbolic(String symbolic)
    {
        DeviceData dd = null;
        try {
            PreparedStatement select = statement.DEVICE_SELECT_BY_SYMBOLIC;
            PreparedStatement categories = statement.CATEGORY_SELECT_BY_DEVICEID;
            select.setString(1, symbolic);
            ResultSet rs = select.executeQuery();
            if(rs.next())
            {
                //id, name, symbolic, last_update, online
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date last_update = rs.getDate("last_update");
                boolean online = rs.getBoolean("online");

                categories.setInt(1, id);
                ResultSet crs = categories.executeQuery();
                List<String> list = new ArrayList<String>();
                while(crs.next())
                {
                    list.add(crs.getString("catname"));
                }
            
                dd = new DeviceData(name, symbolic, last_update, online, list.toArray(new String[list.size()]));
            }
        } catch (SQLException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
        return dd;
    }

       public List<DeviceData> getDeviceByCategory(String category)
    {
        List<DeviceData> data = new ArrayList<DeviceData>();
        try {
            PreparedStatement select = statement.DEVICE_SELECT_BY_CATEGORY;
            select.setString(1, category);
            ResultSet rs = select.executeQuery();

            while(rs.next())
            {
                //id, name, symbolic, last_update, online
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String symbolic = rs.getString("symbolic");
                Date last_update = rs.getDate("last_update");
                boolean online = rs.getBoolean("online");

                data.add(new DeviceData(name, symbolic, last_update, online, new String[]{category}));
            }
        } catch (SQLException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
        return data;
    }


    public void update(List<DeviceData> devices, List<DeviceCategory> categories) throws SQLException {
        conn.setAutoCommit(false);
        updateCategory(categories);
        conn.commit();
        updateDevice(devices);
        conn.commit();

        //Categories <-> Devices
        if (devices != null) {
            PreparedStatement stmt = statement.CATEGORY_DEVICE_RELATIONSHIP;
            for (DeviceData device : devices) {

                for (String category : device.getCategories()) {
                    try {
                        stmt.setString(1, device.getSymbolic());
                        stmt.setString(2, category);

                        stmt.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(LogService.LOG_WARNING, "could not link " + device.getSymbolic() + " <-> " + category);
                    }
                }
            }
        }

        conn.commit();
        conn.setAutoCommit(true);
    }

    private void updateDevice(List<DeviceData> devices) {
        //Devices
        if (devices != null) {
            PreparedStatement select = statement.DEVICE_SELECT_BY_SYMBOLIC;
            PreparedStatement insert = statement.DEVICE_INSERT;
            for (DeviceData device : devices) {
                try {
                    select.setString(1, device.getSymbolic());
                    ResultSet rs = select.executeQuery();
                    if (rs.next()) {
                        rs.updateString("name", device.getName());
                        rs.updateDate("last_update", new java.sql.Date(device.getLast_updated().getTime()));
                        rs.updateBoolean("online", device.isOnline());
                        rs.updateRow();
                    } else {
                        insert.setString(1, device.getName());
                        insert.setString(2, device.getSymbolic());
                        insert.setDate(3, new java.sql.Date(device.getLast_updated().getTime()));
                        insert.setBoolean(4, device.isOnline());
                        insert.executeUpdate();
                    }
                } catch (SQLException ex) {
                    log.log(LogService.LOG_WARNING, null, ex);
                }
            }
        }
    }

    private void updateCategory(List<DeviceCategory> categories) {
        //Categories
        if (categories != null) {
            PreparedStatement select = statement.CATEGORY_SELECT_BY_CATNAME;
            PreparedStatement insert = statement.CATEGORY_INSERT;
            for (DeviceCategory category : categories) {
                try {
                    select.setString(1, category.getTypeString());
                    ResultSet rs = select.executeQuery();
                    if (rs.next()) {
                        //do nothing, we only have one column in category and we just checked for that
                    } else {
                        insert.setString(1, category.getTypeString());
                        insert.executeUpdate();
                    }
                } catch (SQLException ex) {
                    log.log(LogService.LOG_WARNING, null, ex);
                }
            }
        }
    }
}

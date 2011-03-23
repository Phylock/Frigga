package dk.itu.frigga.device.manager;

import dk.itu.frigga.data.DataConnection;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
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

    public DeviceDatabase(String groupname, String filename) {
        this.groupname = groupname;
        this.filename = filename;
    }

    public void initialize() {
        try {
            if (!datamanager.hasConnection(groupname)) {
                datamanager.addConnection(groupname, new DataConnection("jdbc", "sqlite", filename, DRIVERNAME));
            }
            Connection conn = datamanager.requestConnection(groupname);
            InputStream fs = DeviceDatabase.class.getResourceAsStream("devicetables.sql");
            runStream(conn, fs, ";");
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
}

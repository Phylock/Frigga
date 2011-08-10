/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.tui.data;

import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.TimeoutException;
import dk.itu.frigga.data.UnknownDataDriverException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author phylock
 */
public class DataTui {

    public static final String SCOPE = "frigga";
    public static final String FUNCTIONS[] = new String[]{
        "sql"
    };
    private BundleContext context;

    public DataTui(BundleContext context) {
        this.context = context;
    }

    /*@Descriptor("Call a function in a device")*/
    public void sql(
            /*@Descriptor("connection")*/String connection,
            /*@Descriptor("query")*/ String query) throws SQLException, DataGroupNotFoundException, UnknownDataDriverException {
        DataManager dataManager = getDataManager();
        if (dataManager.hasConnection(connection)) {
            ConnectionPool pool = dataManager.requestConnection(connection);
            Connection conn = null;
            try {
                conn = pool.getConnection(30000);
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.execute();
                if(!conn.getAutoCommit())
                {
                    conn.commit();
                }
            } catch (TimeoutException ex) {
                throw new RuntimeException("Timeout, waited 30 seconds ");
            } finally{
                if(conn != null)
                {
                    pool.releaseConnection(conn);
                }
            }
        }else
        {
            throw new RuntimeException("Unknown database: " + connection);
        }
    }

    protected DataManager getDataManager() {
        ServiceReference ref = context.getServiceReference(DataManager.class.getName());
        if (ref == null) {
            throw new RuntimeException("DataManger is not registered");
        }
        DataManager manager = (DataManager) context.getService(ref);
        if (manager == null) {
            throw new RuntimeException("DataManger is not registered");
        }
        return manager;
    }
}

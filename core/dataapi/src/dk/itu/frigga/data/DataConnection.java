/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

/**
 *
 * @author Tommy
 */
public class DataConnection {
    private final String system;
    private final String driver;
    private final String dbUri;
    private final String driverClass;
    private boolean initialized = false;

    public DataConnection(final String system, final String driver, final String dbUri, final String driverClass)
    {
        this.system = system;
        this.driver = driver;
        this.dbUri = dbUri;
        this.driverClass = driverClass;
    }

    protected void initialize() throws UnknownDataDriverException
    {
        if (!initialized)
        {
            try
            {
                Class.forName(driverClass);
                initialized = true;
            }
            catch (ClassNotFoundException ex)
            {
                throw new UnknownDataDriverException(driverClass);
            }
        }
    }

    protected String getConnectionUrl()
    {
        return String.format("%s:%s:%s", system, driver, dbUri);
    }
}

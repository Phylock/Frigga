/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

/**
 *
 * @author Tommy
 */
public class DataConnection
{
    private final String system;
    private final String driver;
    private final String dbUri;
    private final String driverClass;
    private boolean initialized = false;

    public DataConnection(final String system, final String driver, final String dbUri, final String driverClass)
    {
        assert(system != null) : "Data connection cannot be instantiated with a null value for 'system'";
        assert(driver != null) : "Data connection cannot be instantiated with a null value for 'driver'";
        assert(dbUri != null) : "Data connection cannot be instantiated with a null value for 'dbUri'";
        assert(driverClass != null) : "Data connection cannot be instantiated with a null value for 'driverClass'";
        
        this.system = system;
        this.driver = driver;
        this.dbUri = dbUri;
        this.driverClass = driverClass;
    }

    /**
     * Initializes the data connection driver, this is the same as calling the
     * {$code Class.forName } for the driver class. Calling this method multiple
     * times will not initialize the driver more than once. Any subsequent call
     * of the method will simply do nothing.
     *
     * @throws UnknownDataDriverException
     */
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
                // We don't need ex.
                throw new UnknownDataDriverException(driverClass);
            }
        }
    }

    /**
     * Retrieves the connection string for DriverManager, in the form
     * system:driver:uri
     *
     * @return The connection string to use with DriverManager
     */
    protected String getConnectionUrl()
    {
        return String.format("%s:%s:%s", system, driver, dbUri);
    }
}

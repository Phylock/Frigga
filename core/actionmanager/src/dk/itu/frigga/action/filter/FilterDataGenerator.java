package dk.itu.frigga.action.filter;

import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceVariable;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class FilterDataGenerator
{
    // iPOJO fields
    @Requires
    private DataManager datamanager;
    private ConnectionPool connectionPool;

    private Set<Device> devices = Collections.synchronizedSet(new LinkedHashSet<Device>());
    private FilterInput filterInput = new FilterInput();


    public FilterDataGenerator()
    {

    }

    private Set<String> loadVariableNames(final long categoryIndex, final Connection conn) throws SQLException
    {
        Set<String> variables = new LinkedHashSet<String>();
        PreparedStatement statementVariables = conn.prepareStatement("SELECT vt.varname AS varname FROM variabletype vt, category_variable cv WHERE cv.variable_id = vt.id AND cv.category_id = ?");
        statementVariables.setLong(0, categoryIndex);
        ResultSet resultVariables = statementVariables.executeQuery();

        while (resultVariables.next())
        {
            variables.add(resultVariables.getString("varname"));
        }

        return variables;
    }

    private Set<DeviceCategory> loadCategories(final long deviceIndex, final Connection conn) throws SQLException
    {
        Set<DeviceCategory> categories = new LinkedHashSet<DeviceCategory>();
        PreparedStatement statementCategories = conn.prepareStatement("SELECT c.id AS id, c.catname AS catname FROM category c, device_category dc WHERE dc.category_id = c.id AND dc.device_id = ?");
        statementCategories.setLong(0, deviceIndex);
        ResultSet resultCategories = statementCategories.executeQuery();

        while (resultCategories.next())
        {
            Set<String> variables = loadVariableNames(resultCategories.getLong("id"), conn);

            DeviceCategory category = new DeviceCategory(resultCategories.getString("catname"), variables);
            categories.add(category);
        }

        return categories;
    }

    private Map<String, DeviceVariable> loadVariables(final long deviceIndex, final Connection conn) throws SQLException
    {
        Map<String, DeviceVariable> variables = new HashMap<String, DeviceVariable>();
        PreparedStatement statementVariables = conn.prepareStatement("SELECT vt.varname AS varname, vt.vartype AS vartype, dv.value AS varvalue FROM device_variable dv, variabletype vt WHERE dv.variable_id = vt.id AND dv.device_id = ?");
        statementVariables.setLong(0, deviceIndex);
        ResultSet resultVariables = statementVariables.executeQuery();

        while (resultVariables.next())
        {
            DeviceVariable variable = new DeviceVariable(resultVariables.getString("varname"), resultVariables.getString("vartype"));
            variable.setValue(resultVariables.getString("varvalue"));
            variables.put(resultVariables.getString("varname"), variable);
        }

        return variables;
    }

    public void buildData() throws SQLException
    {
        Connection conn = connectionPool.getConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM device");
        ResultSet result = statement.executeQuery();

        while (result.next())
        {
            Set<DeviceCategory> categories = loadCategories(result.getLong("id"), conn);
            Map<String, DeviceVariable> variables = loadVariables(result.getLong("id"), conn);

            Device device = new Device(result.getString("devname"), result.getString("symbolic"), result.getString("driver"), result.getBoolean("online"), result.getDate("last_update"), categories, variables);
            devices.add(device);
        }

        filterInput.devices.addAll(devices);
    }

    public FilterInput getFilterInput()
    {
        return filterInput;
    }

    @Validate
    private void validate()
    {
        if (datamanager.hasConnection("device"))
        {
            try
            {
                connectionPool = datamanager.requestConnection("device");
            }
            catch (SQLException e)
            {
                // Failed
            }
            catch (DataGroupNotFoundException e)
            {
                // Failed
            }
            catch (UnknownDataDriverException e)
            {
                // Failed
            }
        }
    }

    @Invalidate
    private void invalidate()
    {
        connectionPool = null;
    }
}

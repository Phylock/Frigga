/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.manager.dao;

import dk.itu.frigga.data.PreparedStatementProxy;
import dk.itu.frigga.data.dao.GenericSqlDao;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceVariable;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.model.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author phylock
 */
public class DeviceDaoSql extends GenericSqlDao<Device, Long> implements DeviceDAO
{

    private static final String[] FIELDS = new String[]{"devname", "symbolic", "last_update", "online", "driver"};
    private static final String ID = "id";
    public static final String TABLE = "device";
    private final PreparedStatementProxy SELECT_BY_CATEGORY;
    private final PreparedStatementProxy SELECT_BY_SYMBOLIC;
    private final PreparedStatementProxy ADD_TO_CATEGORY;
    private final PreparedStatementProxy REMOVE_FROM_CATEGORY;
    private final PreparedStatementProxy IS_OF_CATEGORY;
    private final PreparedStatementProxy ADD_VARIABLE;
    private final PreparedStatementProxy REMOVE_VARIABLE;
    private final PreparedStatementProxy HAS_VARIABLE;
    private final PreparedStatementProxy SELECT_VARIABLE;
    private final PreparedStatementProxy SELECT_ALL_VARIABLE;
    private final PreparedStatementProxy UPDATE_BY_DRIVER;

    public DeviceDaoSql()
    {
        SELECT_BY_CATEGORY = new PreparedStatementProxy("SELECT d.* FROM device as d, category as c, device_category as dc WHERE d.id = dc.device_id and dc.category_id = c.id and c.catname = ?");
        SELECT_BY_SYMBOLIC = new PreparedStatementProxy("SELECT * FROM device WHERE symbolic = ?");
        IS_OF_CATEGORY = new PreparedStatementProxy("SELECT d.* FROM device as d, category as c, device_category as dc WHERE d.id = dc.device_id and dc.category_id = c.id and c.catname = ? and d.symbolic = ?");
        ADD_TO_CATEGORY = new PreparedStatementProxy("INSERT INTO device_category (device_id, category_id) SELECT "
                + "device.id, category.id FROM device, category WHERE device.symbolic = ? and category.catname = ?");
        REMOVE_FROM_CATEGORY = new PreparedStatementProxy("DELETE FROM device_category "
                + "WHERE device_id=? AND category_id=?");

        ADD_VARIABLE = new PreparedStatementProxy("");
        REMOVE_VARIABLE = new PreparedStatementProxy("");
        HAS_VARIABLE = new PreparedStatementProxy("");
        SELECT_VARIABLE = new PreparedStatementProxy("");
        SELECT_ALL_VARIABLE = new PreparedStatementProxy("SELECT dv.* FROM device as d, device_variable as dv WHERE d.id = dv.device_id AND d.symbolic = ?");
        UPDATE_BY_DRIVER = new PreparedStatementProxy("");
    }

    private Set<String> loadVariableNames(final long categoryIndex) throws SQLException
    {
        Set<String> variables = new LinkedHashSet<String>();
        PreparedStatement statementVariables = connection.prepareStatement("SELECT vt.varname AS varname FROM variabletype vt, category_variable cv WHERE cv.variable_id = vt.id AND cv.category_id = ?");
        statementVariables.setLong(0, categoryIndex);
        ResultSet resultVariables = statementVariables.executeQuery();

        while (resultVariables.next())
        {
            variables.add(resultVariables.getString("varname"));
        }

        return variables;
    }

    private Set<DeviceCategory> loadCategories(final long deviceIndex) throws SQLException
    {
        Set<DeviceCategory> categories = new LinkedHashSet<DeviceCategory>();
        PreparedStatement statementCategories = connection.prepareStatement("SELECT c.id AS id, c.catname AS catname FROM category c, device_category dc WHERE dc.category_id = c.id AND dc.device_id = ?");
        statementCategories.setLong(0, deviceIndex);
        ResultSet resultCategories = statementCategories.executeQuery();

        while (resultCategories.next())
        {
            Set<String> variables = loadVariableNames(resultCategories.getLong("id"));

            DeviceCategory category = new DeviceCategory(resultCategories.getString("catname"), variables);
            categories.add(category);
        }

        return categories;
    }

    private Map<String, DeviceVariable> loadVariables(final long deviceIndex) throws SQLException
    {
        Map<String, DeviceVariable> variables = new HashMap<String, DeviceVariable>();
        PreparedStatement statementVariables = connection.prepareStatement("SELECT vt.varname AS varname, vt.vartype AS vartype, dv.value AS varvalue FROM device_variable dv, variabletype vt WHERE dv.variable_id = vt.id AND dv.device_id = ?");
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

    public List<Device> findByCategory(Category category)
    {
        List<Device> list = new ArrayList<Device>();
        if (category == null)
        {
            return list;
        }

        try
        {
            PreparedStatement stmt_select = SELECT_BY_CATEGORY.createPreparedStatement(connection);
            stmt_select.setString(/*Category*/1, category.getName());
            ResultSet rs = stmt_select.executeQuery();
            parseAll(rs, list);
            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Device> findByExample(Device exampleInstance)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Device makePersistent(Device entity)
    {
        boolean insert = false;
        boolean update = false;
        //Is required values defined
        if (entity.getName() == null)
        {
            throw new IllegalArgumentException("device.Name can not be null");
        }
        try
        {
            //Insert, Update or Ignore
            Device device;
            if (entity.getId() != null)
            {
                device = findById(entity.getId(), false);
                if (device != null && !device.equals(entity))
                {
                    update = true;
                }
            }
            else
            {
                device = findBySymbolic(entity.getSymbolic());
            }

            //exists?
            if (device == null)
            {
                insert = true;
            }

            if (insert)
            {
                PreparedStatement stmt_insert = INSERT.createPreparedStatement(connection);
                stmt_insert.setString(/*name*/1, entity.getName());
                stmt_insert.setString(/*symbolic*/2, entity.getSymbolic());
                stmt_insert.setString(/*last_update*/3, null);
                stmt_insert.setBoolean(/*online*/4, entity.isOnline());
                stmt_insert.setString(/*driver*/5, entity.getDriver());
                stmt_insert.executeUpdate();
                if (!connection.getAutoCommit())
                {
                    connection.commit();
                }

                device = findBySymbolic(entity.getName());
            }
            else if (update)
            {
                PreparedStatement stmt_update = UPDATE.createPreparedStatement(connection);
                stmt_update.setLong(/*id*/6, entity.getId());
                stmt_update.setString(/*name*/1, entity.getName());
                stmt_update.setString(/*symbolic*/2, entity.getSymbolic());
                stmt_update.setString(/*last_update*/3, null);
                stmt_update.setBoolean(/*online*/4, entity.isOnline());
                stmt_update.setString(/*driver*/5, entity.getDriver());
                stmt_update.executeUpdate();
                if (!connection.getAutoCommit())
                {
                    connection.commit();
                }
            }

            return device;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CategoryDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void makeTransient(Device entity)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addToCategory(Device device, Category category)
    {
        try
        {
            if (!isOfCategory(device, category))
            {
                PreparedStatement stmt_add = ADD_TO_CATEGORY.createPreparedStatement(connection);
                stmt_add.setString(/*Symbolic*/1, device.getSymbolic());
                stmt_add.setString(/*Category Name*/2, category.getName());
                stmt_add.executeUpdate();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeFromCategory(Device device, Category category)
    {
        try
        {
            if (isOfCategory(device, category))
            {
                PreparedStatement stmt_remove = REMOVE_FROM_CATEGORY.createPreparedStatement(connection);
                stmt_remove.setLong(1, device.getId());
                stmt_remove.setLong(2, category.getId());
                stmt_remove.executeUpdate();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Variable> getVariables(Device device)
    {
        List<Variable> variables = new LinkedList<Variable>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT " +
                    "  vt.id AS vartypeId, " +
                    "  vt.varname AS vartypeName, " +
                    "  vt.vartype AS vartypeType, " +
                    "  dv.variable_value AS varValue " +
                    "FROM " +
                    "  device_variable dv, " +
                    "  variabletype vt " +
                    "WHERE " +
                    "  dv.device_id = ? AND dv.variable_id = vt.id");
            stmt.setLong(1, device.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                VariableType vt = new VariableType(rs.getLong("vartypeId"), rs.getString("vartypeName"), rs.getString("vartypeType"));
                variables.add(new Variable(new VariablePK(device, vt), rs.getString("varValue")));
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, e);
        }

        return variables;
    }

    protected Device parseCurrent(ResultSet rs) throws SQLException
    {
        Long id = rs.getLong("id");
        String name = rs.getString("devname");
        String symbolic = rs.getString("symbolic");
        Date last = rs.getDate("last_update");
        boolean online = rs.getBoolean("online");
        String driver = rs.getString("driver");

        Device d = new Device(id, name, symbolic, last, online, driver);
        d.setDeviceDao(this);

        //DeviceDaoFactorySql.instance().getCategoryDao().

        return d;
    }

    @Override
    protected String getTable()
    {
        return TABLE;
    }

    public boolean isOfCategory(Device device, Category category)
    {
        boolean result = false;
        try
        {
            PreparedStatement stmt_select = IS_OF_CATEGORY.createPreparedStatement(connection);
            stmt_select.setString(1, category.getName());
            stmt_select.setString(2, device.getSymbolic());
            ResultSet exists = stmt_select.executeQuery();
            result = exists.next();
            exists.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<Category> getCategories(Device device)
    {
        long id = device.getId();
        List<Category> categories = new LinkedList<Category>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT c.id AS id, c.catname AS catname FROM device_category dc, category c WHERE dc.category_id = c.id AND dc.device_id = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Category category = new Category();
                category.setName(rs.getString("catname"));
                category.setId(rs.getLong("id"));

                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, e);
        }

        return categories;
    }

    public void setState(String symbolic, boolean state)
    {
        try
        {
            PreparedStatement stmt_select = SELECT_BY_SYMBOLIC.createPreparedStatement(connection);
            stmt_select.setString(/*Symbolic*/1, symbolic);
            ResultSet rs = stmt_select.executeQuery();
            if (rs.next())
            {
                rs.updateBoolean("online", state);
                rs.updateRow();
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Device findBySymbolic(String symbolic)
    {
        Device device = null;
        try
        {
            PreparedStatement stmt_select = SELECT_BY_SYMBOLIC.createPreparedStatement(connection);
            stmt_select.setString(/*Symbolic*/1, symbolic);
            ResultSet rs = stmt_select.executeQuery();
            if (rs.next())
            {
                device = parseCurrent(rs);
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DeviceDaoSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return device;
    }

    @Override
    protected String[] getFields()
    {
        return FIELDS;
    }

    @Override
    protected String getIdField()
    {
        return ID;
    }

    public void addVariable(Device device, VariableType category)
    {
        throw new UnsupportedOperationException("Not supported yet."); // <---------- : ( .. .. . . .   .      .       .
    }

    public void removeVariable(Device device, VariableType category)
    {
        throw new UnsupportedOperationException("Not supported yet."); // <---------- : ( .. .. . . .   .      .       .
    }

    public boolean hasVariable(Device device, VariableType category)
    {
        throw new UnsupportedOperationException("Not supported yet."); // <---------- : ( .. .. . . .   .      .       .
    }

    public void setStateByDriver(String driver, boolean state)
    {
        throw new UnsupportedOperationException("Not supported yet."); // <---------- : ( .. .. . . .   .      .       .
    }
}

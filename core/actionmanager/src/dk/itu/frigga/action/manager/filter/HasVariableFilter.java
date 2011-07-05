package dk.itu.frigga.action.manager.filter;

import dk.itu.frigga.action.filter.FilterContainer;
import dk.itu.frigga.action.filter.FilterContext;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterInput;
import dk.itu.frigga.action.manager.runtime.DeviceSelection;
import dk.itu.frigga.action.runtime.Selection;
import dk.itu.frigga.device.model.Device;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public class HasVariableFilter extends DbFilter
{
    private String attrName;
    private String attrType = "string";

    @Language("SQL")
    private String query;

    public HasVariableFilter()
    {
    }

    public HasVariableFilter(FilterContainer filterContainer)
    {
        super(filterContainer);
    }

    @Override
    public List<Selection> run(final FilterContext context, final FilterInput filterInput) throws FilterFailedException
    {
        if (context == null) throw new IllegalArgumentException("Argument 'context' is null.");
        if (filterInput == null) throw new IllegalArgumentException("Argument 'filterInput' is null.");

        LinkedList<Selection> result = new LinkedList<Selection>();

        try
        {
            Connection conn = null;

            try
            {
                conn = connectionPool.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(0, attrName);
                if (!attrType.isEmpty()) stmt.setString(1, attrType);
                ResultSet rs = stmt.executeQuery();

                while (rs.next())
                {
                    result.add(new DeviceSelection(new Device(rs.getLong(0))));
                }

                rs.close();
            }
            finally
            {
                connectionPool.releaseConnection(conn);
            }
        }
        catch (SQLException e)
        {
            throw new FilterFailedException();
        }

        return result;
    }

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        assert(attributes != null) : "attributes can not be null.";

        if (attributes.containsKey("name"))
        {
            attrName = attributes.get("name");
        }

        if (attributes.containsKey("type"))
        {
            attrType = attributes.get("type");
        }

        query = "SELECT d.id " +
                "FROM device d, device_variable dv, variabletype vt " +
                "WHERE vt.varname = ? AND " +
                "vt.id = dv.variable_id AND " +
                "dv.device_id = d.id";
        if (!attrType.isEmpty())
        {
            query += "AND vt.vartype = ?";
        }
        query += "LIMIT 1";
    }

    @Override
    protected boolean allowChildFilters()
    {
        return false;
    }
}

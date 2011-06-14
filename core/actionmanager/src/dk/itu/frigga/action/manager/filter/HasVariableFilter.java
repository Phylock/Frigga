package dk.itu.frigga.action.manager.filter;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.action.filter.FilterContainer;
import dk.itu.frigga.action.filter.FilterFactory;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.manager.runtime.DeviceSelection;
import dk.itu.frigga.action.runtime.Selection;
import dk.itu.frigga.device.model.Device;
import org.intellij.lang.annotations.Language;
import org.w3c.dom.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
    private final String SQL_QUERY =
            "SELECT d.id " +
            "FROM device d, device_variable dv, variabletype vt " +
            "WHERE vt.varname = ? AND " +
                  "vt.id = dv.variable_id AND " +
                  "dv.device_id = d.id " +
            "LIMIT 1";

    public HasVariableFilter()
    {
    }

    public HasVariableFilter(FilterContainer filterContainer)
    {
        super(filterContainer);
    }

    @Override
    public void parse(FilterFactory factory, Element element)
    {
        super.parse(factory, element);

        if (element.hasAttribute("name"))
        {
            attrName = element.getAttribute("name");
        }

        if (element.hasAttribute("type"))
        {
            attrType = element.getAttribute("type");
        }
    }

    @Override
    public List<Selection> run() throws FilterFailedException
    {
        LinkedList<Selection> result = new LinkedList<Selection>();

        try
        {
            Connection conn = null;

            try
            {
                conn = connectionPool.getConnection();

                PreparedStatement stmt = conn.prepareStatement(SQL_QUERY);
                stmt.setString(0, attrName);
                ResultSet rs = stmt.executeQuery();

                while (rs.next())
                {
                    DeviceSelection selection = new DeviceSelection(new Device(rs.getLong(0)));
                    result.add(selection);
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
}

package dk.itu.frigga.action.manager.filter;

import dk.itu.frigga.action.filter.Filter;
import dk.itu.frigga.action.filter.FilterContainer;
import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import java.sql.SQLException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
@Component
public abstract class DbFilter extends Filter
{
    // iPOJO fields
    @Requires
    private DataManager datamanager;

    protected ConnectionPool connectionPool;

    public DbFilter()
    {
    }

    public DbFilter(FilterContainer filterContainer)
    {
        super();

        setContainer(filterContainer);
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

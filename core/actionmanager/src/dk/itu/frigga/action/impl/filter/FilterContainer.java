package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.action.filter.FilterNotFoundException;
import dk.itu.frigga.action.filter.NotANamedFilterException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public interface FilterContainer
{
    void registerNamedFilter(final Filter filter) throws NotANamedFilterException;
    void unregisterNamedFilter(final Filter filter) throws FilterNotFoundException;
    Filter getNamedFilter(final String name) throws FilterNotFoundException;
}

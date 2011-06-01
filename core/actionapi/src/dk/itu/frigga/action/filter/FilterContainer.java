package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.filter.Filter;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public interface FilterContainer
{
    public void registerNamedFilter(final Filter filter);
    public Filter getNamedFilter(final String name);
}

package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class EmptyFilter extends Filter
{
    private final boolean allowChildren;

    public EmptyFilter()
    {
        this.allowChildren = true;
    }

    public EmptyFilter(final boolean allowChildFilters)
    {
        this.allowChildren = allowChildFilters;
    }

    public EmptyFilter(FilterContainer filterContainer)
    {
        super();

        setContainer(filterContainer);
        this.allowChildren = true;
    }

    @Override
    public FilterOutput run(final FilterContext context, final FilterInput input) throws FilterFailedException
    {
        return new FilterOutput(input);
    }

    @Override
    protected boolean allowChildFilters()
    {
        return allowChildren;
    }
}

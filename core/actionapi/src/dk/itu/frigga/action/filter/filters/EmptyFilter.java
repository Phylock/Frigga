package dk.itu.frigga.action.filter.filters;

import dk.itu.frigga.action.filter.*;
import dk.itu.frigga.action.runtime.Selection;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class EmptyFilter extends Filter
{
    private final boolean childFilters;

    public EmptyFilter()
    {
        this.childFilters = true;
    }

    public EmptyFilter(final boolean childFilters)
    {
        this.childFilters = childFilters;
    }

    public EmptyFilter(FilterContainer filterContainer)
    {
        super();

        setContainer(filterContainer);
        this.childFilters = true;
    }

    @Override
    public FilterOutput run(final FilterContext context, final FilterInput input) throws FilterFailedException
    {
        return new FilterOutput(input);
    }

    @Override
    protected boolean allowChildFilters()
    {
        return childFilters;
    }
}

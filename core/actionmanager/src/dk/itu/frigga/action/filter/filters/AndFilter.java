package dk.itu.frigga.action.filter.filters;

import dk.itu.frigga.action.filter.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class AndFilter extends Filter
{
    @Override
    public LogicMergeMethod mergeMethod()
    {
        return LogicMergeMethod.AND;
    }

    @Override
    public FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        return new FilterOutput(input);
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

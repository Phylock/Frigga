package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-15
 */
public class IsOnlineFilter extends Filter
{
    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        for (FilterDeviceState state : input)
        {
            if (state.getDevice().isOnline()) output.addDevice(state);
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

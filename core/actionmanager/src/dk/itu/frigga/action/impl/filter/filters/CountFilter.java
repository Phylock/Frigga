package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.Filter;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterInput;
import dk.itu.frigga.action.impl.filter.FilterOutput;

import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public class CountFilter extends Filter
{
    private int min = 0;
    private int max = Integer.MAX_VALUE;

    @Override
    public int optimizeMaxReturnCount()
    {
        return Math.max(max - min, 0);
    }

    @Override
    public boolean optimizeCanDiscardChildFilters()
    {
        return (max - min) <= 0;
    }

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("min"))
        {
            min = parseInt(attributes.get("min"), 10);
        }

        if (attributes.containsKey("max"))
        {
            max = parseInt(attributes.get("max"), 10);
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        int count = input.getDevices().size();

        if (count >= min && count <= max)
        {
            output.useInput(input);
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.runtime.Selection;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public class FilterContext
{
    public FilterInput runFilter(final Filter filter, final FilterInput input) throws FilterFailedException
    {
        FilterInput result = input;

        if (filter.childFilters.size() > 0)
        {
            switch (filter.mergeMethod())
            {
                case OR:
                    result = new FilterInput(runOrFilter(filter.childFilters, new FilterInput()));
                    break;

                case AND:
                    result = new FilterInput(runAndFilter(filter.childFilters, input));
                    break;
            }
        }

        return result;
    }

    protected FilterOutput runAndFilter(final List<Filter> filters, final FilterInput input) throws FilterFailedException
    {
        FilterInput inp = input;
        FilterOutput output = new FilterOutput();
        boolean first = true;

        for (Filter filter : filters)
        {
            if (!first)
            {
                inp = new FilterInput(output);
            }

            inp = runFilter(filter, inp);

            output = filter.run(this, inp);
            first = false;
        }

        return output;
    }

    protected FilterOutput runOrFilter(final List<Filter> filters, final FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        for (Filter filter : filters)
        {
            FilterInput inp = runFilter(filter, input);
            output.merge(filter.run(this, inp));
        }

        return output;
    }
}

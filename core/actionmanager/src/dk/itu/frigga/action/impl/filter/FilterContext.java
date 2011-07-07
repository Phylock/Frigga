package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.action.filter.FilterFailedException;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public class FilterContext
{
    private FilterDataGenerator filterGenerator = new FilterDataGenerator();

    public FilterOutput run(final Filter filter) throws FilterFailedException
    {
        return runFilter(filter, filterGenerator.getFilterInput());
    }

    protected FilterOutput runFilter(final Filter filter, final FilterInput input) throws FilterFailedException
    {
        if (filter.childFilters.size() > 0)
        {
            switch (filter.mergeMethod())
            {
                case OR:
                    return filter.execute(this, new FilterInput(runOrFilter(filter.childFilters, filterGenerator.getFilterInput())));

                case AND:
                    return filter.execute(this, new FilterInput(runAndFilter(filter.childFilters, input)));
            }
        }

        return filter.run(this, input);
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

            inp = new FilterInput(runFilter(filter, inp));

            output = runFilter(filter, inp);
            first = false;
        }

        return output;
    }

    protected FilterOutput runOrFilter(final List<Filter> filters, final FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        for (Filter filter : filters)
        {
            output.merge(runFilter(filter, input));
        }

        return output;
    }
}

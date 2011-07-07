package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.Filter;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterInput;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.device.model.Device;

import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public class PassCountFilter extends Filter
{
    int max = Integer.MAX_VALUE;
    int offset = -1;
    boolean reverse = false;

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("max"))
        {
            max = parseInt(attributes.get("max"), 10);
        }

        if (attributes.containsKey("offset"))
        {
            offset = parseInt(attributes.get("offset"), 10);
        }

        if (attributes.containsKey("reverse"))
        {
            reverse = Boolean.parseBoolean(attributes.get("reverse"));
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        int remaining = max;

        for (Device device : input)
        {
            if (remaining > 0)
            {
                output.addDevice(device);
                remaining--;
            }
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Device;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-08
 */
public class MatchSymbolicFilter extends Filter
{
    private String pattern = "";
    private boolean useRegex = false;

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("pattern"))
        {
            pattern = attributes.get("pattern");
        }

        if (attributes.containsKey("useRegex"))
        {
            useRegex = Boolean.parseBoolean(attributes.get("useRegex"));
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        String pattern = context.prepare(this.pattern);

        for (FilterDeviceState deviceStatee : input)
        {
            if ((useRegex && deviceStatee.getDevice().getSymbolic().matches(pattern)) || !useRegex && deviceStatee.getDevice().getSymbolic().equals(pattern))
            {
                output.addDevice(deviceStatee);

                // If we are not using regex we can at most have one match.
                if (!useRegex) break;
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

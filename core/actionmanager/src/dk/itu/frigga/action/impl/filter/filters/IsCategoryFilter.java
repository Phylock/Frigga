package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class IsCategoryFilter extends Filter
{
    private Pattern category = Pattern.compile(".*", Pattern.CASE_INSENSITIVE);

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("category"))
        {
            category = Pattern.compile(attributes.get("category"), Pattern.CASE_INSENSITIVE);
        }
    }

    @Override
    public FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        Matcher matcher = category.matcher("");

        for (FilterDeviceState deviceState : input)
        {
            for (Category c : deviceState.getDevice().getCategories())
            {
                if (matcher.reset(c.getName()).matches())
                {
                    output.addDevice(deviceState);
                    break;
                }
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

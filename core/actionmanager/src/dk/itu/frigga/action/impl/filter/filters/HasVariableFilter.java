package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.Device;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class HasVariableFilter extends Filter
{
    private Pattern variableName = Pattern.compile(".*");
    private Pattern variableType = Pattern.compile(".*");

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("name"))
        {
            variableName = Pattern.compile(attributes.get("name"));
        }

        if (attributes.containsKey("type"))
        {
            variableType = Pattern.compile(attributes.get("type"));
        }
    }

    @Override
    public FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        for (Device device : input)
        {
            if (device.hasVariableMatch(variableName, variableType))
            {
                output.addDevice(device);
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

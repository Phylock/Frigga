package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
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
        Matcher matcher1 = variableName.matcher("");
        Matcher matcher2 = variableType.matcher("");

        for (Device device : input)
        {
            Set<Variable> variables = device.getVariables();
            for (Variable variable : variables)
            {
                String name = variable.getPrimaryKey().getVariabletype().getName();
                if (matcher1.reset(name).matches())
                {
                    String type = variable.getPrimaryKey().getVariabletype().getType();
                    if (matcher2.reset(type).matches())
                    {
                        output.addDevice(device);
                        break;
                    }
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

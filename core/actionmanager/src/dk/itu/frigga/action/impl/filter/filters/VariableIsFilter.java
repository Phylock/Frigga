package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Variable;

import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-17
 */
public class VariableIsFilter extends Filter
{
    String variableName = "";
    String variableValue = "";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("name"))
        {
            variableName = attributes.get("name");
        }

        if (attributes.containsKey("value"))
        {
            variableValue = attributes.get("value");
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        String localName = context.prepare(variableName);
        String localValue = context.prepare(variableValue);

        for (FilterDeviceState state : input)
        {
            if (localName.equals("symbolic"))
            {
                if (state.getDevice().getSymbolic().equals(localValue))
                {
                    output.addDevice(state);
                }
            }
            else
            {
                for (Variable variable : state.getDevice().getVariables())
                {
                    if (variable.getPrimaryKey().getVariabletype().getName().equals(localName))
                    {
                        if (variable.getValue().equals(localValue))
                        {
                            output.addDevice(state);
                        }
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

package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;

import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-08
 */
public class RemoteCompareFilter extends Filter
{
    private String source = "";
    private String sourceVariable = "";
    private String variable = "";
    private String tagName = "";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("source"))
        {
            source = attributes.get("source");
        }

        if (attributes.containsKey("sourceVariable"))
        {
            sourceVariable = attributes.get("sourceVariable");
        }

        if (attributes.containsKey("variable"))
        {
            variable = attributes.get("variable");
        }

        if (attributes.containsKey("tagName"))
        {
            tagName = attributes.get("tagName");
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        FilterOutput remoteOutput = context.getStoredOutput(context.prepare(source));
        String compare = context.prepare(variable);
        String sourceVar = context.prepare(sourceVariable);

        for (FilterDeviceState state : input)
        {
            String variableValue = null;
            if ("symbolic".equals(compare))
            {
                variableValue = state.getDevice().getSymbolic();
            }
            else
            {
                for (Variable var : state.getDevice().getVariables())
                {
                    if (var.getPrimaryKey().getVariabletype().getName().equals(compare))
                    {
                        variableValue = var.getValue();
                    }
                }
            }

            if (variableValue != null)
            {
                String sourceValue = null;
                for (FilterDeviceState remoteState : remoteOutput)
                {
                    if ("symbolic".equals(sourceVar))
                    {
                        sourceVar = remoteState.getDevice().getSymbolic();
                    }
                    else
                    {
                        for (Variable remoteVar : remoteState.getDevice().getVariables())
                        {
                            if (remoteVar.getPrimaryKey().getVariabletype().getName().equals(sourceVar))
                            {
                                sourceValue = remoteVar.getValue();
                            }
                        }

                    }

                    if (sourceValue != null && sourceValue.equals(variableValue))
                    {
                        state.storeTag(context.prepare(tagName), remoteState);
                        output.addDevice(state);
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

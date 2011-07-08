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

        for (FilterDeviceState state : input)
        {
            for (Variable var : state.getDevice().getVariables())
            {
                if (var.getPrimaryKey().getVariabletype().getName().equals(context.prepare(variable)))
                {
                    for (FilterDeviceState remoteState : remoteOutput)
                    {
                        for (Variable remoteVar : remoteState.getDevice().getVariables())
                        {
                            if (remoteVar.getPrimaryKey().getVariabletype().getName().equals(context.prepare(sourceVariable)))
                            {
                                if (remoteVar.getValue().equals(var.getValue()))
                                {
                                    state.storeTag(context.prepare(tagName), remoteState.getDevice());
                                    output.addDevice(state);
                                }
                            }
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

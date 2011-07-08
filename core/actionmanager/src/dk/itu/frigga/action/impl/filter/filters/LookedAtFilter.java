package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.Filter;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterInput;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-08
 */
public class LookedAtFilter extends Filter
{
    private Variable findVariable(final Set<Variable> haystack, final String needle)
    {
        for (Variable variable : haystack)
        {
            if (variable.getPrimaryKey().getVariabletype().getName().equals(needle))
            {
                return variable;
            }
        }

        return null;
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();
        /*FilterInput eyeTrackers = context.getDevicesByCategory("eyetrack");
        Set<String> lookedAt = new LinkedHashSet<String>();

        for (Device device : eyeTrackers.getDevices())
        {
            Variable variable = findVariable(device.getVariables(), "lookat");
            if (variable != null)
            {
                lookedAt.add(variable.getValue());
            }
        }

        for (Device device : input)
        {
            if (lookedAt.contains(device.getSymbolic()))
            {
                output.addDevice(device);
            }
        }*/

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

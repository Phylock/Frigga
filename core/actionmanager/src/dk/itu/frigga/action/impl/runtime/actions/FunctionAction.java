package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.Parameter;
//import org.apache.felix.ipojo.annotations.Requires;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class FunctionAction extends AbstractAction
{
    private List<Parameter> parameters = Collections.synchronizedList(new ArrayList<Parameter>());
    private String function;
    private String selection = "**";

    public FunctionAction()
    {
    }

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("function"))
        {
            function = attributes.get("function");
        }

        if (attributes.containsKey("selection"))
        {
            selection = attributes.get("selection");
        }
    }

    @Override
    protected void loadChild(String name, Map<String, String> attributes, String contents)
    {
        super.loadChild(name, attributes, contents);

        if ("param".equals(name))
        {
            if (attributes.containsKey("name"))
            {
                parameters.add(new Parameter(attributes.get("name"), contents));
            }
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        String resultStr = "";

        // Magic stuff happens here...
        Set<Device> devices = new LinkedHashSet<Device>();
        parseSelection(context.prepare(selection), devices, context, deviceStates);

        if (devices.size() > 0)
        {
            context.debugMsg("Calling function: " + function + " called.");

            Parameter[] paramCopy = new Parameter[parameters.size()];

            for (Device device : devices)
            {
                int idx = 0;

                for (Parameter param : parameters)
                {
                    paramCopy[idx++] = new Parameter(param.getName(), context.prepare(param.getData().toString(), device));
                }

                FunctionResult localResult = context.getDeviceManager().callFunction(function, new Device[] {device}, paramCopy);
                resultStr = resultStr + localResult.toString();
            }
        }

        return new ActionResult(resultStr);
    }
}

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
    private String[] selection = new String[1];

    public FunctionAction()
    {
        selection[0] = "**";
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
            String sel = attributes.get("selection");
            selection = sel.split("\\.");
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

    private void getAllDevicesRecursivelyFromHere(final FilterDeviceState state, final Collection<Device> devices)
    {
        devices.add(state.getDevice());
        for (String tag : state.getStoredTags())
        {
            getAllDevicesRecursivelyFromHere(state.getStoredDevice(tag), devices);
        }
    }

    private Collection<Device> getAllDevicesRecursivelyFromHere(final FilterOutput output)
    {
        LinkedHashSet<Device> devices = new LinkedHashSet<Device>();

        for (FilterDeviceState state : output)
        {
            getAllDevicesRecursivelyFromHere(state, devices);
        }

        return devices;
    }

    private void parseSelection(final Queue<String> selection, final FilterDeviceState state, final Set<Device> devices)
    {
        String selected = selection.poll();
        Queue<String> localQueue = new LinkedList<String>(selection);
        if (selected != null)
        {
            if (selected.equals("*"))
            {
                for (String tag : state.getStoredTags())
                {
                    FilterDeviceState localState = state.getStoredDevice(tag);
                    devices.add(localState.getDevice());
                    parseSelection(localQueue, localState, devices);
                }
            }
            else if (selected.equals("**"))
            {
                for (String tag : state.getStoredTags())
                {
                    getAllDevicesRecursivelyFromHere(state.getStoredDevice(tag), devices);
                }
            }
            else
            {
                if (state.getStoredTags().contains(selected))
                {
                    FilterDeviceState localState = state.getStoredDevice(selected);
                    devices.add(localState.getDevice());
                    parseSelection(localQueue, localState, devices);
                }
            }
        }
    }

    private void parseSelection(final Set<Device> devices, final FilterContext context)
    {
        Queue<String> localQueue = new LinkedList<String>();
        for (String selector : selection)
        {
            localQueue.add(context.prepare(selector));
        }

        String select = localQueue.poll();

        if (select != null)
        {
            if (select.equals("*"))
            {
                for (FilterDeviceState state : context.getAllOutput())
                {
                    devices.add(state.getDevice());
                    parseSelection(localQueue, state, devices);
                }
            }
            else if (select.equals("**"))
            {
                devices.addAll(getAllDevicesRecursivelyFromHere(context.getAllOutput()));
            }
            else
            {
                FilterOutput output = context.getStoredOutput(select);
                if (output != null)
                {
                    for (FilterDeviceState state : output.matchingDevices())
                    {
                        devices.add(state.getDevice());
                        parseSelection(localQueue, state, devices);
                    }
                }
            }
        }
    }

    private void parseSelection(final Set<Device> devices, final FilterContext context, final Collection<FilterDeviceState> validationSet)
    {
        Queue<String> localQueue = new LinkedList<String>();
        for (String selector : selection)
        {
            localQueue.add(context.prepare(selector));
        }

        String select = localQueue.poll();

        if (select != null)
        {
            if (select.equals("*"))
            {
                for (FilterDeviceState state : validationSet)
                {
                    devices.add(state.getDevice());
                    parseSelection(localQueue, state, devices);
                }
            }
            else if (select.equals("**"))
            {
                for (FilterDeviceState state : validationSet)
                {
                    getAllDevicesRecursivelyFromHere(state, devices);
                }
            }
            else
            {
                for (FilterDeviceState state : validationSet)
                {
                        if (state.getConditionId().equals(select))
                        {
                    devices.add(state.getDevice());
                    parseSelection(localQueue, state, devices);
                  }
                }
            }
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        // Magic stuff happens here...
        Set<Device> devices = new LinkedHashSet<Device>();
        parseSelection(devices, context, deviceStates);

        if (devices.size() > 0)
        {
            context.debugMsg("Calling function: " + function + " called.");

            Parameter[] paramCopy = new Parameter[parameters.size()];
            int idx = 0;

            for (Parameter param : parameters)
            {
                paramCopy[idx++] = new Parameter(param.getName(), context.prepare(param.getData().toString()));
            }

            FunctionResult result = context.getDeviceManager().callFunction(function, devices.toArray(new Device[devices.size()]), paramCopy);
            return new ActionResult(result.toString());
        }

        return new ActionResult();
    }
}

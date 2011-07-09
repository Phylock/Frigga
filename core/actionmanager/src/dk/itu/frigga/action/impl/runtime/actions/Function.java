package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.action.impl.runtime.ExecutableAction;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.utility.XmlHelper;
//import org.apache.felix.ipojo.annotations.Requires;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class Function implements ExecutableAction
{
    private Parameter[] parameters;
    private String function;
    private String[] selection = new String[1];

    public Function()
    {
        selection[0] = "**";
    }

    @Override
    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (!element.hasAttribute("function")) throw new FilterSyntaxErrorException();
        function = element.getAttribute("function");

        if (element.hasAttribute("selection"))
        {
            String sel = element.getAttribute("selection");
            selection = sel.split("\\.");
        }

        List<Parameter> parameters = new LinkedList<Parameter>();

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("param"))
            {
                parameters.add(new Parameter(elem.getAttribute("name"), elem.getTextContent()));
            }
        }

        this.parameters = parameters.toArray(new Parameter[parameters.size()]);
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

    private void parseSelection(final Set<Device> devices, final FilterContext context, final Set<FilterDeviceState> validationSet)
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
                    devices.add(state.getDevice());
                    parseSelection(localQueue, state, devices);
                }
            }
        }
    }

    @Override
    public void execute(final VariableContainer variables, final Collection<FilterDeviceState> deviceStates, final FilterContext context, final Set<FilterDeviceState> validationSet)
    {
        // Magic stuff happens here...
        Set<Device> devices = new LinkedHashSet<Device>();
        parseSelection(devices, context, validationSet);

        if (devices.size() > 0)
        {
            System.out.println("Calling function: " + function + " called.");

            Parameter[] paramCopy = new Parameter[parameters.length];
            int idx = 0;

            for (Parameter param : parameters)
            {
                paramCopy[idx++] = new Parameter(param.getName(), context.prepare(param.getData().toString()));
            }

            context.getDeviceManager().callFunction(function, devices.toArray(new Device[devices.size()]), paramCopy);
        }
    }
}

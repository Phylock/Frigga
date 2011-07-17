package dk.itu.frigga.action.impl.runtime;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-11
 */
public abstract class AbstractAction
{
    private String resultVariable = "";
    protected List<AbstractAction> childActions = Collections.synchronizedList(new LinkedList<AbstractAction>());

    public final void parse(final Element element) throws FilterSyntaxErrorException
    {
        final Map<String, String> attributes = new HashMap<String, String>();
        NamedNodeMap map = element.getAttributes();
        for (int i = 0; i < map.getLength(); i++)
        {
            final Attr attr = (Attr)map.item(i);
            final String name = attr.getName();

            if ("resultVariable".equals(name))
            {
                resultVariable = attr.getValue();
            }
            else
            {
                attributes.put(name, attr.getValue());
            }
        }

        loadAction(attributes);

        if (!canHaveChildActions())
        {
            // Read children as data for the element
            for (Element childElement = XmlHelper.getFirstChildElement(element); childElement != null;
                 childElement = XmlHelper.getNextSiblingElement(childElement))
            {
                final Map<String, String> childAttributes = new HashMap<String, String>();
                NamedNodeMap childMap = childElement.getAttributes();
                for (int i = 0; i < childMap.getLength(); i++)
                {
                    final Attr attr = (Attr)childMap.item(i);
                    childAttributes.put(attr.getName(), attr.getValue());
                }
                loadChild(childElement.getTagName(), childAttributes, childElement.getTextContent());
            }
        }
        else
        {
            ActionFactory factory = new ActionFactory();

            // Read children as data for the element
            for (Element childElement = XmlHelper.getFirstChildElement(element); childElement != null;
                 childElement = XmlHelper.getNextSiblingElement(childElement))
            {
                AbstractAction action = factory.parse(childElement);
                if (action != null)
                {
                    childActions.add(action);
                }
            }
        }
    }

    protected void loadAction(final Map<String, String> attributes)
    {
        // Override this function to perform custom loading.
    }

    protected void loadChild(final String name, final Map<String, String> attributes, final String contents)
    {
        // Override this function to perform custom loading of child attributes.
    }

    public boolean runAgain()
    {
        // Override this function to implement looping functionality
        return false;
    }

    public boolean canHaveChildActions()
    {
        // Override to allow children
        return false;
    }

    public boolean allowRunChildren()
    {
        // Override to disallow children to be run.
        return true;
    }

    public void execute(Collection<FilterDeviceState> devices, FilterContext context)
    {
        do
        {
            ActionResult result = run(devices, context);

            if (!result.isVoid() && !resultVariable.isEmpty())
            {
                context.setVariableValue(resultVariable, result.getValue());
            }

            if (canHaveChildActions() && allowRunChildren())
            {
                for (AbstractAction childAction : childActions)
                {
                    childAction.execute(devices, context);
                }
            }
        }
        while (runAgain());
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

    private void parseSelection(final String[] selection, final Set<Device> devices, final FilterContext context)
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

    protected void parseSelection(final String[] selection, final Set<Device> devices, final FilterContext context, final Collection<FilterDeviceState> validationSet)
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

    protected void parseSelection(final String selection, final Set<Device> devices, final FilterContext context, final Collection<FilterDeviceState> validationSet)
    {
        String[] localSelection = selection.split("\\.");
        parseSelection(localSelection, devices, context, validationSet);
    }

    public abstract ActionResult run(final Collection<FilterDeviceState> deviceStates, final FilterContext context);
}

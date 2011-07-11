package dk.itu.frigga.action.impl.runtime;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
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

    public void parse(final Element element) throws FilterSyntaxErrorException
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
                NamedNodeMap childMap = element.getAttributes();
                for (int i = 0; i < childMap.getLength(); i++)
                {
                    final Attr attr = (Attr)childMap.item(i);
                    childAttributes.put(attr.getName(), attr.getValue());
                }
                loadChild(childElement.getTagName(), childAttributes, childElement.getTextContent());
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
        ActionResult result = run(devices, context);

        if (!result.isVoid() && !resultVariable.isEmpty())
        {
            context.setVariableValue(resultVariable, result.getValue());
        }
    }

    public abstract ActionResult run(final Collection<FilterDeviceState> deviceStates, final FilterContext context);
}

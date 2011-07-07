package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public class ActionContainer
{
    private final Map<String, Action> actions = Collections.synchronizedMap(new HashMap<String, Action>());

    public ActionContainer()
    {
    }

    public void callEvent(final VariableContainer variables, final String event, final Collection<Device> devices)
    {
        if (actions.containsKey(event))
        {
            actions.get(event).execute(variables, devices);
        }
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("action"))
            {
                Action action = new Action();
                action.parse(elem);
                actions.put(action.getEvent(), action);
            }
        }
    }
}

package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionFactory;
import dk.itu.frigga.action.impl.runtime.actions.FunctionAction;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class Action
{
    private String event;
    private final List<AbstractAction> executableActions = Collections.synchronizedList(new LinkedList<AbstractAction>());

    public Action()
    {
    }

    public String getEvent()
    {
        return event;
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (!element.hasAttribute("event")) throw new FilterSyntaxErrorException();
        event = element.getAttribute("event");

        ActionFactory factory = new ActionFactory();

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            AbstractAction action = factory.parse(elem);
            if (action != null)
            {
                executableActions.add(action);
            }
        }
    }

    public void execute(Collection<FilterDeviceState> devices, FilterContext context)
    {
        // Magic stuff happens here...
        System.out.println("Event: " + event + " called.");

        for (AbstractAction action : executableActions)
        {
            action.execute(devices, context);
        }
    }
}

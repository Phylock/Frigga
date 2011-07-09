package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.ExecutableAction;
import dk.itu.frigga.action.impl.runtime.actions.Function;
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
    private final List<ExecutableAction> executableActions = Collections.synchronizedList(new LinkedList<ExecutableAction>());

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

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("function"))
            {
                Function function = new Function();
                function.parse(elem);
                executableActions.add(function);
            }
        }
    }

    public void execute(final VariableContainer variables, Collection<FilterDeviceState> devices, FilterContext context, Set<FilterDeviceState> validationSet)
    {
        // Magic stuff happens here...
        System.out.println("Event: " + event + " called.");

        for (ExecutableAction action : executableActions)
        {
            action.execute(variables, devices, context, validationSet);
        }
    }
}

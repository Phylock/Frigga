package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.device.Device;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class Action
{
    private String event;
    private final List<Function> functions = Collections.synchronizedList(new LinkedList<Function>());

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
                functions.add(function);
            }
        }
    }

    public void execute(final VariableContainer variables, Collection<Device> devices)
    {
        // Magic stuff happens here...
        System.out.println("Event: " + event + " called.");

        for (Function function : functions)
        {
            function.execute(variables, devices);
        }
    }
}

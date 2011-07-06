package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.utility.XmlHelper;
import org.apache.felix.ipojo.annotations.Requires;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public class Function
{
    private Parameter[] parameters;
    private String function;

    //@Requires
    private DeviceManager deviceManager;

    public Function()
    {
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (!element.hasAttribute("function")) throw new FilterSyntaxErrorException();
        function = element.getAttribute("function");

        List<Parameter> parameters = new LinkedList<Parameter>();

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("param"))
            {
                parameters.add(new Parameter(elem.getAttribute("name"), elem.getTextContent()));
            }
        }

        this.parameters = (Parameter[])parameters.toArray();
    }

    public void execute(final VariableContainer variables, Collection<Device> devices)
    {
        // Magic stuff happens here...
        System.out.println("Calling function: " + function + " called.");
        deviceManager.callFunction(function, (Device[]) devices.toArray(), parameters);
    }
}

package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.utility.XmlHelper;
//import org.apache.felix.ipojo.annotations.Requires;
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

        this.parameters = parameters.toArray(new Parameter[parameters.size()]);
    }

    public void execute(final VariableContainer variables, Collection<FilterDeviceState> devices, FilterContext context)
    {
        // Magic stuff happens here...
        System.out.println("Calling function: " + function + " called.");

        Parameter[] paramCopy = new Parameter[parameters.length];
        int idx = 0;

        for (Parameter param : parameters)
        {
            paramCopy[idx++] = new Parameter(param.getName(), context.prepare(param.getData().toString()));
        }

        FilterDeviceState[] states = devices.toArray(new FilterDeviceState[devices.size()]);
        Device[] d = new Device[devices.size()];
        for (int i = 0; i < devices.size(); i++)
        {
            d[i] = states[i].getDevice();
        }
        context.getDeviceManager().callFunction(function, d, paramCopy);
    }
}

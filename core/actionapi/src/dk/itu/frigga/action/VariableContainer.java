package dk.itu.frigga.action;

import dk.itu.frigga.action.block.Variable;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public class VariableContainer
{
    private final Map<String, Variable> variables = Collections.synchronizedMap(new HashMap<String, Variable>());

    public VariableContainer()
    {
    }

    public void addVariable(final Variable variable)
    {
        variables.put(variable.getName(), variable);
    }

    public Variable getVariable(final String name)
    {
        return variables.get(name);
    }

    public void removeVariable(final String name)
    {
        variables.remove(name);
    }

    public Set<String> getVariableNames()
    {
        return variables.keySet();
    }

    public Collection<Variable> getVariables()
    {
        return variables.values();
    }

    public void parse(final Element element)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");

        if (element.getTagName().equals("variables"))
        {
            for (Element elemReplacement = XmlHelper.getFirstChildElement(element, "variable"); elemReplacement != null; elemReplacement = XmlHelper.getNextSiblingElement(elemReplacement, "variable"))
            {
                //Variable variable = new Variable();
                //variable.
                //String id = "";

                // TODO(toan@itu.dk): Add syntax check, id attribute is required.
                //if (elemReplacement.hasAttribute("id")) id = elemReplacement.getAttribute("id");

                //addRule(new Replacement(name, description, type));
            }
        }
    }
}

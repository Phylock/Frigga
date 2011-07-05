/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

/**
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Rule
{
    private String description;
    private String id;
    private final VariableContainer variableContainer = new VariableContainer();
    private final ActionContainer actionContainer = new ActionContainer();
    // TODO: Use the FilterManager class as argument here.
    private final ConditionContainer conditionContainer = new ConditionContainer(null /* FilterFactory, this needs to be not null */);

    public String getDescription()
    {
        return description;
    }

    public String getId()
    {
        return id;
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (!element.hasAttribute("id")) throw new FilterSyntaxErrorException();
        id = element.getAttribute("id");

        if (element.hasAttribute("description")) description = element.getAttribute("description");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("variables"))
            {
                variableContainer.parse(elem);
            }

            else if (elem.getTagName().equals("actions"))
            {
                actionContainer.parse(elem);
            }

            else if (elem.getTagName().equals("condition"))
            {
                conditionContainer.parse(elem);
            }
        }
    }
}

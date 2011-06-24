/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

/**
 * @author phylock
 */
public class Rule
{
    private String description;
    private String id;
    private final VariableContainer variableContainer = new VariableContainer();
    private final ActionContainer actionContainer = new ActionContainer();
    private final ConditionContainer conditionContainer = new ConditionContainer();

    public String getDescription()
    {
        return description;
    }

    public String getId()
    {
        return id;
    }

    public void parse(final Element element)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (element.hasAttribute("id")) id = element.getAttribute("id");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("description"))
            {
                description = elem.getTextContent();
            }

            else if (elem.getTagName().equals("variables"))
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

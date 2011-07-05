package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public class RuleContainer
{
    private final Map<String, Rule> rules = Collections.synchronizedMap(new HashMap<String, Rule>());

    public RuleContainer()
    {

    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");

        if (element.getTagName().equals("rules"))
        {
            for (Element elemReplacement = XmlHelper.getFirstChildElement(element, "rule"); elemReplacement != null; elemReplacement = XmlHelper.getNextSiblingElement(elemReplacement, "rule"))
            {
                Rule rule = new Rule();
                rule.parse(elemReplacement);
                addRule(rule);
            }
        }
    }

    public void addRule(final Rule rule)
    {
        String id = rule.getId();
        if (id.isEmpty())
        {
            id = UUID.randomUUID().toString();
        }

        rules.put(id, rule);
    }

    public Rule getRule(final String id)
    {
        return rules.get(id);
    }

    public void removeRule(final String id)
    {
        rules.remove(id);
    }

    public Set<String> getRuleIds()
    {
        return rules.keySet();
    }

    public Collection<Rule> getRules()
    {
        return rules.values();
    }
}

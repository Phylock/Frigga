package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterFactory;
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
    private final FilterFactory factory;
    private final ReplacementContainer replacementContainer;
    private final Map<String, Rule> rules = Collections.synchronizedMap(new HashMap<String, Rule>());

    public RuleContainer(final FilterFactory filterFactory, final ReplacementContainer replacementContainer)
    {
        factory = filterFactory;
        this.replacementContainer = replacementContainer;
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");

        if (element.getTagName().equals("rules"))
        {
            for (Element elemRule = XmlHelper.getFirstChildElement(element, "rule"); elemRule != null; elemRule = XmlHelper.getNextSiblingElement(elemRule, "rule"))
            {
                Rule rule = new Rule(factory, replacementContainer);
                rule.parse(elemRule);
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

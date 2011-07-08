package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.Replacement;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public class ReplacementContainer
{
    private final Map<String, Replacement> replacements = Collections.synchronizedMap(new HashMap<String, Replacement>());

    public ReplacementContainer()
    {
    }

    public void addReplacement(final Replacement replacement)
    {
        if (replacement == null) throw new IllegalArgumentException("Argument 'replacement' is null.");

        replacements.put(replacement.getName(), replacement);
    }

    public void removeReplacement(final Replacement replacement)
    {
        replacements.remove(replacement.getName());
    }

    public void removeAllReplacements()
    {
        replacements.clear();
    }

    public Replacement getReplacement(final String name)
    {
        return replacements.get(name);
    }

    public boolean hasReplacement(final String name)
    {
        return replacements.containsKey(name);
    }

    public Set<String> getReplacementNames()
    {
        return replacements.keySet();
    }

    public Collection<Replacement> getReplacements()
    {
        return Collections.unmodifiableCollection(replacements.values());
    }

    public void parse(final Element element)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");

        if (element.getTagName().equals("replacements"))
        {
            for (Element elemReplacement = XmlHelper.getFirstChildElement(element, "replace"); elemReplacement != null; elemReplacement = XmlHelper.getNextSiblingElement(elemReplacement, "replace"))
            {
                String name = "";
                String description = "";
                String type = "string";

                // TODO(toan@itu.dk): Add syntax check, name attribute is required.
                if (elemReplacement.hasAttribute("name")) name = elemReplacement.getAttribute("name");
                if (elemReplacement.hasAttribute("description")) description = elemReplacement.getAttribute("description");
                if (elemReplacement.hasAttribute("type")) type = elemReplacement.getAttribute("type");

                addReplacement(new Replacement(name, description, type));
            }
        }
    }
}

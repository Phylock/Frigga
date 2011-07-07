package dk.itu.frigga.action.impl.manager.template;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-08
 */
public class Info
{
    private List<Author> authors = new LinkedList<Author>();
    private String name;
    private String site;
    private String description;

    public Info(final String author, final String name, final String site, final String description)
    {
        this.authors.add(new Author(name));
        this.name = name;
        this.site = site;
        this.description = description;
    }

    public Info(final Element element)
    {
        if (element.getTagName().equals("info"))
        {
            for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            {
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element elem = (Element)node;

                    if (elem.getTagName().equals("author"))
                    {
                        authors.add(new Author(elem));
                    }
                    else if (elem.getTagName().equals("name"))
                    {
                        name = elem.getTextContent();
                    }
                    else if (elem.getTagName().equals("site"))
                    {
                        site = elem.getTextContent();
                    }
                    else if (elem.getTagName().equals("description"))
                    {
                        description = elem.getTextContent();
                    }
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("element has to be named info.");
        }
    }

    @Override
    public String toString()
    {
        String authorStr = "";
        for (Author author : authors)
        {
            if (authorStr.length() > 0) authorStr = authorStr + ", ";
            authorStr = authorStr + author.toString();
        }

        return "Template: " + name + "\n" +
               "  author(s):   " + authorStr + "\n" +
               "  site:        " + site + "\n" +
               "  description: " + description;
    }
}

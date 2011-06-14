package dk.itu.frigga.action.manager.template;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-08
 */
public class Info
{
    private Author author;
    private String name;
    private String site;
    private String description;

    public Info(final String author, final String name, final String site, final String description)
    {
        this.author = new Author(name);
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
                        author = new Author(elem);
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
        return "Template: " + name + "\n" +
                "  author:      " + author + "\n" +
                "  site:        " + site + "\n" +
                "  description: " + description;
    }
}

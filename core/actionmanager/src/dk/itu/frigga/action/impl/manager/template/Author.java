package dk.itu.frigga.action.impl.manager.template;

import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-08
 */
public class Author
{
    private final String author;

    public Author(final String author)
    {
        this.author = author;
    }

    public Author(final Element element)
    {
        if (element.getTagName().equals("author"))
        {
            this.author = element.getTextContent();
        }

        throw new IllegalArgumentException("element must be of type author.");
    }

    @Override
    public String toString()
    {
        return author;
    }
}

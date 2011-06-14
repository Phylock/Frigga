package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.runtime.Executable;
import dk.itu.frigga.action.runtime.Selection;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public abstract class Filter
{
    protected String name = "";
    protected FilterContainer filterContainer;

    public Filter()
    {
    }

    protected void setName(final String name)
    {
        this.name = name;

        if (!name.isEmpty() && filterContainer != null)
        {
            filterContainer.registerNamedFilter(this);
        }
    }

    public boolean hasId()
    {
        return !name.isEmpty();
    }

    public String getId()
    {
        return name;
    }

    public void setContainer(final FilterContainer container)
    {
        filterContainer = container;

        if (!name.isEmpty())
        {
            filterContainer.registerNamedFilter(this);
        }
    }

    public void parse(final FilterFactory factory, Element element)
    {
        if (element.hasAttribute("id"))
        {
            setName(element.getAttribute("id"));
        }
    }

    public abstract List<Selection> run() throws FilterFailedException;
}

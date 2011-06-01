package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.runtime.Executable;
import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public abstract class Filter implements Executable
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

    @Override
    public boolean hasId()
    {
        return !name.isEmpty();
    }

    @Override
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
}

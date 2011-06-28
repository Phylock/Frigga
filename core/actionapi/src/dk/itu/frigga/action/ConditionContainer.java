package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.*;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-24
 */
public class ConditionContainer implements FilterContainer
{
    private final List<Filter> conditions = Collections.synchronizedList(new LinkedList<Filter>());
    private final Map<String, Filter> namedFilters = Collections.synchronizedMap(new HashMap<String, Filter>());
    private final FilterFactory factory;

    public ConditionContainer(final FilterFactory factory)
    {
        this.factory = factory;
    }

    public void parse(final Element element)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");
        if (!element.getTagName().equals("condition")) throw new IllegalArgumentException("Argument 'element' is not at the condition element.");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            try
            {
                conditions.add(factory.createFilter(this, elem));
            }
            catch (UnknownFilterException e)
            {
                // An unknown filter is used, not much we can do about that here.
            }
            catch (FilterInstantiationFailedException e)
            {
                // Either the correct constructor was unavailable or an exception was throw during construction.
            }
        }
    }

    public void addFilter(final Filter filter)
    {
        if (filter == null) throw new IllegalArgumentException("Argument 'filter' is null.");

        conditions.add(filter);
    }

    public void removeFilter(final Filter filter)
    {
        if (filter == null) throw new IllegalArgumentException("Argument 'filter' is null.");

        conditions.remove(filter);
    }

    public boolean hasFilter(final Filter filter)
    {
        if (filter == null) throw new IllegalArgumentException("Argument 'filter' is null.");

        return conditions.contains(filter);
    }

    public Filter[] getFilters()
    {
        return (Filter[])conditions.toArray();
    }

    @Override
    public void registerNamedFilter(final Filter filter) throws NotANamedFilterException
    {
        if (filter == null) throw new IllegalArgumentException("Argument 'filter' is null.");

        namedFilters.put(filter.getId(), filter);
    }

    @Override
    public void unregisterNamedFilter(final Filter filter) throws FilterNotFoundException
    {
        if (filter == null) throw new IllegalArgumentException("Argument 'filter' is null.");

        namedFilters.remove(filter.getId());
    }

    @Override
    public Filter getNamedFilter(final String name) throws FilterNotFoundException
    {
        if (name == null) throw new IllegalArgumentException("Argument 'name' is null.");

        return namedFilters.get(name);
    }
}

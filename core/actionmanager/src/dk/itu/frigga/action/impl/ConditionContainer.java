package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterInstantiationFailedException;
import dk.itu.frigga.action.filter.FilterNotFoundException;
import dk.itu.frigga.action.filter.NotANamedFilterException;
import dk.itu.frigga.action.filter.UnknownFilterException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.action.impl.filter.filters.EmptyFilter;
import dk.itu.frigga.device.DeviceManager;
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
    private final Filter rootFilter = new EmptyFilter();
    private final Map<String, Filter> namedFilters = Collections.synchronizedMap(new HashMap<String, Filter>());

    public ConditionContainer()
    {
    }

    public Filter getRootFilter()
    {
        return rootFilter;
    }

    public void parse(final Element element, final FilterFactory factory)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");
        if (!element.getTagName().equals("condition")) throw new IllegalArgumentException("Argument 'element' is not at the condition element.");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            try
            {
                rootFilter.addFilter(factory.createFilter(this, elem));
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

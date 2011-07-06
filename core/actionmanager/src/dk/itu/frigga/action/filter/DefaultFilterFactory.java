package dk.itu.frigga.action.filter;

import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class DefaultFilterFactory implements FilterFactory
{
    private static final int PARSER_VERSION = 1;

    private final Map<String, Class<? extends Filter>> filterTypes = Collections.synchronizedMap(new HashMap<String, Class<? extends Filter>>());
    private final List<FilterTypeRequestListener> filterTypeRequestListeners = Collections.synchronizedList(new LinkedList<FilterTypeRequestListener>());

    @Override
    public void registerFilterType(final String type, final Class<? extends Filter> filterClass)
    {
        filterTypes.put(type, filterClass);
    }

    private Class<? extends Filter> requestFilterType(final String type) throws UnknownFilterException
    {
        if (!filterTypes.containsKey(type))
        {
            for (FilterTypeRequestListener filterTypeRequestListener : filterTypeRequestListeners)
            {
                filterTypeRequestListener.requestFilterType(type, this);
            }
        }

        if (filterTypes.containsKey(type))
        {
            return filterTypes.get(type);
        }

        throw new UnknownFilterException();
    }

    @Override
    public Filter createFilter(final FilterContainer owner, final String type) throws UnknownFilterException, FilterInstantiationFailedException
    {
        Class<? extends Filter> filterClass = requestFilterType(type);

        try
        {
            try
            {
                return filterClass.getConstructor(FilterContainer.class).newInstance(owner);
            }
            catch (NoSuchMethodException e)
            {
                Filter f = filterClass.newInstance();
                f.setContainer(owner);
                return f;
            }
        }
        catch (InstantiationException e)
        {
            // Unable to find compatible filter, run default action.
        }
        catch (IllegalAccessException e)
        {
            // The constructor is private, so we can not access it, run default action.
        }
        catch (InvocationTargetException e)
        {
            // The constructor threw an exception
            throw new FilterInstantiationFailedException(e.getTargetException());
        }

        throw new UnknownFilterException();
    }

    @Override
    public Filter createFilter(final FilterContainer owner, final Element element) throws UnknownFilterException, FilterInstantiationFailedException
    {
        Filter f = createFilter(owner, element.getTagName());
        f.parse(this, element);

        return f;
    }

    @Override
    public boolean isConstraint(final Element element)
    {
        return element.getTagName().equals("constraint");
    }

    @Override
    public boolean constraintPassed(final Element element)
    {
        boolean result = true;

        if (element.hasAttribute("minVersion"))
        {
            if (parseInt(element.getAttribute("minVersion"), 10) > PARSER_VERSION)
            {
                result = false;
            }
        }

        if (result && element.hasAttribute("maxVersion"))
        {
            if (parseInt(element.getAttribute("maxVersion"), 10) < PARSER_VERSION)
            {
                result = false;
            }
        }

        if (result && element.hasAttribute("hasFilters"))
        {
            String[] filters = element.getAttribute("hasFilters").split("\\s");
            for (String filter : filters)
            {
                if (!filterTypes.containsKey(filter))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public void addFilterTypeRequestListener(FilterTypeRequestListener listener)
    {
        filterTypeRequestListeners.add(listener);
    }

    @Override
    public void removeFilterTypeRequestListener(FilterTypeRequestListener listener)
    {
        filterTypeRequestListeners.remove(listener);
    }
}
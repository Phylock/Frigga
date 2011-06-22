package dk.itu.frigga.action.manager.filter;

import dk.itu.frigga.action.filter.*;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.*;


/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public class FilterManager implements FilterFactory
{
    private static final int PARSER_VERSION = 1;

    private final Map<String, Class<? extends Filter>> filterTypes = Collections.synchronizedMap(new HashMap<String, Class<? extends Filter>>());

    @Override
    public void registerFilterType(final String type, final Class<? extends Filter> filterClass)
    {
        filterTypes.put(type, filterClass);
    }

    @Override
    public Filter createFilter(final FilterContainer owner, final String type) throws UnknownFilterException, FilterInstantiationFailedException
    {
        if (filterTypes.containsKey(type))
        {
            try
            {
                try
                {
                    return filterTypes.get(type).getConstructor(FilterContainer.class).newInstance(new FilterContainer[]{owner});
                }
                catch (NoSuchMethodException e)
                {
                    Filter f = filterTypes.get(type).newInstance();
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
}

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
    public static class RootFilterInformation
    {
        private final String name;
        private final String dependsOn;
        private final Filter filter;
        private final boolean pipeInput;

        public RootFilterInformation(final String name, final String dependsOn, final Filter filter, final boolean pipeInput)
        {
            this.name = name;
            this.dependsOn = dependsOn;
            this.filter = filter;
            this.pipeInput = pipeInput;
        }

        public String getName()
        {
            return name;
        }

        public String getDependsOn()
        {
            return dependsOn;
        }

        public Filter getFilter()
        {
            return filter;
        }

        public boolean isPipeInput()
        {
            return pipeInput;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RootFilterInformation that = (RootFilterInformation) o;

            if (pipeInput != that.pipeInput) return false;
            if (!dependsOn.equals(that.dependsOn)) return false;
            if (!filter.equals(that.filter)) return false;
            if (!name.equals(that.name)) return false;

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = name.hashCode();
            result = 31 * result + dependsOn.hashCode();
            result = 31 * result + filter.hashCode();
            result = 31 * result + (pipeInput ? 1 : 0);
            return result;
        }
    }

    private final Filter rootFilter = new EmptyFilter();
    private final Map<String, RootFilterInformation> rootFilters = Collections.synchronizedMap(new HashMap<String, RootFilterInformation>());
    private final Map<String, Filter> namedFilters = Collections.synchronizedMap(new HashMap<String, Filter>());

    public ConditionContainer()
    {
    }

    public Collection<RootFilterInformation> getPrioritizedFilterList()
    {
        LinkedList<String> resolvedList = new LinkedList<String>();

        for (RootFilterInformation rootFilter : rootFilters.values())
        {
            addDependencyResolvedFilter(rootFilter, resolvedList, 64);
        }

        ArrayList<RootFilterInformation> resolvedFilters = new ArrayList<RootFilterInformation>(resolvedList.size());

        for (String filter : resolvedList)
        {
            resolvedFilters.add(rootFilters.get(filter));
        }

        return resolvedFilters;
    }

    public void addDependencyResolvedFilter(final RootFilterInformation filter, LinkedList<String> filters, int allowedDepth)
    {
        int depth = allowedDepth - 1;

        if (depth >= 0)
        {
            if (!filters.contains(filter.name))
            {
                if (filter.dependsOn.isEmpty())
                {
                    filters.addFirst(filter.name);
                }
                else
                {
                    if (filters.contains(filter.dependsOn))
                    {
                        filters.add(filters.indexOf(filter.dependsOn) + 1, filter.getName());
                    }
                    else
                    {
                        addDependencyResolvedFilter(rootFilters.get(filter.dependsOn), filters, depth);
                    }
                }
            }
        }
    }

    public Filter getRootFilter()
    {
        return rootFilter;
    }

    public void parse(final Element element, final FilterFactory factory)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null.");
        if (!element.getTagName().equals("condition")) throw new IllegalArgumentException("Argument 'element' is not at the condition element.");

        String name = UUID.randomUUID().toString();
        String dependsOn = "";
        boolean pipeInput = false;
        Filter filter = new EmptyFilter();

        if (element.hasAttribute("id"))
        {
            name = element.getAttribute("id");
        }

        if (element.hasAttribute("dependsOn"))
        {
            dependsOn = element.getAttribute("dependsOn");
        }

        if (element.hasAttribute("pipeInput"))
        {
            pipeInput = Boolean.parseBoolean(element.getAttribute("pipeInput"));
        }

        rootFilters.put(name, new RootFilterInformation(name, dependsOn, filter, pipeInput));

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

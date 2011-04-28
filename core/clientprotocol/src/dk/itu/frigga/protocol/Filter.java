package dk.itu.frigga.protocol;

import java.util.Arrays;

/**
 * The filter class is used to select a target for a request or an option. A
 * filter consists of 0 or more filter elements. A filter element is a rule
 * determining which targets are to be selected from a given group. By adding
 * more filter elements we are able to narrow the selection, and thereby
 * ensuring that the devices we select are the desired devices.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Filter
{
    private final FilterElement[] elements;

    public Filter(final String filter) throws InvalidFilterGroupException, InvalidFilterException
    {
        elements = parse(filter);
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();

        for (FilterElement element : elements)
        {
            str.append('/');
            str.append(element.toString());
        }

        return str.toString();
    }

    private FilterElement[] parse(final String filter) throws InvalidFilterGroupException, InvalidFilterException
    {
        final String[] elementStrings = filter.split("/");
        final FilterElement[] elementObjects;

        if (elementStrings.length > 0)
        {
            final boolean isAbsolute = true;//elementStrings[0].isEmpty();
            final int size = (isAbsolute) ? elementStrings.length - 1 : elementStrings.length;
            final int offset = (isAbsolute) ? 1 : 0;

            elementObjects = new FilterElement[size];

            for (int i = 0; i < size; i++)
            {
                elementObjects[i] = new FilterElement(elementStrings[i + offset]);
            }

            // Check to see if the filter is correctly written in the order
            // more general to less general.
            int previous = 0;
            for (int i = 1; i < size; i++)
            {
                if (elementObjects[previous].compareTo(elementObjects[i]) >= 0)
                {
                    throw new InvalidFilterException(filter, "order of filter groups is not correct, '" +
                            elementObjects[previous] + "' should be after '" + elementObjects[i] + "'");
                }

                previous = i;
            }
        }
        else
        {
            throw new InvalidFilterException(filter);
        }

        return elementObjects;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Filter)) return false;

        Filter filter = (Filter) o;

        if (!Arrays.equals(elements, filter.elements)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(elements);
    }
}

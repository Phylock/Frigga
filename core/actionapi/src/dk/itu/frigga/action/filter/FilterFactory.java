package dk.itu.frigga.action.filter;

import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public interface FilterFactory
{
    /**
     * Registers a filter class of a specified type with the factory class.
     *
     * Once a filter type is registered it can be instantiated by parsing a template where an element with the same
     * name as the filter type is used.
     *
     * @param type The name of the filter type, the name can not contain spaces or other special characters.
     * @param filterClass The class to instantiate to give a type of the specified filter.
     */
    public void registerFilterType(final String type, final Class<? extends Filter> filterClass);

    /**
     * Creates a filter of a specified type, and registers a filter container with it.
     *
     * This function creates an empty filter of a given type, and registers a filter container with it. The filter will
     * not be instantiated with data. Call the parse function to do that.
     *
     * @param owner The container to register with this filter.
     * @param type The filter type to create.
     * @return A newly created instance of the specified filter type.
     * @throws UnknownFilterException If the filter is not of a known type, or the filter functions are not following
     *         the correct style, this exception is thrown.
     * @throws FilterInstantiationFailedException If the filter failed during creation this exception is thrown.
     */
    public Filter createFilter(final FilterContainer owner, final String type) throws UnknownFilterException, FilterInstantiationFailedException;

    /**
     * Creates a filter of a specified type, and registers a filter container with it.
     *
     * This function creates a parsed filter of a given type, and registers a filter container with it. The filter will
     * be instantiated with data.
     *
     * @param owner The container to register with this filter.
     * @param element The element to parse, the filter type will be extracted from here.
     * @return A newly created instance of the specified filter type.
     * @throws UnknownFilterException If the filter is not of a known type, or the filter functions are not following
     *         the correct style, this exception is thrown.
     * @throws FilterInstantiationFailedException If the filter failed during creation this exception is thrown.
     */
    public Filter createFilter(final FilterContainer owner, final Element element) throws UnknownFilterException, FilterInstantiationFailedException;
}

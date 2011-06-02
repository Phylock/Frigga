package dk.itu.frigga.action.manager.filter;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.action.filter.FilterContainer;
import dk.itu.frigga.action.filter.FilterFactory;
import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public class HasVariableFilter extends DbFilter
{
    private String attrName;
    private String attrType = "string";

    public HasVariableFilter()
    {
    }

    public HasVariableFilter(FilterContainer filterContainer)
    {
        super(filterContainer);
    }

    @Override
    public void parse(FilterFactory factory, Element element)
    {
        super.parse(factory, element);

        if (element.hasAttribute("name"))
        {
            attrName = element.getAttribute("name");
        }

        if (element.hasAttribute("type"))
        {
            attrType = element.getAttribute("type");
        }
    }

    @Override
    public void execute(RuntimeContext context, ConditionResult result) throws ExecutableException
    {

    }
}

package dk.itu.frigga.action.impl.runtime;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Set;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-09
 */
public interface ExecutableAction
{
    void parse(Element element) throws FilterSyntaxErrorException;

    void execute(VariableContainer variables, Collection<FilterDeviceState> devices, FilterContext context, Set<FilterDeviceState> validationSet);
}

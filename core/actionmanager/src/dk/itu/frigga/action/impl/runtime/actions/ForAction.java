package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;
import dk.itu.frigga.action.impl.runtime.InvalidVariableTypeException;
import dk.itu.frigga.action.impl.runtime.TemplateVariable;

import java.util.Collection;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-12
 */
public class ForAction extends AbstractAction
{
    private String variableName = "";
    private String from;
    private String to;
    private String step = "1";
    private boolean repeat = false;

    @Override
    public boolean runAgain()
    {
        return repeat;
    }

    @Override
    public boolean canHaveChildActions()
    {
        return true;
    }

    @Override
    public boolean allowRunChildren()
    {
        return true;
    }

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("variable"))
        {
            variableName = attributes.get("variable");
        }

        if (attributes.containsKey("from"))
        {
            from = attributes.get("from");
        }

        if (attributes.containsKey("to"))
        {
            to = attributes.get("to");
        }

        if (attributes.containsKey("step"))
        {
            step = attributes.get("step");
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        String localVariableName = context.prepare(variableName);
        int localFrom = parseInt(context.prepare(from), 10);
        int localTo = parseInt(context.prepare(to), 10);
        int localStep = parseInt(context.prepare(step), 10);

        if (!context.hasVariable(localVariableName))
        {
            localStep = 0;
            TemplateVariable var = new TemplateVariable(localVariableName);
            var.set((double)localFrom);
            context.addVariable(var);
        }

        TemplateVariable.Value value = context.getVariableValue(localVariableName);
        try
        {
            int localValue = (int)value.asNumber();
            localValue = localValue + localStep;
            context.setVariableValue(localVariableName, (double)localValue);

            repeat = localValue <= localTo;
        }
        catch (InvalidVariableTypeException e)
        {
            //
        }

        return new ActionResult();
    }
}

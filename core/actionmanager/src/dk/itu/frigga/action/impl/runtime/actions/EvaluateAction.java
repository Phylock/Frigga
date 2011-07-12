package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collection;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-12
 */
public class EvaluateAction extends AbstractAction
{
    private String expression = "";


    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("expression"))
        {
            expression = attributes.get("expression");
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        String localExpression = context.prepare(expression);
        String result = "";

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try
        {
            result = String.valueOf(engine.eval(localExpression));
        }
        catch (ScriptException e)
        {
            context.reportError("Error with expression: " + expression + "\nMessage: " + e.getMessage());
        }

        return new ActionResult(result);
    }
}

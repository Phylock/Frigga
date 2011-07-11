package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.lang.Long.parseLong;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-10
 */
public class SleepAction extends AbstractAction
{
    private String sleepDelay = "0";

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("delay"))
        {
            sleepDelay = attributes.get("delay");
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        try
        {
            Thread.sleep(parseLong(context.prepare(sleepDelay), 10));
        }
        catch (InterruptedException e)
        {
            //
            context.debugMsg("Sleep interrupted.");
        }

        return new ActionResult();
    }
}

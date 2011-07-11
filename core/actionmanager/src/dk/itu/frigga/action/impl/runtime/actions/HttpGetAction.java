package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-11
 */
public class HttpGetAction extends AbstractAction
{
    private String url = "";

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("url"))
        {
            url = attributes.get("url");
        }
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        try
        {
            URL localUrl = new URL(context.prepare(url));
            return new ActionResult((String)localUrl.getContent());
        }
        catch (MalformedURLException e)
        {
            context.reportError("Error, malformed URL: "  + e);
        }
        catch (IOException e)
        {
            context.reportError("Error, IO error: " + e);
        }

        return new ActionResult("");
    }
}

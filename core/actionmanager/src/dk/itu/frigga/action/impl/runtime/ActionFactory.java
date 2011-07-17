package dk.itu.frigga.action.impl.runtime;

import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.runtime.actions.*;
import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-12
 */
public class ActionFactory
{
    public ActionFactory()
    {
    }

    public AbstractAction parse(final Element element) throws FilterSyntaxErrorException
    {
        if ("function".equals(element.getTagName()))
        {
            AbstractAction action = new FunctionAction();
            action.parse(element);
            return action;
        }

        if ("openDialog".equals(element.getTagName()))
        {
            AbstractAction action = new OpenDialogAction();
            action.parse(element);
            return action;
        }

        if ("sleep".equals(element.getTagName()))
        {
            AbstractAction action = new SleepAction();
            action.parse(element);
            return action;
        }

        if ("httpGet".equals(element.getTagName()))
        {
            AbstractAction action = new HttpGetAction();
            action.parse(element);
            return action;
        }

        if ("for".equals(element.getTagName()))
        {
            AbstractAction action = new ForAction();
            action.parse(element);
            return action;
        }

        if ("evaluate".equals(element.getTagName()))
        {
            AbstractAction action = new EvaluateAction();
            action.parse(element);
            return action;
        }

        return null;
    }
}

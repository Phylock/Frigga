package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.Replacement;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.action.TemplateInstance;
import dk.itu.frigga.action.filter.FilterFailedException;

import java.util.Collection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public class TemplateInstanceImpl implements TemplateInstance
{
    private Template template;

    @Override
    public Template getTemplate()
    {
        return template;
    }

    @Override
    public Collection<Replacement> getReplacements()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void run() throws FilterFailedException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

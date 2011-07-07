package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.TemplateInfo;
import dk.itu.frigga.action.TemplateManager;
import dk.itu.frigga.action.TemplateNotFoundException;
import dk.itu.frigga.action.impl.filter.FilterFailedException;
//import org.apache.felix.ipojo.annotations.Component;
//import org.apache.felix.ipojo.annotations.Instantiate;
//import org.apache.felix.ipojo.annotations.Provides;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
//@Component
//@Provides
//@Instantiate
public class TemplateContainer implements TemplateManager
{
    private final Set<Template> templates = Collections.synchronizedSet(new LinkedHashSet<Template>());

    public TemplateContainer()
    {
    }

    @Override
    public int getTemplateCount()
    {
        return templates.size();
    }

    @Override
    public TemplateInfo getTemplateInfo(final int index) throws TemplateNotFoundException
    {
        Iterator<Template> iterator = templates.iterator();

        int left = index;
        while (iterator.hasNext())
        {
            Template template = iterator.next();
            if (left == 0)
            {
                return template.getTemplateInfo();
            }

            left--;
        }

        throw new TemplateNotFoundException();
    }

    @Override
    public void run() throws FilterFailedException
    {
        for (Template template : templates)
        {
            template.run();
        }
    }
}

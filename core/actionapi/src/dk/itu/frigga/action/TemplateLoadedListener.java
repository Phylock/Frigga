package dk.itu.frigga.action;

import java.util.Collection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public interface TemplateLoadedListener
{
    void templateLoaded(TemplateManager manager, Template template) throws IgnoreTemplateException;
}

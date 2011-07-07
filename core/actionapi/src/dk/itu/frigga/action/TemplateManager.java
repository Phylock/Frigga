package dk.itu.frigga.action;

import dk.itu.frigga.action.impl.filter.FilterFailedException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public interface TemplateManager
{
    int getTemplateCount();
    TemplateInfo getTemplateInfo(int index) throws TemplateNotFoundException;

    void run() throws FilterFailedException;
}

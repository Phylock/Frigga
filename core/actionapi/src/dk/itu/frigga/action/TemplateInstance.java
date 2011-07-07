package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterFailedException;

import java.util.Collection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public interface TemplateInstance
{
    Template getTemplate();
    Collection<Replacement> getReplacements();
    void run() throws FilterFailedException;
}

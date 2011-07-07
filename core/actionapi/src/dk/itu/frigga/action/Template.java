package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterFailedException;

import java.util.Collection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public interface Template
{
    void run() throws FilterFailedException;

    TemplateInfo getTemplateInfo();

    Collection<Replacement> getReplacements();
}

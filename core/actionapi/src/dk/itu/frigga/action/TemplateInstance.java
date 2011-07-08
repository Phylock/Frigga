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
    String getName();
    Template getTemplate();
    Collection<Replacement> getReplacements();
    void setReplacementValue(final Replacement replacement, final String value);
    void setReplacementValue(final String replacement, final String value);
    String getReplacementValue(final Replacement replacement);
    String getReplacementValue(final String replacement);
    void run() throws FilterFailedException;
}

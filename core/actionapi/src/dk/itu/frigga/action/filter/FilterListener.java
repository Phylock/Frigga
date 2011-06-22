package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.runtime.Selection;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public interface FilterListener
{
    void FilterNameChanged(final Filter filter, final String oldName, final String newName);
    void FilterBeforeRun(final Filter filter);
    void FilterAfterRun(final Filter filter, final List<Selection> result);
}

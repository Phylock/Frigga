package dk.itu.frigga.action.filter;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public interface FilterListener
{
    void FilterNameChanged(final Filter filter, final String oldName, final String newName);
    void FilterBeforeRun(final Filter filter, final FilterContext context, final FilterInput input);
    void FilterAfterRun(final Filter filter, final FilterContext context, final FilterOutput output);
}

package dk.itu.frigga.action.runtime;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public interface VariableChangedListener
{
    void variableValueChanged(final TemplateVariable variable, final TemplateVariable.Value newValue, final TemplateVariable.Value oldValue);
    void variableUnset(final TemplateVariable variable);
}

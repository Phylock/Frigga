package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.ConditionResult;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public interface Executable
{
    public boolean hasId();

    public String getId();

    public void execute(final RuntimeContext context, final ConditionResult result) throws ExecutableException;
}

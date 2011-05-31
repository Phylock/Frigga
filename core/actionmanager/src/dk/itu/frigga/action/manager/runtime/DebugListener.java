package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.ConditionResult;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public interface DebugListener
{
    public void beforeExecuting(final DebugContext context, final Executable executable);
    public void afterExecuting(final DebugContext context, final Executable executable, final ConditionResult result);
    public void breakpointReached(final DebugContext context, final Breakpoint breakpoint);
    public void exceptionCaught(final DebugContext context, final Executable executable, final ExecutableException exception);
    public void executionStepped(final DebugContext context, final Executable executable);
}

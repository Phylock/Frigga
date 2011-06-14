package dk.itu.frigga.action.runtime;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public interface DebugContext
{
    public CallStack getCallStack();

    public void stepOut();
    public void stepOver();
    public void stepInto();
    public void resume();
    public void terminate();

    public boolean isBreakpoint();
    public void addBreakpoint(final Breakpoint breakpoint);
    public void removeBreakpoint(final Breakpoint breakpoint);
    public void removeAllBreakpoints();
}

package dk.itu.frigga.action.runtime;

import dk.itu.frigga.FriggaException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class BreakpointEncounteredException extends FriggaException
{
    private final Breakpoint breakpoint;

    public BreakpointEncounteredException(final Breakpoint breakpoint)
    {
        this.breakpoint = breakpoint;
    }

    public Breakpoint getBreakpoint()
    {
        return breakpoint;
    }
}

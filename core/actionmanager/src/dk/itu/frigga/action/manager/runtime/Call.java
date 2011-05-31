package dk.itu.frigga.action.manager.runtime;

import java.util.Date;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class Call
{
    private final long callTime;
    private final Executable executable;

    public Call(final Executable executable)
    {
        callTime = (new Date()).getTime();
        this.executable = executable;
    }

    @Override
    public String toString()
    {
        return "Call: " + executable + "@" + callTime;
    }
}

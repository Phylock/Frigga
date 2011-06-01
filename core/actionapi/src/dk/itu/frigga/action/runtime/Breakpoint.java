package dk.itu.frigga.action.runtime;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class Breakpoint
{
    private final Executable breakLocation;
    private int hitCount = 0;
    private boolean pauseOnHit;

    public Breakpoint(final Executable location, final boolean pauseOnHit)
    {
        breakLocation = location;
        this.pauseOnHit = pauseOnHit;
    }

    public void reset()
    {
        hitCount = 0;
    }

    public boolean test(final Executable currentExecutable)
    {
        return breakLocation.equals(currentExecutable);
    }

    public void evaluate(final Executable currentExecutable) throws BreakpointEncounteredException
    {
        if (breakLocation.equals(currentExecutable))
        {
            hitCount++;
            throw new BreakpointEncounteredException(this);
        }
    }

    public int getHitCount()
    {
        return hitCount;
    }

    public boolean requiresPause()
    {
        return pauseOnHit;
    }
}

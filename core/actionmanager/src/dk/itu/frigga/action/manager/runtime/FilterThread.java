package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.filter.Filter;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.runtime.Selection;

import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-14
 */
public class FilterThread extends Thread
{
    private Filter filter;
    private boolean running = false;
    private List<Selection> result;

    @Override
    public void run()
    {
        running = true;

        try
        {
            result = filter.run();
        }
        catch (FilterFailedException e)
        {
        }

        running = false;
    }

    public void runFilter(final Filter filter)
    {
        this.filter = filter;

        start();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void waitForCompletion() throws InterruptedException
    {
        wait();
    }

    public List<Selection> awaitResult() throws InterruptedException
    {
        waitForCompletion();
        return result;
    }

    public List<Selection> getResult()
    {
        return result;
    }
}

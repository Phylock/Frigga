package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.FriggaException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public class FilterInstantiationFailedException extends FriggaException
{
    private Throwable targetException;

    public FilterInstantiationFailedException()
    {
        super();
    }

    public FilterInstantiationFailedException(final Throwable targetException)
    {
        this.targetException = targetException;
    }

    public Throwable getTargetException()
    {
        return targetException;
    }
}

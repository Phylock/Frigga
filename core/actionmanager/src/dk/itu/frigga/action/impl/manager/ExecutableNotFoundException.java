package dk.itu.frigga.action.impl.manager;

import dk.itu.frigga.FriggaException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class ExecutableNotFoundException extends FriggaException
{
    private final String id;

    public ExecutableNotFoundException()
    {
        id = "[unnamed executable]";
    }

    public ExecutableNotFoundException(final String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "Executable Not Found Exception: " + id;
    }
}

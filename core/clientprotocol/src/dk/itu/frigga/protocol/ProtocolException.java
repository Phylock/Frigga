package dk.itu.frigga.protocol;

import dk.itu.frigga.FriggaException;

/**
 * Created by IntelliJ IDEA.
 * User: Tommy
 * Date: 10-03-11
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolException extends FriggaException {
    private final String message;

    public ProtocolException()
    {
        message = "Protocol Exception";
    }

    public ProtocolException(final String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return "Protocol Exception: " + message;
    }
}

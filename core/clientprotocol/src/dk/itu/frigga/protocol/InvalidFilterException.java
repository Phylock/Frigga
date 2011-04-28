package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: Tommy
 * Date: 10-03-11
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class InvalidFilterException extends ProtocolSyntaxException
{
    private final String filter;
    private final String message;

    public InvalidFilterException(final String filter)
    {
        this.filter = filter;
        this.message = "";
    }

    public InvalidFilterException(final String filter, final String message)
    {
        this.filter = filter;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Invalid filter: '" + filter + "' " + message;
    }
}

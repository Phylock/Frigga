package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: Tommy
 * Date: 10-03-11
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolSyntaxException extends ProtocolException
{
    private final String syntaxError;

    public ProtocolSyntaxException(final String syntaxError)
    {
        this.syntaxError = syntaxError;
    }

    public ProtocolSyntaxException()
    {
        syntaxError = "";
    }

    @Override
    public String toString()
    {
        return "Syntax error: " + syntaxError;
    }
}

package dk.itu.frigga.core.serverconnection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-24
 */
public final class ClientType
{
    private final String type;

    public ClientType(final String type)
    {
        assert(type != null) : "type can not be null.";

        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Client type: " + type;
    }
}

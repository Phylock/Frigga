package dk.itu.frigga.core.serverconnection;

import java.util.UUID;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-24
 */
public class Client
{
    private final UUID id;
    private final ClientType type;

    public Client(final UUID id, final ClientType type)
    {
        assert(id != null) : "id can not be null.";
        assert(type != null) : "type can not be null.";

        this.id = id;
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        if (!id.equals(client.id)) return false;
        if (!type.equals(client.type)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return 31 * id.hashCode() + type.hashCode();
    }

    @Override
    public String toString()
    {
        return "" + id + ", " + type;
    }
}

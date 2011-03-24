package dk.itu.frigga.core.serverconnection;

import java.util.UUID;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-24
 */
public final class Session implements Cloneable
{
    private final UUID id;
    private final Client client;

    public Session(final Client client)
    {
        assert(client != null) : "client can not be null.";

        this.id = UUID.randomUUID();
        this.client = client;
    }

    private Session(final Client client, final UUID id)
    {
        this.id = id;
        this.client = client;
    }

    @Override
    protected Object clone() // throws CloneNotSupportedException
    {
        return new Session(client, id);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!client.equals(session.client)) return false;
        if (!id.equals(session.id)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return 31 * id.hashCode() + client.hashCode();
    }

    @Override
    public String toString()
    {
        return "Session id: " + id + " client: " + client;
    }
}

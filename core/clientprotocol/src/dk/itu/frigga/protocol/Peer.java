package dk.itu.frigga.protocol;

import java.util.UUID;

/**
 * The peer class specifies a server capable of providing us with access to
 * devices.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Peer
{
    protected final String host;
    protected final int port;
    protected final String description;
    protected final UUID id;

    public Peer(final String host, final int port, final String description, final UUID id)
    {
        this.host = host;
        this.port = port;
        this.description = description;
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public UUID getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "Peer: " + description + " on " + host + ":" + port + " having id: " + id;
    }
}

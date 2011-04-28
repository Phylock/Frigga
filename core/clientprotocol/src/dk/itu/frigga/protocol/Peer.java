package dk.itu.frigga.protocol;

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

    public Peer(final String host, final int port, final String description)
    {
        this.host = host;
        this.port = port;
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "Peer: " + description + " on " + host + ":" + port;
    }
}

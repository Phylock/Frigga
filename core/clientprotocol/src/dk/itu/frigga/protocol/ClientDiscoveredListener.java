package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA. User: Tommy Date: 13-04-11 Time: 21:56 To change this template use File | Settings | File
 * Templates.
 */
public interface ClientDiscoveredListener
{
    public void clientDiscovered(final Peer client) throws StopDiscoveringException;
}

package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA. User: Tommy Date: 13-04-11 Time: 21:25 To change this template use File | Settings | File
 * Templates.
 */
public interface ServerDiscoveredListener
{
    public void serverDiscovered(final Peer server) throws StopDiscoveringException;
}

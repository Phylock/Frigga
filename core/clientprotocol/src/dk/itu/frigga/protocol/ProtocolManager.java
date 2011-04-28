package dk.itu.frigga.protocol;

import java.io.IOException;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public class ProtocolManager implements ProtocolMessageListener
{
    private transient FriggaConnection connection;

    public ProtocolManager()
    {
    }

    public void connectTo(final Peer peer) throws IOException
    {
        if (connection != null)
        {
            // Clear old connection.
            connection.stopListener();
            connection = null;
        }

        connection = new FriggaConnection(peer, false);
        connection.addMessageListener(this);
        connection.connect();
        if (connection.isConnected())
        {
            connection.startListener();
        }
    }

    public boolean isConnected()
    {
        if (connection != null)
        {
            return connection.isConnected();
        }

        return false;
    }

    public void messageReceived(MessageSource source, MessageResult result)
    {
        Message m = source.getMessage();
        result.getMessage().associateSessionId(UUID.randomUUID());
        result.use();
    }
}

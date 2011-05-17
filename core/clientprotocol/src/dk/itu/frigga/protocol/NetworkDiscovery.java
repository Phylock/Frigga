package dk.itu.frigga.protocol;

import javax.xml.bind.annotation.XmlElementRef;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-23
 */
public class NetworkDiscovery
{
    private class DiscoverThread extends Thread
    {
        private transient boolean running = true;
        private final int port;

        public DiscoverThread(int port)
        {
            super();

            this.port = port;
        }

        public final void stopDiscover()
        {
            running = false;
        }

        @Override
        public void run()
        {
            DatagramSocket socket;
            DatagramPacket needServerPacket = new NeedServerPacket().build(port);

            try
            {
                if (running)
                {
                    socket = new DatagramSocket(port);
                    socket.setBroadcast(true);
                    socket.setSoTimeout(500);
                    socket.bind(null);

                    while (running)
                    {
                        try
                        {
                            DatagramPacket received = new DatagramPacket(new byte[HasServerPacket.LENGTH], HasServerPacket.LENGTH);

                            socket.send(needServerPacket);
                            socket.receive(received);

                            if (HasServerPacket.validate(received))
                            {
                                HasServerPacket hasServerPacket = new HasServerPacket(received);
                                final Peer server = new Peer(hasServerPacket.getServerHost(), hasServerPacket.getServerPort(), hasServerPacket.getServerDescription());
                                notifyServerDiscoveredListeners(server);
                            }
                        }
                        catch (IOException e)
                        {
                            // Allow
                        }
                        catch (NotCorrectPacketException e)
                        {
                            // Allow, should not happen due to validate call.
                        }
                        catch (StopDiscoveringException e)
                        {
                            // Stop discovering, do this by setting the running to false.
                            running = false;
                        }

                        try
                        {
                            if (running) Thread.sleep(delay);
                        }
                        catch (InterruptedException e)
                        {
                            // Allow
                        }
                    }

                    socket.close();
                }
            }
            catch (SocketException e)
            {
                running = false;
            }

        }
    }

    private int delay = 5000;
    private final List<ServerDiscoveredListener> serverDiscoveredListeners = Collections.synchronizedList(new LinkedList<ServerDiscoveredListener>());
    private final DiscoverThread discoverThread;

    public NetworkDiscovery()
    {
        discoverThread = new DiscoverThread(NetworkAnnouncer.DEFAULT_PORT);
    }

    public NetworkDiscovery(final int port)
    {
        assert(port > 0 && port < 65536) : "Port is out of range.";

        discoverThread = new DiscoverThread(port);
    }

    public void addServerDiscoveredListener(final ServerDiscoveredListener listener)
    {
        serverDiscoveredListeners.add(listener);
    }

    public void removeServerDiscoveredListener(final ServerDiscoveredListener listener)
    {
        serverDiscoveredListeners.remove(listener);
    }

    private void notifyServerDiscoveredListeners(final Peer server) throws StopDiscoveringException
    {
        boolean stopDiscovering = false;

        for (ServerDiscoveredListener listener : serverDiscoveredListeners)
        {
            try
            {
                listener.serverDiscovered(server);
            }
            catch (StopDiscoveringException e)
            {
                stopDiscovering = true;
            }
        }

        if (stopDiscovering) throw new StopDiscoveringException();
    }

    public void setDelay(final int delay)
    {
        assert(delay > 0) : "Delay can not be zero or negative.";

        this.delay = delay;
    }

    public int getDelay()
    {
        return delay;
    }

    public void start()
    {
        discoverThread.start();
    }

    public void stop()
    {
        discoverThread.stopDiscover();
    }
}
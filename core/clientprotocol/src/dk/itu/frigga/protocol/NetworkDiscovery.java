package dk.itu.frigga.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collections;
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
        private final DatagramPacket packet;
        private final int port;

        public DiscoverThread(int port)
        {
            super();

            byte[] data = new byte[0];

            this.port = port;
            this.packet = new DatagramPacket(data, data.length, port);
        }

        public final void stopDiscover()
        {
            running = false;
        }

        @Override
        public void run()
        {
            DatagramSocket socket;
            DatagramPacket received = new DatagramPacket(new byte[32], 32);

            try
            {
                if (running)
                {
                    socket = new DatagramSocket(port);
                    socket.setBroadcast(true);
                    socket.setSoTimeout(500);

                    while (running)
                    {
                        try
                        {
                            socket.send(packet);
                            socket.receive(received);

                            //
                        }
                        catch (IOException e)
                        {
                            // Allow
                        }

                        try
                        {
                            Thread.sleep(3000);
                        }
                        catch (InterruptedException e)
                        {
                            // Allow
                        }
                    }
                }
            }
            catch (SocketException e)
            {
                running = false;
            }

        }
    }

    private final List<ServerDiscoveredListener> serverDiscoveredListeners = Collections.synchronizedList(new LinkedList<ServerDiscoveredListener>());
    private final DiscoverThread discoverThread = new DiscoverThread(7250);

    public NetworkDiscovery()
    {
    }

    public void addServerDiscoveredListener(final ServerDiscoveredListener listener)
    {
        serverDiscoveredListeners.add(listener);
    }

    public void removeServerDiscoveredListener(final ServerDiscoveredListener listener)
    {
        serverDiscoveredListeners.remove(listener);
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
package dk.itu.frigga.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-28
 */
public class NetworkAnnouncer
{
    public static final int DEFAULT_PORT = 7250;

    private transient Peer lastKnownPeer;
    private AnnouncerThread announcerThread;

    private final List<ServerInformationRequiredListener> serverInformationRequiredListeners = Collections.synchronizedList(new LinkedList<ServerInformationRequiredListener>());

    private class AnnouncerThread extends Thread
    {
        private transient boolean running = true;
        private final int port;

        public AnnouncerThread(final int port)
        {
            super();

            this.port = port;
        }

        @Override
        public void run()
        {
            try
            {
                DatagramSocket socket = new DatagramSocket(port);
                socket.setBroadcast(true);
                socket.setSoTimeout(500);
                //socket.bind(null);

                while (running)
                {
                    DatagramPacket received = new DatagramPacket(new byte[NeedServerPacket.LENGTH], NeedServerPacket.LENGTH);
                    try
                    {
                        socket.setBroadcast(true);
                        socket.receive(received);

                        if (NeedServerPacket.validate(received))
                        {
                            try
                            {
                                NeedServerPacket needServerPacket = new NeedServerPacket(received);

                                final String clientIp = received.getAddress().getHostAddress();
                                final Peer server = notifyServerInformationRequiredListeners(clientIp);

                                if (running && server != null)
                                {
                                    if (needServerPacket.matchServer(server.id))
                                    {
                                        HasServerPacket hasServerPacket = new HasServerPacket(server.host, server.port, server.id, server.description);
                                        DatagramPacket serverPacket = hasServerPacket.build(received.getAddress(), received.getPort());

                                        socket.setBroadcast(true);
                                        socket.send(serverPacket);
                                    }
                                }
                            }
                            catch (NotCorrectPacketException e)
                            {
                                // Since we are using the validate function this should never occur.
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        // Probably received a timeout, no packet received.
                    }
                }

                socket.close();
            }
            catch (SocketException e)
            {
                // Unable to bind server socket on specified port, perhaps because it already runs an instance on this
                // port.
            }
        }

        public void startAnnouncer()
        {
            running = true;
            start();
        }

        public void stopAnnouncer()
        {
            running = false;
        }
    }

    public NetworkAnnouncer()
    {
        announcerThread = new AnnouncerThread(DEFAULT_PORT);
    }

    public NetworkAnnouncer(final int port)
    {
        assert(port > 0 && port < 65536) : "Port is out of range.";

        announcerThread = new AnnouncerThread(port);
    }

    public void addClientDiscoveredListener(final ServerInformationRequiredListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        serverInformationRequiredListeners.add(listener);
    }

    public void removeClientDiscoveredListener(final ServerInformationRequiredListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        serverInformationRequiredListeners.remove(listener) ;
    }

    public void start()
    {
        announcerThread.startAnnouncer();
    }

    public void stop()
    {
        announcerThread.stopAnnouncer();
    }

    private Peer notifyServerInformationRequiredListeners(final String clientIp)
    {
        Peer suggested = lastKnownPeer;

        for (ServerInformationRequiredListener listener : serverInformationRequiredListeners)
        {
            suggested = listener.requestServerInformation(clientIp, suggested);
        }

        if (suggested != null)
        {
            lastKnownPeer = suggested;
        }

        return suggested;
    }
}

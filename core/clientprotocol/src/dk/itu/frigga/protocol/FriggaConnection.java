package dk.itu.frigga.protocol;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
final public class FriggaConnection
{
    private class ConnectionListener implements Runnable
    {
        private transient boolean listening = false;

        public ConnectionListener()
        {
        }

        public void run()
        {
            Thread.currentThread().setName("ConnectionListener");

            try
            {
                while (listening)
                {
                    try
                    {
                        Packet packet = new Packet(FriggaConnection.this.socket.getInputStream());
                        messageQueue.queue(new MessageReceivedHandler(new Message(packet.getInputStream())));
                    }
                    catch (ParserConfigurationException e)
                    {
                        // Allow this
                    }
                    catch (IOException e)
                    {
                        listening = false;
                    }
                    catch (SAXException e)
                    {
                        // Allow this
                    }
                    catch (ProtocolSyntaxException e)
                    {
                        // Allow this
                    }
                    catch (ProtocolException e)
                    {
                        // Allow this
                    }
                    catch (InterruptedException e)
                    {
                        // Allow this
                    }

                    Thread.yield();
                }
            }
            finally
            {
                listening = false;
            }
        }

        public boolean isListening()
        {
            return listening;
        }

        public void setListening(final boolean listening)
        {
            this.listening = listening;

            if (listening)
            {
                new Thread(this).start();
            }
        }
    }

    private class MessageReceivedHandler implements dk.itu.frigga.protocol.MessageHandler
    {
        private final Message message;
        private int retryCount = 50;
        private long delay = 0;

        public MessageReceivedHandler(final Message message)
        {
            assert(message != null) : "Message can not be null.";

            this.message = message;
        }

        public void addInitialDelay(long ms)
        {
            delay = ms;
        }

        public void handle() throws RetryMessageHandlingException, ProtocolException
        {
            doDelay();

            final MessageResult result = new MessageResult(FriggaConnection.this, new Message());
            final MessageSource source = new MessageSource(FriggaConnection.this, message);

            for (ProtocolMessageListener listener : messageListeners)
            {
                listener.messageReceived(source, result);
            }

            notifyQueryListeners(source, result);
            notifyInformationListeners(source, result);
            notifyActionListeners(source, result);
            notifyOptionsListeners(source, result);

            if (result.hasResult)
            {
                try
                {
                    sendMessage(result.getMessage());
                }
                catch (InterruptedException e)
                {
                    throw new RetryMessageHandlingException();
                }
                catch (IOException e)
                {
                    throw new RetryMessageHandlingException();
                }
            }
        }

        public boolean retry()
        {
            return --retryCount > 0;
        }

        private void doDelay()
        {
            if (delay > 0)
            {
                try
                {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e)
                {
                    // Allowed.
                }
            }
        }
    }

    private class SendMessageHandler implements dk.itu.frigga.protocol.MessageHandler
    {
        private final Packet packet;
        private int retryCount = 50;
        private long delay = 0;

        public SendMessageHandler(final Message message) throws IOException, ProtocolException
        {
            assert(message != null) : "Message can not be null.";

            packet = new Packet(message);
        }

        public void addInitialDelay(long ms)
        {
            delay = ms;
        }

        public void handle() throws RetryMessageHandlingException, ProtocolException
        {
            doDelay();

            if (!socket.isConnected())
            {
                throw new RetryMessageHandlingException();
            }

            try
            {
                packet.writeToStream(FriggaConnection.this.socket.getOutputStream());
            }
            catch (IOException e)
            {
                throw new RetryMessageHandlingException();
            }
        }

        public boolean retry()
        {
            return --retryCount > 0;
        }

        private void doDelay()
        {
            if (delay > 0)
            {
                try
                {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e)
                {
                    // Allowed.
                }
            }
        }
    }

    private class MessageHandlerQueue implements Runnable
    {
        private final BlockingQueue<MessageHandler> queue = new LinkedBlockingQueue<MessageHandler>();
        private transient boolean listen = true;

        public MessageHandlerQueue()
        {
        }

        public void queue(final MessageHandler handler) throws InterruptedException
        {
            assert(handler != null) : "Message can not be null.";

            queue.put(handler);
        }

        public void setListening(final boolean listening)
        {
            listen = listening;

            if (listening)
            {
                new Thread(this).start();
            }
        }

        public void run()
        {
            Thread.currentThread().setName("MessageHandlerQueue");

            while (listen)
            {
                try
                {
                    while (!queue.isEmpty() && socket.isConnected())
                    {
                        MessageHandler handler = queue.take();
                        try
                        {
                            handler.handle();
                        }
                        catch (RetryMessageHandlingException e)
                        {
                            // This handler wishes to retry the message handling, so this handler is added to the end of
                            // the queue.
                            if (handler.retry())
                            {
                                handler.addInitialDelay(100);
                                queue.put(handler);
                                Thread.yield();
                            }
                        }
                        catch (AbortMessageHandlingException e)
                        {
                            // We wish to abort the message handling of this message, so we do not put this in the
                            // queue again.
                        }
                        catch (ProtocolException e)
                        {
                            if (handler.retry())
                            {
                                handler.addInitialDelay(100);
                                queue.put(handler);
                                Thread.yield();
                            }
                        }
                    }

                    if (listen) Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    // Stop running
                    listen = false;
                }

                Thread.yield();
            }
        }
    }

    private final Peer peer;
    private final Socket socket;
    private final ConnectionListener listener = new ConnectionListener();
    private final MessageHandlerQueue messageQueue = new MessageHandlerQueue();
    private final List<ProtocolMessageListener> messageListeners = Collections.synchronizedList(new LinkedList<ProtocolMessageListener>());
    private final List<InformationReceivedListener> informationReceivedListeners = Collections.synchronizedList(new LinkedList<InformationReceivedListener>());
    private final List<ActionReceivedListener> actionReceivedListeners = Collections.synchronizedList(new LinkedList<ActionReceivedListener>());
    private final List<OptionsReceivedListener> optionsReceivedListeners = Collections.synchronizedList(new LinkedList<OptionsReceivedListener>());
    private final List<QueryReceivedListener> queryReceivedListeners = Collections.synchronizedList(new LinkedList<QueryReceivedListener>());

    public FriggaConnection(final Peer peer)
    {
        this.peer = peer;
        this.socket = new Socket();

        try
        {
            this.socket.setReceiveBufferSize(128 * 1024);
        }
        catch (SocketException e)
        {
            // Ignore
        }

        try
        {
            connect();
        }
        catch (IOException e)
        {
            // Allow exception.
        }
    }

    public FriggaConnection(final Peer peer, final boolean connected)
    {
        this.peer = peer;
        this.socket = new Socket();

        try
        {
            this.socket.setReceiveBufferSize(128 * 1024);
        }
        catch (SocketException e)
        {
            // Ignore
        }

        if (connected)
        {
            try
            {
                connect();
            }
            catch (IOException e)
            {
                // Allow exception.
            }
        }
    }

    public void connect() throws IOException
    {
        socket.connect(new InetSocketAddress(peer.host, peer.port));
        messageQueue.setListening(true);
    }

    public boolean isConnected()
    {
        return socket.isConnected();
    }

    public void startListener()
    {
        listener.setListening(true);
    }

    public void stopListener()
    {
        //messageQueue.setListening(false);
        listener.setListening(false);
    }

    public void sendMessage(final Message message) throws IOException, ProtocolException, InterruptedException
    {
        messageQueue.queue(new SendMessageHandler(message));
    }

    public void addMessageListener(final ProtocolMessageListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        messageListeners.add(listener);
    }

    public void removeMessageListener(final ProtocolMessageListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        messageListeners.remove(listener);
    }

    public void addQueryReceivedListener(final QueryReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        queryReceivedListeners.add(listener);
    }

    public void removeQueryReceivedListener(final QueryReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        queryReceivedListeners.remove(listener);
    }

    public void addInformationReceivedListener(final InformationReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        informationReceivedListeners.add(listener);
    }

    public void removeInformationReceivedListener(final InformationReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        informationReceivedListeners.remove(listener);
    }

    public void addActionReceivedListener(final ActionReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        actionReceivedListeners.add(listener);
    }

    public void removeActionReceivedListener(final ActionReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        actionReceivedListeners.remove(listener);
    }

    public void addOptionsReceivedListener(final OptionsReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        optionsReceivedListeners.add(listener);
    }

    public void removeOptionsReceivedListener(final OptionsReceivedListener listener)
    {
        assert(listener != null) : "Listener can not be null.";

        optionsReceivedListeners.remove(listener);
    }

    private void notifyQueryListeners(final MessageSource source, final MessageResult result)
    {
        final boolean hasLookups = source.getMessage().getLookups().count() > 0;
        final boolean hasRequires = source.getMessage().getRequires().count() > 0;

        if (hasLookups || hasRequires)
        {
            for (QueryReceivedListener listener : queryReceivedListeners)
            {
                if (hasRequires) listener.requiresReceived(source, result, source.getMessage().getRequires());
                if (hasLookups) listener.lookupsReceived(source, result, source.getMessage().getLookups());
            }
        }
    }

    private void notifyInformationListeners(final MessageSource source, final MessageResult result)
    {
        final boolean hasReports = source.getMessage().getReports().count() > 0;
        final boolean hasResources = source.getMessage().getResources().count() > 0;

        if (hasReports || hasResources)
        {
            for (InformationReceivedListener listener : informationReceivedListeners)
            {
                if (hasReports) listener.reportsReceived(source, result, source.getMessage().getReports());
                if (hasResources) listener.resourcesReceived(source, result, source.getMessage().getResources());
            }
        }
    }

    private void notifyActionListeners(final MessageSource source, final MessageResult result)
    {
        final boolean hasRequests = source.getMessage().getRequests().count() > 0;

        if (hasRequests)
        {
            for (ActionReceivedListener listener : actionReceivedListeners)
            {
                listener.requestsReceived(source, result, source.getMessage().getRequests());
            }
        }
    }

    private void notifyOptionsListeners(final MessageSource source, final MessageResult result)
    {
        final boolean hasOptions = source.getMessage().getOptions().count() > 0;

        if (hasOptions)
        {
            for (OptionsReceivedListener listener : optionsReceivedListeners)
            {
                listener.optionsReceived(source, result, source.getMessage().getOptions());
            }
        }
    }
}

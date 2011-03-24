package dk.itu.frigga.core.serverconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-24
 */
public class Connection
{
    private final Session session;
    private final Socket socket;

    public Connection(final Session session, final Socket socket)
    {
        this.session = session;
        this.socket = socket;
    }

    public OutputStream getOutputStream() throws IOException
    {
        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException
    {
        return socket.getInputStream();
    }
}

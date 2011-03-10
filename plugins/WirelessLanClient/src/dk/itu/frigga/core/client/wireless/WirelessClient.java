package dk.itu.frigga.core.client.wireless;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import dk.itu.frigga.core.clientapi.Client;
import dk.itu.frigga.core.clientapi.Session;
import java.net.Socket;

/**
 *
 * @author phylock
 */
public class WirelessClient extends Thread implements Client{
    private Socket socket;

    public WirelessClient(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {

    }

    public Session getSession() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

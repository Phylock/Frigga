package dk.itu.frigga.core.client.wireless;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 *
 * @author phylock
 */
public class ServerConnection extends Thread {

    private volatile boolean running;
    private int port;

    public ServerConnection(int port) {
        this.port = 4444;
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                //Start Listener
                waitForClients(new ServerSocket(port));
            } catch (IOException ex) {
                //Something wrong, wait and try again
                try {
                    Thread.sleep(5 * 60000);
                    //TODO: log
                } catch (InterruptedException ex1) {
                }
            }
        }
    }

    public void waitForClients(ServerSocket socket) throws IOException {
        while (running) {
            new WirelessClient(socket.accept()).start();
        }
    }
}

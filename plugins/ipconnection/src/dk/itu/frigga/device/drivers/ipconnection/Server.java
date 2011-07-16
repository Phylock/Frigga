/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.protocol.FriggaConnection;
import dk.itu.frigga.protocol.NetworkAnnouncer;
import dk.itu.frigga.protocol.Peer;
import dk.itu.frigga.protocol.ServerInformationRequiredListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author phylock
 */
public class Server {

  private static final String THREAD_NAME = "Ip Driver Server";
  private final NetworkAnnouncer announcer = new NetworkAnnouncer();
  private final ServerInformationProvider infoprovider;
  private final ConnectionListener serverconnection = new ConnectionListener();
  private final ServerSocket server;
  private final UUID server_id;
  private final FriggaConnectionListener handle;
  private boolean alive = false;

  public Server(Dictionary prop, FriggaConnectionListener handle) throws IOException {
    this.handle = handle;

    int port = 0;
    String address = null;
    String description = "";

    server_id = UUID.fromString(parseString(prop, "uuid", (UUID.randomUUID()).toString()));
    port = parseInteger(prop, "port", 6666);
    address = parseString(prop, "address", null);
    description = parseString(prop, "description", "no description");

    if (address != null) {
      server = new ServerSocket();
      server.bind(new InetSocketAddress(address, port));
    } else {
      server = new ServerSocket(port);
    }

    infoprovider = new ServerInformationProvider(server_id, description, server);
    announcer.addClientDiscoveredListener(infoprovider);
  }

  private String parseString(Dictionary prop, String key, String def) {
    Object obj = prop.get(key);
    if (obj != null) {
      return obj.toString();
    }
    return def;
  }

  private int parseInteger(Dictionary prop, String key, int def) {
    try {
      Object obj = prop.get(key);
      if (obj != null) {
        return Integer.parseInt(obj.toString());
      }
    } catch (NumberFormatException ex) {
    }
    return def;
  }

  public void activate() {
    if (!alive) {
      announcer.start();
      serverconnection.activate();
      alive = true;
    }

  }

  private void configure() {
  }

  public void deactivate() {
    if (alive) {
      announcer.stop();
      serverconnection.deactivate();
      alive = false;
    }
  }

  public boolean isAlive() {
    return alive;
  }

  private class ConnectionListener extends Thread {

    private volatile boolean running = false;

    public void activate() {
      if (!this.running) {
        start();
      }
    }

    public void deactivate() {
      running = false;
      this.interrupt();
    }

    @Override
    public void run() {
      Thread.currentThread().setName(THREAD_NAME);
      running = true;
      while (running) {
        try {
          Socket socket = server.accept();
          FriggaConnection conn = new FriggaConnection(infoprovider.getServerPeer(), socket);

          handle.newUnknownConnection(conn);
        } catch (IOException ex) {
          //ignore and continue
        }
      }
    }
  }

  private class ServerInformationProvider implements ServerInformationRequiredListener {

    private final Peer serverpeer;

    public ServerInformationProvider(UUID uuid, String description, ServerSocket socket) {
      this.serverpeer = new Peer(socket.getInetAddress().getHostAddress(), socket.getLocalPort(), description, uuid);
    }

    public Peer requestServerInformation(String requesterIp, Peer suggestedServer) {
      return serverpeer;
    }

    public Peer getServerPeer() {
      return serverpeer;
    }
  }
}

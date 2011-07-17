/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.core.clientapi.Client;
import dk.itu.frigga.core.clientapi.ClientManager;
import dk.itu.frigga.core.clientapi.RequestFunctionCall;
import dk.itu.frigga.protocol.ActionReceivedListener;
import dk.itu.frigga.protocol.FriggaConnection;
import dk.itu.frigga.protocol.InformationReceivedListener;
import dk.itu.frigga.protocol.Lookups;
import dk.itu.frigga.protocol.Message;
import dk.itu.frigga.protocol.MessageResult;
import dk.itu.frigga.protocol.MessageSource;
import dk.itu.frigga.protocol.ProtocolException;
import dk.itu.frigga.protocol.QueryReceivedListener;
import dk.itu.frigga.protocol.Report;
import dk.itu.frigga.protocol.ReportNotFoundException;
import dk.itu.frigga.protocol.Reports;
import dk.itu.frigga.protocol.Requests;
import dk.itu.frigga.protocol.Requires;
import dk.itu.frigga.protocol.Resources;
import dk.itu.frigga.protocol.Selection;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class FriggaConnectionHandler implements ActionReceivedListener, InformationReceivedListener, QueryReceivedListener {

  private final FriggaConnection connection;
  private ClientManager manager;
  private Client client = null;
  private IpClientDriver driver;
  private String challenge;
  private String user = null;
  private UUID device = null;

  public FriggaConnectionHandler(IpClientDriver driver, ClientManager manager, FriggaConnection connection) {
    this.manager = manager;
    this.connection = connection;
    this.driver = driver;
    connection.addActionReceivedListener(this);
    connection.addInformationReceivedListener(this);
    connection.addQueryReceivedListener(this);
    connection.startListener();
  }

  public void requestsReceived(MessageSource source, MessageResult result, Requests requests) {
    System.out.println("source: " + source + ", request: " + requests);
    for (int i = 0; i < requests.count(); i++) {
      List<Selection> selections = requests.get(i).getSelections();
      Map<String,Object> params = new HashMap<String, Object>();
      params.put("value", requests.get(i).getValue());
      manager.handleUserRequest(client, new RequestFunctionCall(selections, null, params));
    }
  }

  public void resourcesReceived(MessageSource source, MessageResult result, Resources resources) {
    System.out.println("source: " + source + ", resources: " + resources);
  }

  public void reportsReceived(MessageSource source, MessageResult result, Reports reports) {
    if (reports.has("user") && reports.has("device")) {
      try {
        String user = reports.get("user").getValue();
        String device = reports.get("device").getValue();

        identify(user, device);

      } catch (ReportNotFoundException ex) {
        Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    if (reports.has("secret")) {
      try {
        String secret = reports.get("secret").getValue();
      } catch (ReportNotFoundException ex) {
        Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    if (reports.has("gps.latitude") && reports.has("gps.longitude") && reports.has("gps.provider")) {
      try {
        String latitude = reports.get("gps.latitude").getValue();
        String longitude = reports.get("gps.longitude").getValue();
        String provider = reports.get("gps.provider").getValue();
        System.out.println("latitude: " + latitude + ", longitude: " + longitude + ", provider: " + provider);
      } catch (ReportNotFoundException ex) {
        Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void requiresReceived(MessageSource source, MessageResult result, Requires requires) {
    System.out.println("source: " + source + ", requires: " + requires);
    if (requires.has("challenge")) {
      challenge = UUID.randomUUID().toString();
      result.getMessage().getReports().add(new Report("challenge", challenge));
      result.use();
    }
    if (requires.has("id")) {
      //TODO: FIX
    }
    for (int i = 0; i < requires.count(); i++) {
      System.out.println(requires.get(i).whatIsRequired());
    }
  }

  public void lookupsReceived(MessageSource source, MessageResult result, Lookups lookups) {
    System.out.println("source: " + source + ", lookups: " + lookups);
  }

  public Client getClient() {
    return client;
  }

  public UUID getDevice() {
    return device;
  }

  public String getUser() {
    return user;
  }

  public FriggaConnection getConnection() {
    return connection;
  }

  private void identify(String user, String device) {
    try {
      this.user = user;
      this.device = UUID.fromString(device);
      client = manager.enter(this.device, this.user);
      driver.newIdentifiedConnection(this);
      Message m = new Message();
      m.associateSessionId(client.getSession().getSessionId());
      connection.sendMessage(m);
    } catch (IOException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ProtocolException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

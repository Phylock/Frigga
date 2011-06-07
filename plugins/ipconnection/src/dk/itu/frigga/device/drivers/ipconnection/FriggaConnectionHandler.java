/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.protocol.ActionReceivedListener;
import dk.itu.frigga.protocol.FriggaConnection;
import dk.itu.frigga.protocol.InformationReceivedListener;
import dk.itu.frigga.protocol.Lookups;
import dk.itu.frigga.protocol.Message;
import dk.itu.frigga.protocol.MessageResult;
import dk.itu.frigga.protocol.MessageSource;
import dk.itu.frigga.protocol.ProtocolException;
import dk.itu.frigga.protocol.QueryReceivedListener;
import dk.itu.frigga.protocol.ReportNotFoundException;
import dk.itu.frigga.protocol.Reports;
import dk.itu.frigga.protocol.Requests;
import dk.itu.frigga.protocol.Require;
import dk.itu.frigga.protocol.Requires;
import dk.itu.frigga.protocol.Resources;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class FriggaConnectionHandler implements ActionReceivedListener, InformationReceivedListener, QueryReceivedListener {

  private final FriggaConnection connection;

  public FriggaConnectionHandler(FriggaConnection connection) {

    this.connection = connection;
    connection.addActionReceivedListener(this);
    connection.addInformationReceivedListener(this);
    connection.addQueryReceivedListener(this);
    connection.startListener();
    try {
      Message m = new Message();
      m.getRequires().add(new Require("gps.coordinates"));
      connection.sendMessage(m);
    } catch (IOException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ProtocolException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(FriggaConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void requestsReceived(MessageSource source, MessageResult result, Requests requests) {
    System.out.println("source: " + source + ", request: " + requests);
  }

  public void resourcesReceived(MessageSource source, MessageResult result, Resources resources) {
    System.out.println("source: " + source + ", resources: " + resources);
  }

  public void reportsReceived(MessageSource source, MessageResult result, Reports reports) {
    if (reports.has("gps.latitude")) {
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
  }

  public void lookupsReceived(MessageSource source, MessageResult result, Lookups lookups) {
    System.out.println("source: " + source + ", lookups: " + lookups);
  }
}

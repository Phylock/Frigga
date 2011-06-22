/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.core.clientapi;

import java.util.UUID;

/**
 *
 * @author phylock
 */
public class Client {

  private final Session session;
  private final UUID clientId;
  private final String deviceId;

  public Client(Session session, UUID clientId, String deviceId) {
    this.session = session;
    this.clientId = clientId;
    this.deviceId = deviceId;
  }

  public Client(UUID clientId, String deviceId) {
    session = null;
    this.clientId = clientId;
    this.deviceId = deviceId;
  }

  public Session getSession() {
    return session;
  }

  public UUID getClientId() {
    return clientId;
  }

  public String getDeviceId() {
    return deviceId;
  }
}

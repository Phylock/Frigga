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

  public Client(Session session) {
    this.session = session;
  }

  public Client(UUID clientId, String deviceId) {
    session = null;
  }

  public Session getSession() {
    return session;
  }
}

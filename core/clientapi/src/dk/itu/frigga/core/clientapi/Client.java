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
  private String username;

  public Client(Session session) {
    this.session = session;
    username = "";
  }

  public Client(Session session, String Username) {
    this.session = session;
    this.username = Username;
  }

  public Session getSession() {
    return session;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}

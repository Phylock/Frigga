/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.client.manager;

import dk.itu.frigga.core.clientapi.Authentication;
import dk.itu.frigga.core.clientapi.Client;
import dk.itu.frigga.core.clientapi.ClientManager;
import dk.itu.frigga.core.clientapi.Session;
import dk.itu.frigga.core.clientapi.UserRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class ClientManagerImpl implements ClientManager {

  private final Map<UUID, Session> clients;
  private final AuthenticationHandler authentication;

  public ClientManagerImpl() {
    clients = Collections.synchronizedMap(new HashMap<UUID, Session>());
    authentication = new AuthenticationMock();
  }

  @Override
  public void login(Client client, Authentication auth) {
    authentication.authenticate(client,auth);
  }

  @Override
  public void logout(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleUserRequest(Client client, UserRequest request) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Client enter(UUID clietid, String deviceId) {
    if (clients.containsKey(clietid)) {
      return new Client(clients.get(clietid), clietid, deviceId);
    } else {
      Session s = new Session(clietid, deviceId);
      Client c = new Client(s, clietid, deviceId);
      clients.put(clietid, s);
      return c;
    }
  }
}

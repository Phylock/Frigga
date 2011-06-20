/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.client.manager;

import dk.itu.frigga.core.clientapi.Client;
import dk.itu.frigga.core.clientapi.ClientManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class ClientManagerImpl implements ClientManager{
  private final Map<UUID, Client> clients;
  private final Authentication authentication;

  public ClientManagerImpl() {
    clients = Collections.synchronizedMap(new HashMap<UUID, Client>());
    authentication = new AuthenticationMock();
  }

  public Client login(Client client) {
    Client c = authentication.authenticate(client);
    clients.put(c.getSession().getSessionId(), c);
    return c;
  }

  public Client logout(Client client) {
    Client c = authentication.authenticate(client);
    clients.put(c.getSession().getSessionId(), c);
    return c;
  }

  public boolean validate(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}

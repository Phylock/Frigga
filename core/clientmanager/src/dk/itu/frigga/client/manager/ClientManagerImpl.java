/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.client.manager;

import dk.itu.frigga.core.clientapi.Authentication;
import dk.itu.frigga.core.clientapi.Client;
import dk.itu.frigga.core.clientapi.ClientManager;
import dk.itu.frigga.core.clientapi.RequestFunctionCall;
import dk.itu.frigga.core.clientapi.Session;
import dk.itu.frigga.core.clientapi.UserRequest;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.protocol.Filter;
import dk.itu.frigga.protocol.FilterElement;
import dk.itu.frigga.protocol.Selection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.osgi.service.log.LogService;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class ClientManagerImpl implements ClientManager {

  private final Map<UUID, Session> clients;
  private final AuthenticationHandler authentication;
  private LogService log;
  private DeviceManager devicemanager;

  public ClientManagerImpl() {
    clients = Collections.synchronizedMap(new HashMap<UUID, Session>());
    authentication = new AuthenticationMock();
  }

  @Override
  public void login(Client client, Authentication auth) {
    authentication.authenticate(client, auth);
  }

  @Override
  public void logout(Client client) {
    if (clients.containsKey(client.getSession().getUserId())) {
      clients.remove(client.getSession().getSessionId());
      client.getSession().invalidate();
    }
  }

  @Override
  public void handleUserRequest(Client client, UserRequest request) {
    log.log(LogService.LOG_INFO, "userrequest: " + request + " from " + client.getSession().getUserId());
    if (request instanceof RequestFunctionCall) {
      RequestFunctionCall functioncall = (RequestFunctionCall) request;
      for (Selection selection : functioncall.getSelections()) {
        String function = functioncall.getFunction();
        Filter filter = selection.getFilter();
        Set<Device> devices = new HashSet<Device>();
        for (int idx_filter = 0; idx_filter < filter.getElementCount(); idx_filter++) {
          FilterElement element = filter.getElement(idx_filter);
          FilterElement.Group group = element.getGroup();
          switch (group) {
            case DEVICE:
              String category = element.getSpecifier();
              Iterable<Device> devicesByType = devicemanager.getDevicesByType(category);
              for (Device d : devicesByType) {
                devices.add(d);
              }
              break;
            case ID:
              devices.add(devicemanager.getDeviceById(new DeviceId(element.getSpecifier())));
              break;
            case FUNCTION:
              function = element.getSpecifier();
              break;
            case LOCATION:
              //TODO: Ignore for now, implements later
              break;
            case REGION:
              //TODO: Ignore for now, implements later
              break;
            case ROOM:
              //TODO: Ignore for now, implements later
              break;
          }
        }
        if (function != null && !devices.isEmpty()) {
          Map<String, Object> params = functioncall.getParams();
          Parameter parameters[] = new Parameter[params.size()];
          int i = 0;
          for (Entry<String, Object> entry : params.entrySet()) {
            parameters[i++] = new Parameter(entry.getKey(), entry.getValue());
          }

          devicemanager.callFunction(function, devices.toArray(new Device[0]), parameters);
        }
      }
    }
  }

  @Override
  public Client enter(UUID clietid, String deviceId) {
    Client client = null;
    if (clients.containsKey(clietid)) {
      Session s = clients.get(clietid);
      if (s.getStatus() == Session.Status.Valid) {
        client = new Client(s);
      }
    }
    if (client == null) {
      Session s = new Session(clietid, deviceId);
      client = new Client(s);
      clients.put(clietid, s);
    }
    return client;
  }
}

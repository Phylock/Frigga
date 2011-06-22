package dk.itu.frigga.core.clientapi;

import java.util.UUID;

/**
 *
 * @author Mikkel Wendt-Larsen(miwe@itu.dk)
 */
public interface ClientManager {

  /**
   * Authenticate a client, and get user rights
   * @param client the client
   * @param auth the authentication object to validate
   */
  public void login(Client client, Authentication auth);

  /**
   * Force logout, this will clean the session data
   * @param client
   */
  public void logout(Client client);

  /**
   * Make a new client connection known to the manager,
   * this will set the previous session to the client,
   * or generate a new one if none is available
   * @param clietid
   * @param deviceId
   * @return
   */
  public Client enter(UUID clietid, String deviceId);

  /**
   * Will handle UserRequests from the client drivers implementations
   * @param client
   * @param request
   */
  public void handleUserRequest(Client client, UserRequest request);
}

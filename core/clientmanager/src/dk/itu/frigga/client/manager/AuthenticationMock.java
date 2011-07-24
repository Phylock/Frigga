/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.client.manager;

import dk.itu.frigga.core.clientapi.Authentication;
import dk.itu.frigga.core.clientapi.Client;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class AuthenticationMock implements AuthenticationHandler{
  public void authenticate(Client client, Authentication auth) {
    //Accept all 
    client.setUsername(auth.getUsername());
  }
}

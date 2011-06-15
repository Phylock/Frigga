/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.client.manager;

import dk.itu.frigga.core.clientapi.Client;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class AuthenticationMock implements Authentication{
  public Client authenticate(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

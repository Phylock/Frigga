/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.core.clientapi;

/**
 *
 * @author Mikkel Wendt-Larsen(miwe@itu.dk)
 */
public interface ClientManager
{
  public Client login(Client client);
  public Client logout(Client client);
  public boolean validate(Client client);
}

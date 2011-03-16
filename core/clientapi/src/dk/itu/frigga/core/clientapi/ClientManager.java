/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.core.clientapi;

import dk.itu.frigga.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author phylock
 */
public class ClientManager extends Singleton{

    private List<Client> clients;
    private static ClientManager instance = new ClientManager();
    private ClientManager() {
        clients = Collections.synchronizedList(new ArrayList<Client>());
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Client client) {
        clients.add(client);
    }

    public static ClientManager instance()
    {
       return instance;
    }
}

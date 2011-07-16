/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.protocol.FriggaConnection;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public interface FriggaConnectionListener {

  void newUnknownConnection(FriggaConnection connection);

  void newIdentifiedConnection(FriggaConnectionHandler handler);
}

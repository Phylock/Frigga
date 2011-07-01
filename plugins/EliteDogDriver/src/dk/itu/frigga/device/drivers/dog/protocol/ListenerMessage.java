/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class ListenerMessage extends ConfigRequestMessage {

  public enum ListenerActions {

    ADD, REMOVE, MODIFY
  }

  /**
   * Add or remove a listener to a device
   * @param session session id
   * @param devices device filter, use null for all devices
   * @param action can be "add" ...
   * @return the xml request
   * @throws ParserConfigurationException
   */
  public ListenerMessage(String[] devices, ListenerActions action) {
    super("LISTENER");
    try {
      initialize();
      Element request = (Element) doc.getElementsByTagName("request").item(0);
      request.setAttribute("listeneraction", action.name());
      if (devices != null) {
        for (String device : devices) {
          Element d = doc.createElement("device");
          d.setAttribute("uri", device);
          request.appendChild(d);
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(ListenerMessage.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ListenerMessage.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

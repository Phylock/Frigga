/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol.message;

import dk.itu.frigga.device.Parameter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class StatusMessage extends DogMessage {

  public StatusMessage(String[] devices) {
    try {
      initialize();

      Element root = doc.getDocumentElement();
      Element statemessage = doc.createElement("statemessage");
      Element elm_devices = doc.createElement("devices");

      for (String device : devices) {
        Element d = doc.createElement("device");
        d.setAttribute("uri", device);
        elm_devices.appendChild(d);
      }
      statemessage.appendChild(elm_devices);
      root.appendChild(statemessage);
    } catch (SAXException ex) {
      Logger.getLogger(StatusMessage.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(StatusMessage.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

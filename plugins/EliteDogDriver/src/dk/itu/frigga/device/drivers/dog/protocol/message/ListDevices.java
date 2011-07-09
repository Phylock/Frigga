/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol.message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class ListDevices extends ConfigRequestMessage {

  /**
   * Generate a dog gateway to request known devices and where they are located
   * @param session session id
   * @param devices device filter, limit description to listed devices, use null to get all
   * @return the xml request
   * @throws ParserConfigurationException
   */
  public ListDevices(String[] devices) {
    super("alldevices");
    try {
      initialize();
      //only one request tag in the doc
      Node request = doc.getElementsByTagName("request").item(0);
      if (devices != null) {
        for (String device : devices) {
          Element d = doc.createElement("device");
          d.setAttribute("uri", device);
          request.appendChild(d);
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(ListDevices.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ListDevices.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

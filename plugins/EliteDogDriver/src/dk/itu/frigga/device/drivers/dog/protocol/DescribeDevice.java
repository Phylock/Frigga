/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class DescribeDevice extends ConfigRequestMessage {
  public DescribeDevice(String[] devices)
  {
    super("describe_device");
    try {
      initialize();
      Node request = doc.getElementsByTagName("request").item(0);
      if (devices != null) {
        for (String device : devices) {
          Element d = doc.createElement("device");
          d.setAttribute("uri", device);
          request.appendChild(d);
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(DescribeDevice.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(DescribeDevice.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

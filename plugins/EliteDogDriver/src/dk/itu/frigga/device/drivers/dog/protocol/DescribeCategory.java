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
public class DescribeCategory extends ConfigRequestMessage {

  /**
   * Generate describe device request for the dog gateway
   * @param session session id
   * @param devicecategories filter description to given device categories, use null to get all
   * @return the request in xml format
   * @throws ParserConfigurationException
   */
  public DescribeCategory(String[] devicecategories) {
    super("describe");
    try {
      initialize();
      Node request = doc.getElementsByTagName("request").item(0);
      if (devicecategories != null) {
        for (String category : devicecategories) {
          Element cat = doc.createElement("devicecategory");
          cat.setAttribute("uri", category);
          request.appendChild(cat);
        }
      }
    } catch (SAXException ex) {
      Logger.getLogger(DescribeCategory.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(DescribeCategory.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

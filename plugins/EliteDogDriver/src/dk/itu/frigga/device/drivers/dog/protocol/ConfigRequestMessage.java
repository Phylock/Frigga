/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public abstract class ConfigRequestMessage extends DogMessage {
  private String type;
  public ConfigRequestMessage(String type) {
        this.type = type;
  }

  @Override
  protected void initialize() throws SAXException, IOException {
    super.initialize();
    try {
      generateConfigRequest(doc, type);
    } catch (ParserConfigurationException ex) {
    }
  }

  /**
   * Template for all Config messages
   * @param session session id
   * @param configtype config request type
   * @return template document
   * @throws ParserConfigurationException
   */
  protected static void generateConfigRequest(Document doc, String configtype) throws ParserConfigurationException {
    Element root = doc.getDocumentElement();
    Element config = doc.createElement("configmessage");
    Element request = doc.createElement("request");
    request.setAttribute("configtype", configtype);

    config.appendChild(request);
    root.appendChild(config);

  }
}

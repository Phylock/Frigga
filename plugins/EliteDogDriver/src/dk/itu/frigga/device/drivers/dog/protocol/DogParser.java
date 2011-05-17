/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import dk.itu.frigga.device.drivers.dog.DogDeviceManager;
import dk.itu.frigga.device.drivers.dog.DogDriver;
import dk.itu.frigga.device.drivers.dog.StructureUpdate;
import dk.itu.frigga.utility.XmlHelper;
import it.polito.elite.domotics.dog2.dog2leash.interfaces.Dog2MessageListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.service.log.LogService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class DogParser implements Dog2MessageListener {

  private DogDriver driver;
  private LogService log;
  private static final Map<String, MessageParsable> parsers;

  static {
    Map<String, MessageParsable> list = new HashMap<String, MessageParsable>();

    //fill
    list.put("configmessage", new ConfigMessageParser());

    //write protection + speed
    parsers = Collections.unmodifiableMap(list);

  }

  public DogParser(DogDriver driver) {
    this.driver = driver;
  }

  public void parseMessage(Document doc, Element message) {
    String type = message.getTagName();

    if (parsers.containsKey(type)) {
      MessageParsable handler = parsers.get(type);
      StructureUpdate update = DogDeviceManager.instance().beginUpdate(driver.getDriverId());
      handler.parse(driver, update, doc, message);
      DogDeviceManager.instance().commit(update);
    } else {
      log.log(LogService.LOG_INFO, "Unhandled dog event: " + type);
    }
  }

  @Override
  public void newMessage(final String message) {
    log.log(LogService.LOG_INFO, "Recived message");
    new Thread(new Runnable() {

      public void run() {
        try {
          Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(message.getBytes("UTF-8")));
          Element root = (Element) doc.getDocumentElement();
          Element messagetype = XmlHelper.getFirstChildElement(root);

          parseMessage(doc, messagetype);

          //TODO: get id and notify the waiting thread
        } catch (ParserConfigurationException ex) {
          Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
          Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }).start();

  }
}

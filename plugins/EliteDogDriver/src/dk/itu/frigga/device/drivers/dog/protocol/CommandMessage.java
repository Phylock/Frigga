/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

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
public class CommandMessage extends DogMessage {

  public CommandMessage(Command command)
  {
    this(new Command[]{command});
  }

  /**
   * Generate a command message to send one or more commands to a list of devices,
   * @param session session id
   * @param commands the commands to send
   * @return the xml request as a string
   * @throws ParserConfigurationException
   */
  public CommandMessage(Command[] commands) {
    try {
      initialize();

      Element root = doc.getDocumentElement();
      Element commandmessage = doc.createElement("commandmessage");

      for (Command command : commands) {
        Element commanddevice = doc.createElement("commanddevice");
        Element devices_group = doc.createElement("devices");
        commanddevice.appendChild(devices_group);

        String[] devices = command.getDevices();
        for (String device : devices) {
          Element d = doc.createElement("device");
          d.setAttribute("uri", device);
          devices_group.appendChild(d);
        }

        Element c = doc.createElement("command");
        c.setAttribute("name", command.getCommand());

        Parameter[] parameters = command.getParameters();
        if (parameters != null) {
          for (Parameter param : parameters) {
            Element p = doc.createElement("param");
            p.setTextContent(param.getData().toString());
            c.appendChild(p);
          }
        }
        commanddevice.appendChild(c);
        commandmessage.appendChild(commanddevice);
      }

      root.appendChild(commandmessage);
    } catch (SAXException ex) {
      Logger.getLogger(CommandMessage.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(CommandMessage.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

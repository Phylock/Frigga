/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.manager.block.Condition;
import dk.itu.frigga.action.manager.block.Device;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class DeviceParser implements BlockParser {

  private static final String[] POSIBLE_CHILDREN = new String[]{"variable", "and","or"};
  public Condition parse(Document doc, Element element) {
    String id = element.getAttribute("name");
    Device device = new Device(id);
    if (id.startsWith("!")) {
      device.addReplacement(id.substring(1));
    }
    return device;
  }

  public String[] childElementsTypes() {
    return POSIBLE_CHILDREN;
  }
}

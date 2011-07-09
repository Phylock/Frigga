/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol.parser;

import dk.itu.frigga.device.VariableChangedEvent;
import dk.itu.frigga.device.VariableUpdate;
import dk.itu.frigga.device.drivers.dog.DogDeviceManager;
import dk.itu.frigga.device.drivers.dog.DogDriver;
import dk.itu.frigga.device.drivers.dog.StructureUpdate;
import dk.itu.frigga.device.drivers.dog.protocol.message.DogMessage;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class StateMessageParser implements MessageParsable {

  public StateMessageParser() {
  }

  @Override
  public void parse(DogDriver driver, StructureUpdate transaction, Document doc, Element element) {
    //String response = DogMessage.xmlToString(element);
    VariableChangedEvent vu = DogDeviceManager.instance().beginVariableUpdate();
    while(element != null)
    {
      String device = element.getAttribute("uri");
      addStates(vu, device, XmlHelper.getFirstChildElement(element, "currentstate"));
      element = XmlHelper.getNextSiblingElement(element, "statenotification");
    }
    DogDeviceManager.instance().commitVariableUpdate(vu);
  }

  private void addStates(VariableChangedEvent vu, String device, Element element)
  {
    while(element != null)
    {
      String name = element.getAttribute("name");
      String value = element.getAttribute("value");
      vu.getVariables().add(new VariableUpdate(device, name, value));
      element = XmlHelper.getNextSiblingElement(element, "currentstate");
    }
  }
}

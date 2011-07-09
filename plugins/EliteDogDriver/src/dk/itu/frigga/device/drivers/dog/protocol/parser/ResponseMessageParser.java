/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol.parser;

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
public class ResponseMessageParser implements MessageParsable {

  public ResponseMessageParser() {
  }

  @Override
  public void parse(DogDriver driver, StructureUpdate transaction, Document doc, Element element) {
    String response = element.getTextContent();
    //for now expect success :d

  }
}

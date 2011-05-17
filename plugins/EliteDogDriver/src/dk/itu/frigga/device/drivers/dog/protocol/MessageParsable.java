/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog.protocol;

import dk.itu.frigga.device.drivers.dog.DogDriver;
import dk.itu.frigga.device.drivers.dog.StructureUpdate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public interface MessageParsable {
    void parse(DogDriver driver, StructureUpdate transaction, Document doc, Element element);
}

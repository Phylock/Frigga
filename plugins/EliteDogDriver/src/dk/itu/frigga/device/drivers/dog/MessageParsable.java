/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public interface MessageParsable {
    void parse(DogDriver driver, StructureUpdate transaction, Document doc, Element element);
}

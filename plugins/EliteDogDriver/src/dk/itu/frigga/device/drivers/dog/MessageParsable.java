/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.manager.DeviceManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public interface MessageParsable {
    void setDeviceManager(DeviceManager manager);
    void parse(DogDriver driver, Document doc, Element element);
}

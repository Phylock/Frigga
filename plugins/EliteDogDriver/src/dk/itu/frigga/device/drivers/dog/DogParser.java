/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.utility.XmlHelper;
import it.polito.elite.domotics.dog2.dog2leash.interfaces.Dog2MessageListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class DogParser implements Dog2MessageListener {

    private DocumentBuilder builder;
    private DogDriver driver;
    private static final Map<String, MessageParsable> parsers;

    static {
        Map<String, MessageParsable> list = new HashMap<String, MessageParsable>();

        //fill
        list.put("configmessage", new ConfigMessageParser());

        //write protection + speed
        parsers = Collections.unmodifiableMap(list);

    }

    public DogParser(DogDriver driver) {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.driver = driver;
    }

    public void parseMessage(Document doc, Element message) {
        String type = message.getTagName();

        if (parsers.containsKey(type)) {
            MessageParsable handler = parsers.get(type);
            handler.parse(driver, doc, message);
        }
    }

    @Override
    public void newMessage(String message) {
        try {
            Document doc = builder.parse(new ByteArrayInputStream(message.getBytes("UTF-8")));
            Element root = (Element) doc.getDocumentElement();
            //TODO: get id and notify the waiting thread
            Element messagetype = XmlHelper.getFirstChildElement(root);

            parseMessage(doc, messagetype);

        } catch (SAXException ex) {
            Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDeviceManager(DeviceManager manager)
    {
        for(MessageParsable parser : parsers.values())
        {
            parser.setDeviceManager(manager);
        }
    }
}

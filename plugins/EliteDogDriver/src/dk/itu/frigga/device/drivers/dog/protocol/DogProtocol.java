/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.utility.XmlHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class DogProtocol {

    public enum ListenerActions {

        ADD, REMOVE, MODIFY
    }
    private static DocumentBuilder builder = null;
    private static final String BASICXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<dogmessage xmlns=\"http://elite.polito.it/domotics/dog2/DogMessage\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://elite.polito.it/domotics/dog2/DogMessage DogMessage.xsd\" >"
            + "</dogmessage>";

    //Initialize
    static {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Generate describe device request for the dog gateway
     * @param session session id
     * @param devicecategories filter description to given device categories, use null to get all
     * @return the request in xml format
     * @throws ParserConfigurationException
     */
    public static DogMessage generateDescribeDeviceCategory( String[] devicecategories) throws ParserConfigurationException {
        Document doc = generateConfigRequest("describe");

        //only one request tag in the doc
        Node request = doc.getElementsByTagName("request").item(0);
        if (devicecategories != null) {
            for (String category : devicecategories) {
                Element cat = doc.createElement("devicecategory");
                cat.setAttribute("uri", category);
                request.appendChild(cat);
            }
        }
        return new DogMessage(doc);
    }

    /**
     * Generate a dog gateway to request known devices and where they are located
     * @param session session id
     * @param devices device filter, limit description to listed devices, use null to get all
     * @return the xml request
     * @throws ParserConfigurationException
     */
    public static DogMessage generateDescribeDevices(String[] devices) throws ParserConfigurationException {
        Document doc = generateConfigRequest("alldevices");

        //only one request tag in the doc
        Node request = doc.getElementsByTagName("request").item(0);
        if (devices != null) {
            for (String device : devices) {
                Element d = doc.createElement("device");
                d.setAttribute("uri", device);
                request.appendChild(d);
            }
        }

        return new DogMessage(doc);
    }

    public static DogMessage generateDescribeDevice(String[] devices) throws ParserConfigurationException {
        Document doc = generateConfigRequest("describe_device");

        //only one request tag in the doc
        Node request = doc.getElementsByTagName("request").item(0);
        if (devices != null) {
            for (String device : devices) {
                Element d = doc.createElement("device");
                d.setAttribute("uri", device);
                request.appendChild(d);
            }
        }

        return new DogMessage(doc);
    }

    /**
     * Add or remove a listener to a device
     * @param session session id
     * @param devices device filter, use null for all devices
     * @param action can be "add" ...
     * @return the xml request
     * @throws ParserConfigurationException
     */
    public static DogMessage generateListenerRequest(String[] devices, ListenerActions action) throws ParserConfigurationException {
        Document doc = generateConfigRequest("LISTENER");

        //only one request tag in the doc
        Element request = (Element) doc.getElementsByTagName("request").item(0);
        request.setAttribute("listeneraction", action.name());
        if (devices != null) {
            for (String device : devices) {
                Element d = doc.createElement("device");
                d.setAttribute("uri", device);
                request.appendChild(d);
            }
        }

        return new DogMessage(doc);
    }

    /**
     * Generate a command message to send a command to a list of devices
     * @param session session id
     * @param command the command to send
     * @return the xml request as a string
     * @throws ParserConfigurationException
     */
    public static DogMessage generateCommandMessage(Command command) throws ParserConfigurationException {
        return generateCommandMessage(new Command[]{command});
    }

    /**
     * Generate a command message to send one or more commands to a list of devices,
     * @param session session id
     * @param commands the commands to send
     * @return the xml request as a string
     * @throws ParserConfigurationException
     */
    public static DogMessage generateCommandMessage(Command[] commands) throws ParserConfigurationException {
        Document doc = null;

        try {
            doc = newBasicTemplate();
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
                        //TODO: add parameters
                    }
                }
                commanddevice.appendChild(c);
                commandmessage.appendChild(commanddevice);
            }

            root.appendChild(commandmessage);
        } catch (SAXException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DogMessage(doc);
    }

    /**
     * Generate a request to get states the Status of one or more devices
     * @param session session id
     * @param devices list of devices
     * @return the xml request as a string
     * @throws ParserConfigurationException
     */
    public static DogMessage generateStatusRequest(String[] devices) throws ParserConfigurationException {
        Document doc = null;

        try {
            doc = newBasicTemplate();

            Element root = doc.getDocumentElement();
            Element state = doc.createElement("statemessage");
            Element devices_group = doc.createElement("devices");

            for (String device : devices) {
                Element d = doc.createElement("device");
                d.setAttribute("uri", device);
                devices_group.appendChild(d);
            }

            state.appendChild(devices_group);
            root.appendChild(state);
        } catch (SAXException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DogMessage(doc);
    }

    /**
     * Template for all Config messages
     * @param session session id
     * @param configtype config request type
     * @return template document
     * @throws ParserConfigurationException
     */
    private static Document generateConfigRequest(String configtype) throws ParserConfigurationException {
        Document doc = null;

        try {
            doc = newBasicTemplate();
            Element root = doc.getDocumentElement();
            Element config = doc.createElement("configmessage");
            Element request = doc.createElement("request");
            request.setAttribute("configtype", configtype);

            config.appendChild(request);
            root.appendChild(config);
        } catch (SAXException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }

    private static Document newBasicTemplate() throws SAXException, IOException {
        return builder.parse(new ByteArrayInputStream(BASICXML.getBytes("UTF-8")));
    }

    public static void setPriority(Document doc, int priority) {
        doc.getDocumentElement().setAttribute("priority", "" + priority);
    }

    public static void setMessageId(Document doc, String id) {
        doc.getDocumentElement().setAttribute("id", id);
    }

    public static void setSessionId(Document doc, String session) {
        Element root = XmlHelper.getFirstChildElement(doc.getDocumentElement());
        NodeList list = root.getElementsByTagName("session");
        Element current_session = null;
        if(list.getLength() > 0)
        {
            current_session = XmlHelper.getFirstElement(list);
        }
        if(current_session == null)
        {
            current_session = doc.createElement("session");
            doc.getDocumentElement().appendChild(current_session);
        }

        current_session.setTextContent(session);
    }
}

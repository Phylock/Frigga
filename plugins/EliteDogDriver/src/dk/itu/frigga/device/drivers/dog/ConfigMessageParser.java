/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.Executable;
import dk.itu.frigga.device.Location;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.utility.XmlHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author phylock
 */
public class ConfigMessageParser implements MessageParsable {

    private volatile DeviceManager devicemanager;

    private Map<String, LinkedList<Executable>> knownFunctionality;
    private Map<String, Executable> knownStates;

    private AsyncDeviceCategory unknown;

    public ConfigMessageParser() {
        knownFunctionality = new HashMap<String, LinkedList<Executable>>();
        knownStates = new HashMap<String, Executable>();
        unknown = new AsyncDeviceCategory();
    }

    @Override
    public void parse(DogDriver driver, Document doc, Element element) {
        //detect message subtype
        Element response = XmlHelper.getFirstChildElement(element);
        String subtype = response.getAttribute("configtype");

        if (subtype.equalsIgnoreCase("ALLDEVICES")
                || subtype.equalsIgnoreCase("DESCRIBE")
                || subtype.equalsIgnoreCase("DESCRIBE_DEVICE")) {

            updateDeviceManager(driver, doc, response);

        } else if (subtype.equalsIgnoreCase("LISTENER")) {
            //TODO: implement
        }
    }

    private void updateDeviceManager(DogDriver driver, Document doc, Element response) {
        Element functionalities = XmlHelper.getFirstElement(
                response.getElementsByTagName("functionalities"));
        Element states = XmlHelper.getFirstElement(
                response.getElementsByTagName("states"));
        Element devicecategory = XmlHelper.getFirstElement(
                response.getElementsByTagName("devicecategories"));
        Element devices = XmlHelper.getFirstElement(
                response.getElementsByTagName("devices"));

        if (functionalities != null) {
            parseFunctionality(driver, doc, functionalities);
        }
        if (states != null) {
            parseStates(doc, states);
        }
        if (devicecategory != null) {
            updateDeviceCategories(doc, devicecategory);
        }
        if (devices != null) {
            updateDevice(driver, doc, devices);
        }
    }

    private void updateDeviceCategories(Document doc, Element categories) {
        NodeList devicecategries = categories.getElementsByTagName("devicecategory");

        ArrayList<String> variables = new ArrayList<String>();
        ArrayList<String> listeners = new ArrayList<String>();

        for (int i = 0; i < devicecategries.getLength(); i++) {
            Node node = devicecategries.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element devicecategory = (Element) node;
                String name = devicecategory.getAttribute("uri");

                if (devicemanager.getDeviceCategory(name) == null) {

                    HashMap<String, Executable> functionalies = new HashMap<String, Executable>();
                    variables.clear();
                    listeners.clear();

                    Element contains = XmlHelper.getFirstChildElement(devicecategory);
                    while (contains != null) {
                        if (contains.getNodeName().equalsIgnoreCase("devicestates")) {
                            NodeList states = contains.getElementsByTagName("devicestate");
                            for (int j = 0; j < states.getLength(); j++) {
                                Node statenode = states.item(j);
                                if (statenode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element state = (Element) statenode;
                                    variables.add(state.getTextContent());
                                }
                            }

                        } else if (contains.getNodeName().equalsIgnoreCase("devicefunctionalities")) {
                            NodeList functionality = contains.getElementsByTagName("devicefunctionality");
                            for (int j = 0; j < functionality.getLength(); j++) {
                                Node functionalitynode = functionality.item(j);
                                if (functionalitynode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element function = (Element) functionalitynode;
                                    String func = function.getTextContent();
                                    LinkedList<Executable> functions = knownFunctionality.get(func);
                                    for(Executable f : functions)
                                    {
                                        functionalies.put(f.getName(), f);
                                    }
                                }
                            }
                        } else if (contains.getNodeName().equalsIgnoreCase("devicenotifications")) {
                            NodeList notifications = contains.getElementsByTagName("devicenotification");
                            for (int j = 0; j < notifications.getLength(); j++) {
                                Node notificationnode = notifications.item(j);
                                if (notificationnode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element notification = (Element) notificationnode;
                                    listeners.add(notification.getTextContent());
                                }
                            }
                        }
                        contains = XmlHelper.getNextSiblingElement(contains);
                    }
                    DeviceCategory category = new DeviceCategory(name, functionalies,
                            variables.toArray(new String[variables.size()]),
                            listeners.toArray(new String[listeners.size()]));
                    devicemanager.registerDeviceCategory(category);
                    if(unknown.isDeviceCategoryUnknown(name))
                    {
                        unknown.makeDeviceCategoryKnown(category);
                    }

                }
            }
        }
    }

    private void parseStates(Document doc, Element states) {
        //TODO: implement
    }

    private void parseFunctionality(DogDriver driver, Document doc, Element functionalities) {
        NodeList items = functionalities.getElementsByTagName("functionality");

        for (int i = 0; i
                < items.getLength(); i++) {
            Node node = items.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element functionality = (Element) node;
                String name = functionality.getAttribute("uri");

                if (!knownFunctionality.containsKey(name)) {
                    Element commands = XmlHelper.getFirstChildElement(functionality);
                    NodeList commandslist = commands.getElementsByTagName("command");

                    LinkedList<Executable> list = new LinkedList<Executable>();
                    for (int j = 0; j
                            < commandslist.getLength(); j++) {
                        Node commandnode = commandslist.item(j);

                        if (commandnode.getNodeType() == Node.ELEMENT_NODE) {
                            Element command = (Element) commandnode;
                            String commandname = command.getAttribute("name");
                            Executable function = new Function(driver, driver.getConnection(), commandname);

                            int numparameters = Integer.parseInt(command.getAttribute("numparameters"));
                            //TODO: add parameters to function

                            list.add(function);
                        }
                    }
                    knownFunctionality.put(name, list);
                }
            }
        }
    }

    private void updateDevice(DogDriver driver, Document doc, Element devices) {
        NodeList items = devices.getElementsByTagName("device");

        for (int i = 0; i < items.getLength(); i++) {
            Node node = items.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element device = (Element) node;
                DeviceId id = new DeviceId(device.getAttribute("uri"));
                String devicecategory = device.getAttribute("devicecategory");

                //if Device is unknown to DeviceManager
                if (devicemanager.getDeviceById(id) == null) {
                    NodeList locations = device.getElementsByTagName("isIn");
                    
                    for (int j = 0; j < locations.getLength(); j++) {
                        Node locationnode = locations.item(j);

                        if (locationnode.getNodeType() == Node.ELEMENT_NODE) {
                            Element location = (Element) locationnode;
                            String locationname = location.getTextContent();
                            //TODO: add location to device
                        }
                    }


                    DeviceCategory category = devicemanager.getDeviceCategory(devicecategory);
                    if (category != null) {
                        Device d = new Device(id, category, null, null, null);
                        devicemanager.registerDevice(d);
                    } else {
                        Device d = new Device(id, category, null, null, null);
                        unknown.addUnknownDeviceCategory(devicecategory, d);
                    }
                }
            }
        }
        if (unknown.hasUnknown()) {
            try {
                //NOTE : possible loop, if the DOG gateway donsn't know the device, will this be a problem?
                DogMessage message = DogProtocol.generateDescribeDeviceCategory(unknown.getUnknownDeviceCategories());
                driver.getConnection().send(message);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ConfigMessageParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setDeviceManager(DeviceManager manager) {
        devicemanager = manager;
    }
}

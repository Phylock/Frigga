/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol.parser;

import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.Executable;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import dk.itu.frigga.device.drivers.dog.DogDriver;
import dk.itu.frigga.device.drivers.dog.Function;
import dk.itu.frigga.device.drivers.dog.StructureUpdate;
import dk.itu.frigga.utility.XmlHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author phylock
 */
public class ConfigMessageParser implements MessageParsable {

  private Map<String, LinkedList<Executable>> knownFunctionality;
  private Set<String> knownStates;

  public ConfigMessageParser() {
    knownFunctionality = Collections.synchronizedMap(new HashMap<String, LinkedList<Executable>>());
    knownStates = Collections.synchronizedSet(new HashSet<String>());
  }

  @Override
  public void parse(DogDriver driver, StructureUpdate transaction, Document doc, Element element) {
    //detect message subtype
    Element response = XmlHelper.getFirstChildElement(element);
    String subtype = response.getAttribute("configtype");

    if (subtype.equalsIgnoreCase("ALLDEVICES")
            || subtype.equalsIgnoreCase("DESCRIBE")
            || subtype.equalsIgnoreCase("DESCRIBE_DEVICE")) {

      updateDeviceManager(driver, transaction, doc, response);

    } else if (subtype.equalsIgnoreCase("LISTENER")) {
      //TODO: implement
    }
  }

  private void updateDeviceManager(DogDriver driver, StructureUpdate transaction, Document doc, Element response) {
    Element functionalities = XmlHelper.getFirstElement(
            response.getElementsByTagName("functionalities"));
    Element states = XmlHelper.getFirstElement(
            response.getElementsByTagName("states"));
    Element devicecategory = XmlHelper.getFirstElement(
            response.getElementsByTagName("devicecategories"));
    Element devices = XmlHelper.getFirstElement(
            response.getElementsByTagName("devices"));

    if (functionalities != null) {
      parseFunctionality(driver, transaction, doc, functionalities);
    }
    if (states != null) {
      parseStates(doc, transaction, states);
    }
    if (devicecategory != null) {
      updateDeviceCategories(doc, transaction, devicecategory);
    }
    if (devices != null) {
      updateDevice(driver, transaction, doc, devices);
    }
  }

  private void updateDeviceCategories(Document doc, StructureUpdate transaction, Element categories) {
    NodeList devicecategries = categories.getElementsByTagName("devicecategory");

    ArrayList<String> variables = new ArrayList<String>();
    ArrayList<String> listeners = new ArrayList<String>();

    for (int i = 0; i < devicecategries.getLength(); i++) {
      Node node = devicecategries.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element devicecategory = (Element) node;
        String name = devicecategory.getAttribute("uri");

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
                for (Executable f : functions) {
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
        CategoryDescriptor category = new CategoryDescriptor(name,
                variables.toArray(new String[variables.size()]),
                functionalies.keySet().toArray(new String[functionalies.size()]));
        transaction.addCategory(category);
      }
    }
  }

  private void parseStates(Document doc, StructureUpdate transaction, Element states) {
    Element current = XmlHelper.getFirstChildElement(states, "state");
    while (current != null) {
      String uri = current.getAttribute("uri");
      Element enumstate = XmlHelper.getFirstChildElement(current, "possiblestates");
      if (enumstate != null) {
        transaction.addVariable(new VariableDescriptor(uri, "string"));
      } else {
        String type = current.getAttribute("type");
        if ("float".equalsIgnoreCase(type)) {
          long min = Long.parseLong(current.getAttribute("min"));
          long max = Long.parseLong(current.getAttribute("min"));
          //TODO: use these values

        }
        transaction.addVariable(new VariableDescriptor(uri, type));
      }
      knownStates.add(uri);
      current = XmlHelper.getNextSiblingElement(current, "state");
    }
  }

  private void parseFunctionality(DogDriver driver, StructureUpdate transaction, Document doc, Element functionalities) {
    NodeList items = functionalities.getElementsByTagName("functionality");

    for (int i = 0; i < items.getLength(); i++) {
      Node node = items.item(i);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element functionality = (Element) node;
        String name = functionality.getAttribute("uri");

        if (!knownFunctionality.containsKey(name)) {
          Element commands = XmlHelper.getFirstChildElement(functionality);
          NodeList commandslist = commands.getElementsByTagName("command");

          LinkedList<Executable> list = new LinkedList<Executable>();
          for (int j = 0; j < commandslist.getLength(); j++) {
            Node commandnode = commandslist.item(j);

            if (commandnode.getNodeType() == Node.ELEMENT_NODE) {
              Element command = (Element) commandnode;
              String commandname = command.getAttribute("name");
              Executable function = new Function(driver, driver.getConnection(), commandname);

              int numparameters = Integer.parseInt(command.getAttribute("numparameters"));
              if (commandname.equalsIgnoreCase("CONTINUOS")) {
                //TODO: handle CONTINUOS command
              } else {
                if (numparameters == 0) {
                  transaction.addFunction(new FunctionDescriptor(commandname, null));
                } else {
                  //TODO: add parameters to function
                  transaction.addFunction(new FunctionDescriptor(commandname, null));
                }
                list.add(function);
              }
            }
          }
          knownFunctionality.put(name, list);
        }
      }
    }
  }

  private void updateDevice(DogDriver driver, StructureUpdate transaction, Document doc, Element devices) {
    NodeList items = devices.getElementsByTagName("device");

    for (int i = 0; i < items.getLength(); i++) {
      Node node = items.item(i);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element device = (Element) node;
        DeviceId id = new DeviceId(device.getAttribute("uri"));
        String devicecategory = device.getAttribute("devicecategory");

        NodeList locations = device.getElementsByTagName("isIn");

        for (int j = 0; j < locations.getLength(); j++) {
          Node locationnode = locations.item(j);

          if (locationnode.getNodeType() == Node.ELEMENT_NODE) {
            Element location = (Element) locationnode;
            String locationname = location.getTextContent();
            //TODO: add location to device
          }
        }

        DeviceDescriptor d = new DeviceDescriptor(id.toString().replaceAll("_", " "), id.toString(), new String[]{devicecategory});
        transaction.addDevice(d);
      }

    }
  }
}

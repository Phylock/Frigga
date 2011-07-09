/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.Singleton;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.drivers.dog.protocol.message.ListenerMessage;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

/**
 *
 * @author phylock
 */
public class DogDeviceManager extends Singleton {

  private Publisher event;
  private Connection connection;
  private final Map<DeviceId, Device> devices = new HashMap<DeviceId, Device>();
  private final Map<String, Category> categories = new HashMap<String, Category>();
  private static DogDeviceManager instance = new DogDeviceManager();

  private DogDeviceManager() {
  }

  public StructureUpdate beginUpdate(String responsible) {
    return new StructureUpdate(responsible);
  }

  public void commit(StructureUpdate update) {
    //add as listener
    List<String> newDevices = new LinkedList<String>();
    for (DeviceDescriptor device : update.getDevices()) {
      newDevices.add(device.getSymbolic());
    }
    if(!newDevices.isEmpty())
    {
      connection.send(new ListenerMessage(newDevices.toArray(new String[0]), ListenerMessage.ListenerActions.ADD));
    }

    event.sendData((DeviceUpdateEvent) update);
  }

  public static DogDeviceManager instance() {
    return instance;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.Singleton;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.model.Category;
import dk.itu.frigga.device.model.Device;
import java.util.HashMap;
import java.util.Map;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

/**
 *
 * @author phylock
 */
public class DogDeviceManager extends Singleton {
    private Publisher event;

    private final Map<DeviceId, Device> devices = new HashMap<DeviceId, Device>();
    private final Map<String, Category> categories = new HashMap<String, Category>();

    private static DogDeviceManager instance = new DogDeviceManager();

    private DogDeviceManager()
    {
    }
    
    public StructureUpdate beginUpdate(String responsible)
    {
        return new StructureUpdate(responsible);
    }
    
    public void commit(StructureUpdate update)
    {
        //TODO: do local checks and updates here

        event.sendData((DeviceUpdateEvent)update);
    }

    public static DogDeviceManager instance()
    {
        return instance;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.Singleton;
import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceCategory;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdateEvent;
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
    private final Map<String, DeviceCategory> categories = new HashMap<String, DeviceCategory>();

    private static DogDeviceManager instance = new DogDeviceManager();

    private DogDeviceManager()
    {
    }
    
    public Transaction beginTransaction()
    {
        return new Transaction();
    }
    
    public void commit(Transaction transaction)
    {
        //TODO: do local checks and updates here

        event.sendData(new DeviceUpdateEvent(transaction.getDevices(), transaction.getCategories()));
    }

    public static DogDeviceManager instance()
    {
        return instance;
    }
}

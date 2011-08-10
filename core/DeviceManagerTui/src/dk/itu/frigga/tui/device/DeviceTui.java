/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.tui.device;

import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import java.util.LinkedList;
import java.util.List;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author phylock
 */
public class DeviceTui {

    public static final String SCOPE = "frigga";
    public static final String FUNCTIONS[] = new String[]{
        "call", "device","devices"
    };
    private BundleContext context;

    public DeviceTui(BundleContext context) {
        this.context = context;
    }

    public Device device(String symbolic)
    {
        DeviceManager manager = getDeviceManager();
        return manager.getDeviceById(new DeviceId(symbolic));
    }

    public List<Device> devices()
    {
        List<Device> devices = new LinkedList<Device>();
        DeviceManager manager = getDeviceManager();
        Iterable<Device> list = manager.getDevices();
        for(Device d : list)
        {
            devices.add(d);
        }
        
        return devices;
    }

    /*@Descriptor("Call a function in a device")*/
    public void call(
            /*@Descriptor("device")*/String device,
            /*@Descriptor("function")*/ String function,
            /*@Descriptor("parameter")*/ String param[]) {
        DeviceManager manager = getDeviceManager();
        Device d = manager.getDeviceById(new DeviceId(device));
        if (d == null) {
            throw new RuntimeException("Unknown Device");
        }

        Parameter paramerters[] = new Parameter[param.length];
        for(int i = 0; i < param.length; i++)
        {
            int split = param[i].indexOf("=");
            String key;
            String value;
            if(split >= 1)
            {
               key = param[i].substring(0,split);
               value = param[i].substring(split+1);
            }else{
                key = "";
                value = param[i];
            }
            paramerters[i] = new Parameter(key, value);
        }

        manager.callFunction(function, new Device[]{d}, paramerters);
    }

    /*@Descriptor("Call a function in a device")*/
    public void call(
            /*@Descriptor("device")*/String device,
            /*@Descriptor("function")*/ String function) {
        call(device, function, new String[0]);
    }

    protected DeviceManager getDeviceManager() {
        ServiceReference ref = context.getServiceReference(DeviceManager.class.getName());
        if (ref == null) {
            throw new RuntimeException("DeviceManger is not registered");
        }
        DeviceManager manager = (DeviceManager) context.getService(ref);
        if (manager == null) {
            throw new RuntimeException("DeviceManger is not registered");
        }
        return manager;
    }
}

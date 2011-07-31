/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.tui.device;

import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.Parameter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author phylock
 */
public class DeviceTui {
    public static final String SCOPE = "frigga";
    public static final String FUNCTIONS[] = new String[]{
        "call"
    };

    private BundleContext context;

    public DeviceTui(BundleContext context) {
        this.context = context;
    }

    /*@Descriptor("Call a function in a device")*/
    public void call(
            /*@Descriptor("device")*/ String device,
            /*@Descriptor("function")*/ String function,
            /*@Descriptor("parameter")*/ String param)
    {
        DeviceManager manager = getDeviceManager();
        Device d = manager.getDeviceById(new DeviceId(device));
        if(d == null)
        {
            throw new RuntimeException("Unknown Device");
        }


        Parameter p = new Parameter("", param);
        manager.callFunction(function, new Device[]{d}, p);
    }

    protected DeviceManager getDeviceManager()
    {
        ServiceReference ref = context.getServiceReference(DeviceManager.class.getName());
        if(ref == null)
        {
            throw new RuntimeException("DeviceManger is not registered");
        }
        DeviceManager manager = (DeviceManager)context.getService(ref);
        if(manager == null)
        {
            throw new RuntimeException("DeviceManger is not registered");
        }
        return manager;
    }

}

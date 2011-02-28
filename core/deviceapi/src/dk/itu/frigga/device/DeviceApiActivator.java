/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

import dk.itu.frigga.device.manager.DeviceManager;
import dk.itu.frigga.device.manager.impl.DeviceManagerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class DeviceApiActivator implements BundleActivator{

    public void start(BundleContext bc) throws Exception {
        bc.registerService(DeviceManager.class.getName(), DeviceManagerImpl.getInstance(), null);
    }

    public void stop(BundleContext bc) throws Exception {
        
    }

}

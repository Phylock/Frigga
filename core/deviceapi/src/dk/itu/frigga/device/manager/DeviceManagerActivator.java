/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.manager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class DeviceManagerActivator implements BundleActivator{

    public void start(BundleContext bc) throws Exception {
        bc.registerService(DeviceManager.class.getName(), DeviceManager.getInstance(), null);
    }

    public void stop(BundleContext bc) throws Exception {
        
    }

}

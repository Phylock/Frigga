package dk.itu.frigga.device;

import dk.itu.frigga.device.manager.DeviceManager;
import dk.itu.frigga.device.manager.impl.DeviceManagerImpl;
import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class Activator extends DependencyActivatorBase {

  @Override
  public void init(BundleContext bc, DependencyManager dm) throws Exception {
    System.out.println("Device Api Activated");
    dm.add(createService()
            .setInterface(DeviceManager.class.getName(), null)
            .setImplementation(DeviceManagerImpl.getInstance())
            .add(createServiceDependency()
                .setService(Driver.class)
                .setRequired(false)
                .setCallbacks("deviceDriverAdded", "deviceDriverRemoved")));
  }

  @Override
  public void destroy(BundleContext bc, DependencyManager dm) throws Exception {
      System.out.println("Device Api Deactivated");
  }
}

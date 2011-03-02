/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.manager.DeviceManager;
import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class Activator extends DependencyActivatorBase implements BundleActivator{
  private DogDriver driver;
  @Override
  public void init(BundleContext bc, DependencyManager dm) throws Exception {
      System.out.println("Dog Driver Activated");
    driver = new DogDriver();

    dm.add(createService()
            .setInterface(Driver.class.getName(), null)
            .setImplementation(driver)
            .add(createServiceDependency()
                .setService(DeviceManager.class)
                .setRequired(true))
            .add(createServiceDependency()
                .setService(LogService.class)
                .setRequired(false)
                .setDefaultImplementation(new NullLog())));

    driver.connect(DogDriver.DEFAULT_DOG_ADDRESS);
  }

  @Override
  public void destroy(BundleContext bc, DependencyManager dm) throws Exception 
  {
    driver.disconnect();
    System.out.println("Dog Driver Deactivated");
  }

}

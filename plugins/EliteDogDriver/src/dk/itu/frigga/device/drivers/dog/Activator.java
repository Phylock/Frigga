/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.manager.DeviceManager;
import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class Activator extends DependencyActivatorBase{

  @Override
  public void init(BundleContext bc, DependencyManager dm) throws Exception {
    dm.add(createService()
            .setImplementation(DogDriver.class)
            .add(createServiceDependency()
                .setService(DeviceManager.class)
                .setRequired(true))
            .add(createServiceDependency()
                .setService(LogService.class)
                .setRequired(false)));
  }

  @Override
  public void destroy(BundleContext bc, DependencyManager dm) throws Exception {}

}

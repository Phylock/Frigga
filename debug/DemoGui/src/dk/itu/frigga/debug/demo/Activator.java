/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.demo;

import dk.itu.frigga.debug.demo.gui.MainWindow;
import dk.itu.frigga.device.manager.DeviceManager;
import java.util.List;
import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class Activator extends DependencyActivatorBase
{
    private MainWindow window;

    @Override
    public void destroy(BundleContext context, DependencyManager dm) throws Exception {
        window.dispose();
    }

    @Override
    public void init(BundleContext context, DependencyManager dm) throws Exception {
        window = new MainWindow(context);
        window.setVisible(true);
        dm.add(createService()
            .setImplementation(window)
            .add(createServiceDependency()
                .setService(DeviceManager.class)
                .setRequired(true))
            .add(createServiceDependency()
                .setService(LogService.class)
                .setRequired(false)));
        List list = dm.getServices();
    }
}

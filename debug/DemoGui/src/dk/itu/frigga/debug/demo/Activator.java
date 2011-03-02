/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.debug.demo;

import dk.itu.frigga.debug.demo.gui.MainWindow;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class Activator implements BundleActivator{
    private MainWindow window;
	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
      System.out.println("Start GUI");
        window = new MainWindow(context);
        window.setVisible(true);
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		window.dispose();
        System.out.println("Stop GUI");
	}
}

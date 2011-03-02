/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Tommy
 */
public class Activator implements BundleActivator
{
    // Bundle's context.
    private BundleContext context = null;

    public void start(BundleContext bc)
    {
        context = bc;

        synchronized (this)
        {
            bc.registerService(ConfigManager.class.getName(), ConfigManager.getInstance(), null);
        }
    }

    public void stop(BundleContext bc)
    {
    }

}

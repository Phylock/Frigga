/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.tui.data;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class Activator implements BundleActivator{

    public void start(BundleContext bc) throws Exception {
       Dictionary<String,Object> props = new Hashtable<String, Object>();
       props.put("osgi.command.scope", DataTui.SCOPE);
       props.put("osgi.command.function", DataTui.FUNCTIONS);

       bc.registerService(DataTui.class.getName(), new DataTui(bc), props);
    }

    public void stop(BundleContext bc) throws Exception {
        
    }

}

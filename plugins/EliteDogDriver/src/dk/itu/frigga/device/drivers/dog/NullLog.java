/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class NullLog implements LogService{

    public void log(int i, String string) {
    }

    public void log(int i, String string, Throwable thrwbl) {

    }

    public void log(ServiceReference sr, int i, String string) {

    }

    public void log(ServiceReference sr, int i, String string, Throwable thrwbl) {

    }

}

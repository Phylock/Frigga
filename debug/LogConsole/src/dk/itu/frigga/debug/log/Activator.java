/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.log;

import dk.itu.frigga.debug.log.impl.LogWindow;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogReaderService;

/**
 *
 * @author phylock
 */
public class Activator implements BundleActivator {

  private LogWindow window;

  public void start(BundleContext bc) throws Exception {
    window = new LogWindow(bc);

    ServiceReference ref = bc.getServiceReference(LogReaderService.class.getName());
    if (ref != null) {
      LogReaderService reader = (LogReaderService) bc.getService(ref);
      reader.addLogListener(window);
      Enumeration<LogEntry> e = reader.getLog();
      List<LogEntry> entries = new LinkedList<LogEntry>();
      while(e.hasMoreElements())
      {
        entries.add(e.nextElement());
      }
      window.fillLogEnties(entries.toArray(new LogEntry[entries.size()]));
      
    }
    window.setVisible(true);
  }

  public void stop(BundleContext bc) throws Exception {

    window.dispose();
    window = null;
  }
}

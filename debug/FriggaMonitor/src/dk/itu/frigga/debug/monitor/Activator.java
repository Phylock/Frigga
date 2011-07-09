/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.monitor;

import dk.itu.frigga.debug.monitor.impl.LogWindow;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
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
      while (e.hasMoreElements()) {
        entries.add(e.nextElement());
      }
      window.fillLogEnties(entries.toArray(new LogEntry[entries.size()]));
    }

    String[] topics = new String[]{
      "*"
    };

    Dictionary props = new Properties();
    props.put(EventConstants.EVENT_TOPIC, topics);
    bc.registerService(EventHandler.class.getName(), new AllEventHandler(), props);

    window.setVisible(true);
  }

  public void stop(BundleContext bc) throws Exception {
    window.dispose();
    window = null;
  }

  private class AllEventHandler implements EventHandler {

    public void handleEvent(Event event) {
      StringBuilder sb = new StringBuilder();
      sb.append(event.getTopic());
      sb.append(" (");
      for (String key : event.getPropertyNames()) {
        if (!key.startsWith("service")) {
          sb.append(key);
          sb.append(": ");
          sb.append(event.getProperty(key));
          sb.append(", ");
        }
      }
      sb.delete(sb.length() - 2, sb.length()); //remove the last seperator
      sb.append(")");
      window.addEvent(sb.toString());
    }
  }
}

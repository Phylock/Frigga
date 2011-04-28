/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ActionManager;
import dk.itu.frigga.action.manager.parser.TemplateParser;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.action.Context;
import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.DeviceManager;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class ActionManagerImpl implements ActionManager {
  /* iPOJO services */

  private LogService log;
  private DataManager datamanager;
  private DeviceManager devicemanager;
  //local
  private final Map<String, Template> templates;
  private final Map<String, Context> contexts;
  private final TemplateParser parser;

  private ConnectionPool cpool;

  private BundleContext bc;

  public ActionManagerImpl(BundleContext bc) {
    this.bc = bc;
    templates = Collections.synchronizedMap(new HashMap<String, Template>());
    contexts = Collections.synchronizedMap(new HashMap<String, Context>());
    parser = new TemplateParser();
  }

  public Context getContext(String id) {
    return contexts.get(id);
  }

  public Template getTemplate(String id) {
    return templates.get(id);
  }

  public void LoadTemplate(File file) throws IOException, SAXException {
    try {
      Template template = parser.parse(file);
      templates.put(template.getInfo().getName(), template);
    } catch (ParserConfigurationException ex) {
      log.log(LogService.LOG_ERROR, "Fatel error, ActionManager is stopping",ex);
      try {
        bc.getBundle().stop();
      } catch (BundleException bex) {
      }
    }
  }

  public void CompileTemplate(Template template, Map<String,String> replace) {
    Context c = new Context(template, replace);
  }

  private void validate()
  {
    if(datamanager.hasConnection("device"))
    {
      try {
        cpool = datamanager.requestConnection("device");
      } catch (SQLException ex) {
        Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (DataGroupNotFoundException ex) {
        Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
      } catch (UnknownDataDriverException ex) {
        Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private void invalidate()
  {
    cpool = null;
  }
}

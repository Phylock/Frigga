package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ActionManager;
import dk.itu.frigga.action.Context;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.DataGroupNotFoundException;
import dk.itu.frigga.data.DataManager;
import dk.itu.frigga.data.UnknownDataDriverException;
import dk.itu.frigga.device.DeviceManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author phylock
 */
public class ActionManagerImpl implements ActionManager
{
    /* iPOJO services */

    private LogService log;
    private DataManager datamanager;
    private DeviceManager devicemanager;
    //local
    private final Map<String, Template> templates;
    private final Map<String, Context> contexts;
    private ConnectionPool cpool;
    private BundleContext bc;
    private final ActionWorker actionworker;

    public ActionManagerImpl(BundleContext bc)
    {
        this.bc = bc;
        templates = Collections.synchronizedMap(new HashMap<String, Template>());
        contexts = Collections.synchronizedMap(new HashMap<String, Context>());
        actionworker = new ActionWorker();
    }

    public Collection<Template> getTemplates()
    {
        return Collections.unmodifiableCollection(templates.values());
    }

    public Context getContext(String id)
    {
        return contexts.get(id);
    }

    public Template getTemplate(String id)
    {
        return templates.get(id);
    }

    public boolean hasTemplate(String id)
    {
        return templates.containsKey(id);
    }

    public void removeTemplate(String id)
    {
        templates.remove(id);
    }

    public String LoadTemplate(File file) throws IOException, SAXException
    {
        String id = "";
        /*try
        {
            Template template = parser.parse(file);
            id = template.getTemplateInfo().getName();
            templates.put(id, template);
        }
        catch (ParserConfigurationException ex)
        {
            log.log(LogService.LOG_ERROR, "Fatel error, ActionManager is stopping", ex);
            try
            {
                bc.getBundle().stop();
            }
            catch (BundleException bex)
            {
            }
        }*/
        return id;
    }

    public void CompileTemplate(File action) throws FileNotFoundException, IOException
    {
        Properties loader = new Properties();
        loader.load(new BufferedInputStream(new FileInputStream(action)));
        if (loader.containsKey(ACTION_TEMPLATE_KEY) && loader.containsKey(ACTION_ID_KEY))
        {
            Map<String, String> prop = new HashMap<String, String>((Map) loader);
            String id = prop.get(ACTION_ID_KEY);
            String[] action_templates = prop.get(ACTION_TEMPLATE_KEY).toString().split(";");
            for (String current : action_templates)
            {
                if (templates.containsKey(current))
                {
                    CompileTemplate(id, templates.get(current), prop);
                }
                else
                {
                    log.log(LogService.LOG_WARNING, "Unknown template name '" + current + "', action compilation skipped");
                }
            }

        }
        else
        {
            //TODO: Throws invalid action file
        }
    }

    public void CompileTemplate(String id, Template template, Map<String, String> replace)
    {
        Context c = new Context(id, template, replace);
        /*Map<String, RuleTemplate> rules = template.getRules();
        for (RuleTemplate rule : rules.values())
        {
            RuleSql r = new RuleSql(c, rule);
            r.setDeviceDatabaseConnectionPool(cpool);
            c.addRule(r);
            actionworker.addRule(r);
        }*/
        contexts.put(id, c);
    }

    private void validate()
    {
        if (datamanager.hasConnection("device"))
        {
            try
            {
                cpool = datamanager.requestConnection("device");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (DataGroupNotFoundException ex)
            {
                Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (UnknownDataDriverException ex)
            {
                Logger.getLogger(ActionManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            //TODO: investegate if this could happen, if it can wait for device database to be ready
        }
    }

    private void invalidate()
    {
        cpool = null;
    }
}

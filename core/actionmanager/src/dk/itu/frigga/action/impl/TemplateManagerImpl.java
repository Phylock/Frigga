package dk.itu.frigga.action.impl;

import dk.itu.frigga.FriggaException;
import dk.itu.frigga.action.*;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.device.DeviceManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
//@Component
//@Provides
//@Instantiate
public class TemplateManagerImpl implements TemplateManager
{
    private final Set<TemplateData> templates = Collections.synchronizedSet(new LinkedHashSet<TemplateData>());
    private final Map<String, TemplateInstanceImpl> templateInstances = Collections.synchronizedMap(new HashMap<String, TemplateInstanceImpl>());
    private final List<TemplateLoadedListener> templateLoadedListeners = Collections.synchronizedList(new LinkedList<TemplateLoadedListener>());

    private DeviceManager deviceManager;
    private InstanceRunner instanceRunner = new InstanceRunner();

    public TemplateManagerImpl()
    {
    }

    public void loadInstancesFromDisk()
    {
        File path = new File("conf/instances/");
        File[] files = path.listFiles();

        for (File file : files)
        {
            try
            {
                FileInputStream fileStream = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(fileStream);
                loadBinds(properties);
            }
            catch (IOException e)
            {
                // Ignore this file
            }
        }
    }

    @Override
    public void loadTemplatesFromDisk()
    {
        File path = new File("conf/templates/");
        File[] files = path.listFiles();

        for (File file : files)
        {
            try
            {
                this.loadTemplateFromFile(file.getAbsolutePath());
            }
            catch (IOException e)
            {
                // Ignore this file
            }
            catch (FriggaException e)
            {
                // Ignore this file
            }
        }
    }

    private void loadBinds(final Properties properties)
    {
        String id = properties.getProperty("instance.id", UUID.randomUUID().toString());

        if (!templateInstances.containsKey(id))
        {
            String templateName = properties.getProperty("template.name", "");

            for (TemplateData template : templates)
            {
                if (template.getTemplateInfo().getName().equals(templateName))
                {
                    TemplateInstanceImpl instance = new TemplateInstanceImpl(id, template);
                    templateInstances.put(id, instance);
                    break;
                }
            }
        }
    }

    @Override
    public int getTemplateCount()
    {
        return templates.size();
    }

    @Override
    public TemplateInfo getTemplateInfo(final int index) throws TemplateNotFoundException
    {
        Iterator<TemplateData> iterator = templates.iterator();

        int left = index;
        while (iterator.hasNext())
        {
            Template template = iterator.next();
            if (left == 0)
            {
                return template.getTemplateInfo();
            }

            left--;
        }

        throw new TemplateNotFoundException();
    }

    @Override
    public void loadTemplateFromFile(final String filename) throws FileNotFoundException, InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException
    {
        loadTemplateFromStream(new FileInputStream(filename));
    }

    @Override
    public void loadTemplateFromString(String templateData) throws InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException
    {
        loadTemplateFromSource(new InputSource(new StringReader(templateData)));
    }

    @Override
    public void loadTemplateFromStream(final InputStream stream) throws InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException
    {
        loadTemplateFromSource(new InputSource(stream));
    }

    protected void loadTemplateFromSource(final InputSource source) throws InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException
    {
        TemplateData templateData = new TemplateData(deviceManager);
        try
        {
            templateData.loadFromSource(source);

            for (TemplateLoadedListener listener : templateLoadedListeners)
            {
                listener.templateLoaded(this, templateData);
            }

            templates.add(templateData);
        }
        catch (ParserConfigurationException e)
        {
            throw new InvalidTemplateFormatException();
        }
        catch (IOException e)
        {
            throw new UnableToReadTemplateException();
        }
        catch (SAXException e)
        {
            throw new UnableToReadTemplateException();
        }
        catch (FilterSyntaxErrorException e)
        {
            throw new InvalidTemplateFormatException();
        }
        catch (IgnoreTemplateException e)
        {
            throw new TemplateIgnoredException();
        }
    }

    @Override
    public void run(final TemplateInstance instance) throws FilterFailedException
    {
        TemplateInstanceImpl inst = templateInstances.get(instance.getName());
        for (TemplateData templateData : templates)
        {
            templateData.run(inst);
        }
    }

    @Override
    public Collection<Template> getTemplates()
    {
        return Collections.unmodifiableSet(new LinkedHashSet<Template>(templates));
    }

    @Override
    public TemplateInstance createTemplateInstance(final Template template, final String name) throws TemplateNotFoundException, ErrorCreatingTemplateInstanceException
    {
        TemplateData data = null;
        for (TemplateData t : templates)
        {
            if (t.getTemplateInfo().equals(template.getTemplateInfo()))
            {
                data = t;
                break;
            }
        }

        if (data == null)
        {
            throw new TemplateNotFoundException();
        }

        TemplateInstanceImpl instance = new TemplateInstanceImpl(name, data);
        templateInstances.put(name, instance);

        return instance;
    }

    @Override
    public void removeTemplateInstance(TemplateInstance instance)
    {
        TemplateInstanceImpl impl = templateInstances.get(instance.getName());
        templateInstances.remove(instance.getName());

        impl.removePropertiesFile();
    }

    @Override
    public void addTemplateLoadedListener(TemplateLoadedListener listener)
    {
        templateLoadedListeners.add(listener);
    }

    @Override
    public void removeTemplateLoadedListener(TemplateLoadedListener listener)
    {
        templateLoadedListeners.remove(listener);
    }

    @Override
    public void startRunner()
    {
        instanceRunner.startRunner();
    }

    @Override
    public void stopRunner()
    {
        instanceRunner.stopRunner();
    }

    private class InstanceRunner extends Thread
    {
        private boolean started = true;
        private int delay = 1000;

        public InstanceRunner()
        {
        }

        @Override
        public void run()
        {
            while (started)
            {
                for(TemplateInstanceImpl instance : templateInstances.values())
                {
                    try
                    {
                        instance.run();
                    }
                    catch (FilterFailedException e)
                    {
                        // Failed
                    }
                }

                try
                {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e)
                {
                    // Interupted
                }
            }
        }

        public void startRunner()
        {
            started = true;
            start();
        }

        public void stopRunner()
        {
            started = false;
        }
    }
}

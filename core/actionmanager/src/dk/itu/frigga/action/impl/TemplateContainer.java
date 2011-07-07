package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.*;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.device.DeviceManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
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
public class TemplateContainer implements TemplateManager
{
    private final Set<TemplateData> templates = Collections.synchronizedSet(new LinkedHashSet<TemplateData>());
    private final List<TemplateLoadedListener> templateLoadedListeners = Collections.synchronizedList(new LinkedList<TemplateLoadedListener>());

    private DeviceManager deviceManager;

    public TemplateContainer()
    {
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
    public void run() throws FilterFailedException
    {
        for (TemplateData templateData : templates)
        {
            templateData.run();
        }
    }

    @Override
    public Collection<Template> getTemplates()
    {
        return Collections.unmodifiableSet(new LinkedHashSet<Template>(templates));
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
}

package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterFailedException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-06
 */
public interface TemplateManager
{
    int getTemplateCount();
    TemplateInfo getTemplateInfo(int index) throws TemplateNotFoundException;

    void loadTemplateFromFile(String filename) throws FileNotFoundException, InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException;
    void loadTemplateFromString(String templateData) throws InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException;
    void loadTemplateFromStream(InputStream stream) throws InvalidTemplateFormatException, UnableToReadTemplateException, TemplateIgnoredException;

    void run() throws FilterFailedException;

    Collection<Template> getTemplates();

    void addTemplateLoadedListener(TemplateLoadedListener listener);
    void removeTemplateLoadedListener(TemplateLoadedListener listener);
}

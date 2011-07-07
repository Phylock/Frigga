package dk.itu.frigga.action;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    void loadTemplateFromFile(String filename) throws FileNotFoundException, InvalidTemplateFormatException, UnableToReadTemplateException;
    void loadTemplateFromString(String templateData) throws InvalidTemplateFormatException, UnableToReadTemplateException;
    void loadTemplateFromStream(InputStream stream) throws InvalidTemplateFormatException, UnableToReadTemplateException;

    void run() throws FilterFailedException;

    Collection<Template> getTemplates();
}

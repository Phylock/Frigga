package dk.itu.frigga.action;

import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Container class for a loaded template, this can be used to compile context
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Template
{
    private TemplateInfo templateInfo = new TemplateInfo();
    private final ReplacementContainer replacementContainer = new ReplacementContainer();
    private final RuleContainer ruleContainer = new RuleContainer();

    private final Map<String, RuleTemplate> rules;
    private final Map<String, File> include;
    private final Map<String, Replacement> replacements;

    public Template()
    {
        rules = new HashMap<String, RuleTemplate>();
        include = new HashMap<String, File>();
        replacements = new HashMap<String, Replacement>();
    }

    public void loadFromStream(final InputStream stream) throws ParserConfigurationException, IOException, SAXException, InvalidTemplateFormatException
    {
        loadFromSource(new InputSource(stream));
    }

    public void loadFromString(final String string) throws InvalidTemplateFormatException, IOException, SAXException, ParserConfigurationException
    {
        loadFromSource(new InputSource(new StringReader(string)));
    }

    public void loadFromSource(final InputSource source) throws ParserConfigurationException, IOException, SAXException, InvalidTemplateFormatException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);

        parse(document.getDocumentElement());

    }

    protected void parse(final Element element) throws InvalidTemplateFormatException
    {
        assert(element != null) : "Element can not be null.";

        Element elemTemplate = element;
        if (elemTemplate.getTagName().equals("frigga"))
        {
            elemTemplate = XmlHelper.getFirstChildElement(elemTemplate, "template");
            if (elemTemplate == null) throw new InvalidTemplateFormatException();
        }

        if (!elemTemplate.getTagName().equals("template"))
        {
            throw new InvalidTemplateFormatException();
        }

        for(Element elem = XmlHelper.getFirstChildElement(elemTemplate); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            String tagName = elem.getTagName();

            if (tagName.equals("info"))
            {
                templateInfo = new TemplateInfo(elem);
            }

            else if (tagName.equals("replacements"))
            {
                replacementContainer.parse(elem);
            }

            else if (tagName.equals("rules"))
            {
                ruleContainer.parse(elem);
            }
        }
    }

    public TemplateInfo getTemplateInfo()
    {
        return templateInfo;
    }

    public void setTemplateInfo(TemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
    }

    public Map<String, RuleTemplate> getRules()
    {
        return rules;
    }

    public Map<String, File> getInclude()
    {
        return include;
    }

    public Map<String, Replacement> getReplacements()
    {
        return replacements;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Template {\n ");
        sb.append(templateInfo.toString());
        sb.append("\n Include: \n");
        for (Entry<String, File> entry : include.entrySet())
        {
            sb.append("  ");
            sb.append(entry.getKey());
            sb.append(" = ");
            sb.append(entry.getValue().getAbsoluteFile());
            sb.append("\n");
        }

        sb.append("\n Replacements: \n");
        for (Replacement entry : replacements.values())
        {
            sb.append("  ");
            sb.append(entry.toString());
            sb.append("\n");
        }

        sb.append("\n Rules { \n");
        for (RuleTemplate entry : rules.values())
        {
            sb.append("  ");
            sb.append(entry.toString());
            sb.append("\n");
        }

        sb.append(" }\n}");

        return sb.toString();
    }
}

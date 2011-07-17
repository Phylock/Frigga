package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.InvalidTemplateFormatException;
import dk.itu.frigga.action.Replacement;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.action.TemplateInfo;
import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.action.impl.filter.filters.*;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collection;

/**
 * Container class for a loaded template, this can be used to compile context
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class TemplateData implements Template
{
    private static final FilterFactory filterFactory = new DefaultFilterFactory();

    static
    {
        filterFactory.registerFilterType("hasVariable", HasVariableFilter.class);
        filterFactory.registerFilterType("and", AndFilter.class);
        filterFactory.registerFilterType("or", OrFilter.class);
        filterFactory.registerFilterType("isCategory", IsCategoryFilter.class);
        filterFactory.registerFilterType("count", CountFilter.class);
        filterFactory.registerFilterType("passCount", PassCountFilter.class);
        filterFactory.registerFilterType("matchSymbolic", MatchSymbolicFilter.class);
        filterFactory.registerFilterType("lookedAt", LookedAtFilter.class);
        filterFactory.registerFilterType("remoteCompare", RemoteCompareFilter.class);
        filterFactory.registerFilterType("location", LocationFilter.class);
        filterFactory.registerFilterType("distance", DistanceFilter.class);
        filterFactory.registerFilterType("isOnline", IsOnlineFilter.class);
        filterFactory.registerFilterType("variableIs", VariableIsFilter.class);
        filterFactory.registerFilterType("time", TimeFilter.class);
        filterFactory.registerFilterType("empty", EmptyFilter.class);
    }

    private TemplateInfo templateInfo = new TemplateInfo();
    private final ReplacementContainer replacementContainer = new ReplacementContainer();
    private final RuleContainer ruleContainer = new RuleContainer(filterFactory, replacementContainer);
    private DeviceManager deviceManager;

    public TemplateData(DeviceManager deviceManager)
    {
        this.deviceManager = deviceManager;
    }

    public void run(final TemplateInstanceImpl instance) throws FilterFailedException
    {
        for (Rule rule : ruleContainer.getRules())
        {
            rule.run(deviceManager, instance);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateData that = (TemplateData) o;

        return templateInfo.getName().equals(that.templateInfo.getName());
    }

    @Override
    public int hashCode()
    {
        return templateInfo.hashCode();
    }

    public void loadFromStream(final InputStream stream) throws ParserConfigurationException, IOException, SAXException, InvalidTemplateFormatException, FilterSyntaxErrorException
    {
        loadFromSource(new InputSource(stream));
    }

    public void loadFromString(final String string) throws InvalidTemplateFormatException, IOException, SAXException, ParserConfigurationException, FilterSyntaxErrorException
    {
        loadFromSource(new InputSource(new StringReader(string)));
    }

    public void loadFromSource(final InputSource source) throws ParserConfigurationException, IOException, SAXException, InvalidTemplateFormatException, FilterSyntaxErrorException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);

        parse(document.getDocumentElement());
    }

    protected void parse(final Element element) throws InvalidTemplateFormatException, FilterSyntaxErrorException
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

    @Override
    public TemplateInfo getTemplateInfo()
    {
        return templateInfo;
    }

    public void setTemplateInfo(TemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
    }

    @Override
    public Collection<Replacement> getReplacements()
    {
        return replacementContainer.getReplacements();
    }

    @Override
    public String toString()
    {
        return "Template{" +
                "templateInfo=" + templateInfo +
                ", replacementContainer=" + replacementContainer +
                ", ruleContainer=" + ruleContainer +
                '}';
    }
}

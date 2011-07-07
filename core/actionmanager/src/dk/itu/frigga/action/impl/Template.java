package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.InvalidTemplateFormatException;
import dk.itu.frigga.action.TemplateInfo;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.action.impl.filter.filters.*;
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

/**
 * Container class for a loaded template, this can be used to compile context
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Template
{
    private static final FilterFactory filterFactory = new DefaultFilterFactory();

    static
    {
        filterFactory.registerFilterType("hasVariable", HasVariableFilter.class);
        filterFactory.registerFilterType("and", AndFilter.class);
        filterFactory.registerFilterType("or", OrFilter.class);
        filterFactory.registerFilterType("isCategory", IsCategoryFilter.class);
        filterFactory.registerFilterType("empty", EmptyFilter.class);
    }

    private TemplateInfo templateInfo = new TemplateInfo();
    private final ReplacementContainer replacementContainer = new ReplacementContainer();
    private final RuleContainer ruleContainer = new RuleContainer(filterFactory, replacementContainer);

    public Template()
    {
    }

    public void run() throws FilterFailedException
    {
        FilterContext context = new FilterContext();

        for (Rule rule : ruleContainer.getRules())
        {
            rule.run(context);
        }
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

    public TemplateInfo getTemplateInfo()
    {
        return templateInfo;
    }

    public void setTemplateInfo(TemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
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

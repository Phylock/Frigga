package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-06
 */
public class Report extends ProtocolElement
{
    private final String what;
    private final String value;

    public Report(final String what, final String value)
    {
        super(ProtocolElement.Type.REPORT);

        this.what = what;
        this.value = value;
    }

    public Report(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.REPORT);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("report"))
        {
            if (!element.hasAttribute("what")) throw new ProtocolSyntaxException("What is required.");
            if (!element.hasAttribute("value")) throw new ProtocolSyntaxException("Value is required.");

            what = element.getAttribute("what");
            value = element.getAttribute("value");
        }
        else
        {
            throw new UnexpectedElementException("report", element.getNodeName());
        }
    }

    public boolean is(final String what)
    {
        return this.what.equals(what);
    }

    public String getWhat()
    {
        return what;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "report");
        serializer.attribute(null, "what", what);
        serializer.attribute(null, "value", value);
        serializer.endTag(null, "report");
    }
}

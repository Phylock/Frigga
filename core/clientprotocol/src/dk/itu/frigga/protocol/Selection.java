package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Selection extends ProtocolElement
{
    private final Filter filter;
    private final String subject;
    private final boolean selected;

    public Selection(final Element element) throws ProtocolSyntaxException
    {
        super(ProtocolElement.Type.SELECTION);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("selection"))
        {
            if (!element.hasAttribute("value")) throw new ProtocolSyntaxException("value is required.");

            // Subject is no longer required by all selections
            // if (!element.hasAttribute("subject")) throw new ProtocolSyntaxException("subject is required.");

            filter = new Filter(element.getAttribute("value"));

            subject = (element.hasAttribute("subject")) ? element.getAttribute("subject") : "";
            selected = (element.hasAttribute("selected")) && element.getAttribute("selected").equals("true");
        }
        else
        {
            throw new UnexpectedElementException("selection", element.getNodeName());
        }
    }

    public Selection(final String filter, final String subject, boolean selected) throws InvalidFilterException, InvalidFilterGroupException
    {
        super(ProtocolElement.Type.SELECTION);

        this.filter = new Filter(filter);
        this.subject = subject;
        this.selected = selected;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "selection");
        serializer.attribute(null, "value", filter.toString());
        if (!subject.isEmpty()) serializer.attribute(null, "subject", subject);
        if (selected) serializer.attribute(null, "selected", "true");
        serializer.endTag(null, "selection");
    }
}

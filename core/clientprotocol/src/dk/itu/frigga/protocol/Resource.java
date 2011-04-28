package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
public class Resource extends ProtocolElement
{
    private final ResourceID id;
    private final Object value;

    public Resource(final ResourceID id, final Object value)
    {
        super(ProtocolElement.Type.RESOURCE);

        this.id = id;
        this.value = value;
    }

    public Resource(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.RESOURCE);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("resource"))
        {
            if (!element.hasAttribute("id")) throw new ProtocolSyntaxException("ID is required.");
            if (!element.hasAttribute("value")) throw new ProtocolSyntaxException("Value is required.");

            id = new ResourceID(element.getAttribute("id"));
            value = element.getAttribute("value");
        }
        else
        {
            throw new UnexpectedElementException("resource", element.getNodeName());
        }
    }

    public ResourceID getId()
    {
        return id;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "resource");
        serializer.attribute(null, "id", id.toString());
        serializer.attribute(null, "value", value.toString());
        serializer.endTag(null, "resource");
    }
}

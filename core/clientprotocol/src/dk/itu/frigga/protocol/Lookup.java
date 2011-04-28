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
public class Lookup extends ProtocolElement
{
    private final ResourceID id;

    public Lookup(final ResourceID id)
    {
        super(ProtocolElement.Type.LOOKUP);

        this.id = id;
    }

    public Lookup(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.LOOKUP);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("lookup"))
        {
            if (!element.hasAttribute("id")) throw new ProtocolSyntaxException("ID is required.");

            id = new ResourceID(element.getAttribute("id"));
        }
        else
        {
            throw new UnexpectedElementException("lookup", element.getNodeName());
        }
    }

    public ResourceID getId()
    {
        return id;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "lookup");
        serializer.attribute(null, "id", id.toString());
        serializer.endTag(null, "lookup");
    }
}

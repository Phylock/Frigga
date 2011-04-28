package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public class Request extends ProtocolElement
{
    private final String reference;
    private final String value;
    private final List<Selection> selections = Collections.synchronizedList(new ArrayList<Selection>());

    public Request(final String reference, final String value)
    {
        super(ProtocolElement.Type.REQUEST);

        this.reference = reference;
        this.value = value;
    }

    public Request(final Element element) throws ProtocolSyntaxException
    {
        super(ProtocolElement.Type.REQUEST);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("request"))
        {
            if (!element.hasAttribute("ref")) throw new ProtocolSyntaxException("Ref is required.");
            if (!element.hasAttribute("value")) throw new ProtocolSyntaxException("Value is required.");

            reference = element.getAttribute("ref");
            value = element.getAttribute("value");

            for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
            {
                if (child.getNodeType() == Node.ELEMENT_NODE)
                {
                    if (child.getNodeName().equals("selection"))
                    {
                        selections.add(new Selection((Element)child));
                    }
                    else
                    {
                        throw new UnexpectedElementException("selection", child.getNodeName());
                    }
                }
            }
        }
        else
        {
            throw new UnexpectedElementException("option", element.getNodeName());
        }
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "request");
        serializer.attribute(null, "ref", reference);
        serializer.attribute(null, "value", value);

        for (Selection selection : selections)
        {
            selection.build(serializer);
        }

        serializer.endTag(null, "request");
    }
}

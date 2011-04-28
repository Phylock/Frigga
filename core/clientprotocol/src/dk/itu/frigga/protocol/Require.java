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
public class Require extends ProtocolElement
{
    private final String what;

    public Require(final String what)
    {
        super(ProtocolElement.Type.REQUIRE);

        this.what = what;
    }

    public Require(final Element element) throws ProtocolSyntaxException
    {
        super(ProtocolElement.Type.REQUIRE);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("require"))
        {
            if (!element.hasAttribute("what")) throw new ProtocolSyntaxException("What is required.");

            what = element.getAttribute("what");
        }
        else
        {
            throw new UnexpectedElementException("require", element.getNodeName());
        }
    }

    public String whatIsRequired()
    {
        return what;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "require");
        serializer.attribute(null, "what", what);
        serializer.endTag(null, "require");
    }
}

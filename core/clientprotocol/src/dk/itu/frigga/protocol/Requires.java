package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-06
 */
public class Requires extends ProtocolElement
{
    private final List<Require> requires = Collections.synchronizedList(new ArrayList<Require>());

    public Requires()
    {
        super(ProtocolElement.Type.REQUIRE_COLLECTION);
    }

    public Requires(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.REQUIRE_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "requires", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"require"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                requires.add(new Require(element));
            }
        });
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "requires");

        for (Require require : requires)
        {
            require.build(serializer);
        }

        serializer.endTag(null, "requires");
    }

    public void add(final Require require)
    {
        assert(require != null) : "Null reference not allowed in add.";

        requires.add(require);
    }

    public int count()
    {
        return requires.size();
    }
}

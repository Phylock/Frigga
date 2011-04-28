package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-06
 */
public class Lookups extends ProtocolElement
{
    private final Map<ResourceID, Lookup> lookups = Collections.synchronizedMap(new HashMap<ResourceID, Lookup>());

    public Lookups()
    {
        super(ProtocolElement.Type.LOOKUP_COLLECTION);
    }

    public Lookups(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.LOOKUP_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "lookups", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"lookup"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                Lookup lookup = new Lookup(element);
                lookups.put(lookup.getId(), lookup);
            }
        });
    }

    public void add(final Lookup lookup)
    {
        lookups.put(lookup.getId(), lookup);
    }

    public Lookup get(final int index)
    {
        return (Lookup)lookups.values().toArray()[index];
    }

    public Lookup get(final ResourceID id)
    {
        return lookups.get(id);
    }

    public int count()
    {
        return lookups.size();
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "lookups");

        for (Lookup lookup : lookups.values())
        {
            lookup.build(serializer);
        }

        serializer.endTag(null, "lookups");
    }
}

package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
public class Resources extends ProtocolElement
{
    private final Map<ResourceID, Resource> resources = Collections.synchronizedMap(new HashMap<ResourceID, Resource>());

    public Resources()
    {
        super(ProtocolElement.Type.RESOURCE_COLLECTION);
    }

    public Resources(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.RESOURCE_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "resources", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"resource"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                Resource resource = new Resource(element);
                resources.put(resource.getId(), resource);
            }
        });
    }

    public void add(final Resource resource)
    {
        resources.put(resource.getId(), resource);
    }

    public Resource get(final int index)
    {
        return (Resource)resources.values().toArray()[index];
    }

    public Resource get(final ResourceID id)
    {
        return resources.get(id);
    }

    public int count()
    {
        return resources.size();
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "resources");

        for (Resource resource : resources.values())
        {
            resource.build(serializer);
        }

        serializer.endTag(null, "resources");
    }
}

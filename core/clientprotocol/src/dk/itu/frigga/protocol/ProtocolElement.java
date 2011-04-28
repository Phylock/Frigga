package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;

import java.io.IOException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public abstract class ProtocolElement
{
    public enum Type { OPTION_COLLECTION, OPTION,
                       REQUEST_COLLECTION, REQUEST,
                       REQUIRE_COLLECTION, REQUIRE,
                       LOOKUP_COLLECTION, LOOKUP,
                       REPORT_COLLECTION, REPORT,
                       RESOURCE_COLLECTION, RESOURCE,
                       SELECTION };

    protected Type type;

    public ProtocolElement(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }

    public boolean isOfType(Type type)
    {
        return this.type == type;
    }

    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        throw new ProtocolException();
    }
}

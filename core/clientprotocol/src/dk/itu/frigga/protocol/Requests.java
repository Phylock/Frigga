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
public class Requests extends ProtocolElement
{
    private final List<Request> requests = Collections.synchronizedList(new ArrayList<Request>());

    public Requests()
    {
        super(ProtocolElement.Type.REQUEST_COLLECTION);
    }

    public Requests(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.REQUEST_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "requests", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"request"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                requests.add(new Request(element));
            }
        });
    }

    public void add(final Request request)
    {
        requests.add(request);
    }

    public Request get(int index)
    {
        return requests.get(index);
    }

    public void remove(int index)
    {
        requests.remove(index);
    }

    public int count()
    {
        return requests.size();
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "requests");

        for (Request request : requests)
        {
            request.build(serializer);
        }

        serializer.endTag(null, "requests");
    }
}

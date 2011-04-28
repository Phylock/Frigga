package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * A message is a package used to transport the protocol in.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Message
{
    private final Lookups lookups = new Lookups();
    private final Options options = new Options();
    private final Reports reports = new Reports();
    private final Requests requests = new Requests();
    private final Requires requires = new Requires();
    private final Resources resources = new Resources();

    private UUID sessionId;

    public Message()
    {
    }

    public Message(final InputStream input) throws ParserConfigurationException, IOException, SAXException, ProtocolException//, XMLStreamException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        final DocumentBuilder builder = factory.newDocumentBuilder();

        /*StringWriter writer = new StringWriter();

        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 64 * 1024);
        int n;
        int total = 0;
        while ((n = reader.read(buffer)) != -1)
        {
            total += n;
            writer.write(buffer, 0, n);
        }
        Log.d("Frigga", writer.toString());
        Log.d("Frigga", "CLOSED! Received bytes: " + total);
        input.reset();  */
        //return writer.toString();

        InputSource source = new InputSource(new BufferedReader(new InputStreamReader(input, "UTF-8"), 64 * 1024));
        parse(builder.parse(source));
    }

    public Message(final Document doc) throws ProtocolException
    {
        parse(doc);
    }

    public void writeToStream(OutputStream stream) throws ProtocolException, IOException
    {
        XmlSerializer serializer = new XmlSerializer(stream);
        //serializer.setProperty("SERIALIZER INDENTATION", "");
        //serializer.setProperty("SERIALIZER LINE SEPARATOR", "");
        build(serializer);
    }

    public void associateSessionId(UUID sessionId)
    {
        this.sessionId = sessionId;
    }

    public void parse(final Document doc) throws ProtocolException
    {
        final Element root = doc.getDocumentElement();
        parse(root);
    }

    private void parse(final Element element) throws ProtocolException
    {
        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("frigga"))
        {
            if (element.hasAttribute("session"))
            {
                sessionId = UUID.fromString(element.getAttribute("session"));
            }
        }

        Parser.parse(element, "frigga", new ParserListener()
        {
            public String[] acceptedElements(String name, Element element)
            {
                return new String[]{"lookups", "options", "reports", "requests", "requires", "resources"};
            }

            public void elementFound(String name, Element element) throws ProtocolException
            {
                if (name.equals("lookups"))
                {
                    lookups.parse(element);
                } else if (name.equals("options"))
                {
                    options.parse(element);
                } else if (name.equals("reports"))
                {
                    reports.parse(element);
                } else if (name.equals("requests"))
                {
                    requests.parse(element);
                } else if (name.equals("requires"))
                {
                    requires.parse(element);
                } else if (name.equals("resources"))
                {
                    resources.parse(element);
                }
            }
        }, false);
    }

    protected void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "frigga");

        if (hasSessionId())
        {
            serializer.attribute(null, "session", sessionId.toString());
        }

        requires.build(serializer);
        lookups.build(serializer);
        reports.build(serializer);
        resources.build(serializer);
        options.build(serializer);
        requests.build(serializer);

        serializer.endDocument();
    }

    public boolean hasElementOfType(final ProtocolElement.Type type)
    {
        switch (type)
        {
            case LOOKUP: case LOOKUP_COLLECTION: return lookups.count() > 0;
            case OPTION: case OPTION_COLLECTION: return options.count() > 0;
            case REPORT: case REPORT_COLLECTION: return reports.count() > 0;
            case REQUEST: case REQUEST_COLLECTION: return requests.count() > 0;
            case REQUIRE: case REQUIRE_COLLECTION: return requires.count() > 0;
            case RESOURCE: case RESOURCE_COLLECTION: return resources.count() > 0;
            default: return false;
        }
    }

    public boolean hasSessionId()
    {
        return sessionId != null;
    }

    public UUID getSessionId()
    {
        return sessionId;
    }

    public Lookups getLookups()
    {
        return lookups;
    }

    public Options getOptions()
    {
        return options;
    }

    public Reports getReports()
    {
        return reports;
    }

    public Requests getRequests()
    {
        return requests;
    }

    public Requires getRequires()
    {
        return requires;
    }

    public Resources getResources()
    {
        return resources;
    }
}

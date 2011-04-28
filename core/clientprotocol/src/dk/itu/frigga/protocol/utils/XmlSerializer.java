package dk.itu.frigga.protocol.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-27
 */
public class XmlSerializer
{
    private final OutputStream stream;
    private String encoding;
    private Stack<String> openTags = new Stack<String>();
    private boolean open = false;

    public XmlSerializer(final OutputStream stream)
    {
        this.stream = stream;
    }

    public void startDocument(final String encoding, final boolean standalone) throws IOException
    {
        this.encoding = encoding;

        stream.write("<?xml version=\"1.0\" encoding=\"".getBytes(encoding));
        stream.write(encoding.getBytes(encoding));
        stream.write("\"".getBytes(encoding));
        stream.write(" standalone=\"".getBytes(encoding));
        stream.write(standalone ? "yes".getBytes(encoding) : "no".getBytes(encoding));
        stream.write("\"".getBytes(encoding));
        stream.write("?>".getBytes(encoding));
    }

    public void startTag(final String namespace, final String tagName) throws IOException
    {
        if (open)
        {
            stream.write(">".getBytes(encoding));
            open = false;
        }

        String name = tagName;
        if (namespace != null)
        {
            name = namespace + ":" + name;
        }
        openTags.push(name);
        name = "<" + name;
        open = true;

        stream.write(name.getBytes(encoding));
    }

    public void endTag(final String namespace, final String tagName) throws IOException, MalformedXmlException
    {
        String name = tagName;
        if (namespace != null)
        {
            name = namespace + ":" + name;
        }

        String tagOnStack = openTags.pop();

        while (!tagOnStack.equals(name) && openTags.size() > 0)
        {
            writeCloseTag(tagOnStack);
            tagOnStack = openTags.pop();
        }

        if (!tagOnStack.equals(name))
        {
            throw new MalformedXmlException();
        }

        writeCloseTag(name);
    }

    public void endDocument() throws IOException
    {
        while (openTags.size() > 0)
        {
            String tagOnStack = openTags.pop();
            writeCloseTag(tagOnStack);
        }
    }

    public void attribute(final String namespace, final String attributeName, final String value) throws MalformedXmlException, IOException
    {
        if (!open)
        {
            throw new MalformedXmlException();
        }

        String name = attributeName + "=\"";
        if (namespace != null)
        {
            name = namespace + ":" + name;
        }
        String out = " " + name + value.replaceAll("\"", "\\\\\"") + "\"";
        stream.write(out.getBytes(encoding));
    }

    private void writeCloseTag(final String name) throws IOException
    {
        if (open)
        {
            stream.write("/>".getBytes(encoding));
        }
        else
        {
            String tagStr = "</" + name + ">";
            stream.write(tagStr.getBytes(encoding));
        }
        open = false;
    }
}

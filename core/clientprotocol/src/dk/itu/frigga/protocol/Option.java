package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The option class holds what action a user can take.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Option extends ProtocolElement
{
    private final String description;
    private final String subject;
    private final String type;
    private final String current;
    private final String id;
    private final float significance;
    private final List<Selection> selections = Collections.synchronizedList(new ArrayList<Selection>());

    public Option(Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.OPTION);

        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("option"))
        {
            if (!element.hasAttribute("subject")) throw new ProtocolSyntaxException("Subject is required.");
            if (!element.hasAttribute("type")) throw new ProtocolSyntaxException("type is required.");
            if (!element.hasAttribute("current")) throw new ProtocolSyntaxException("current is required.");
            if (!element.hasAttribute("id")) throw new ProtocolSyntaxException("id is required.");

            subject = element.getAttribute("subject");
            type = element.getAttribute("type");
            current = element.getAttribute("current");
            id = element.getAttribute("id");

            if (element.hasAttribute("description"))
            {
                description = element.getAttribute("description");
            }
            else
            {
                description = "";
            }

            if (element.hasAttribute("significance"))
            {
                significance = Float.parseFloat(element.getAttribute("significance"));
            }
            else
            {
                significance = 50.0f;
            }
        }
        else
        {
            throw new UnexpectedElementException("option", element.getNodeName());
        }

        Parser.parse(element, "option", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{ "selection" };
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                selections.add(new Selection(element));
            }
        });
    }

    public Option(final String id, final String subject, final String type, final String current, final String description, float significance)
    {
        super(ProtocolElement.Type.OPTION);

        this.id = id;
        this.subject = subject;
        this.type = type;
        this.current = current;
        this.description = description;
        this.significance = significance;
    }

    public void addSelection(final Selection selection)
    {
        selections.add(selection);
    }

    public float getSignificance()
    {
        return significance;
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "option");
        serializer.attribute(null, "subject", subject);
        serializer.attribute(null, "type", type);
        serializer.attribute(null, "current", current);
        serializer.attribute(null, "id", id);
        serializer.attribute(null, "significance", Float.toString(significance));

        if (!description.isEmpty())
        {
            serializer.attribute(null, "description", description);
        }

        for (Selection selection : selections)
        {
            selection.build(serializer);
        }

        serializer.endTag(null, "option");
    }

    @Override
    public String toString() {
        return "Option{" +
                "selections=" + selections +
                ", description='" + description + '\'' +
                ", subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                ", current='" + current + '\'' +
                ", significance=" + significance +
                '}';
    }
}

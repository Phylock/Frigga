package dk.itu.frigga.protocol;

import dk.itu.frigga.protocol.utils.XmlSerializer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Options is a collection of option elements.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class Options extends ProtocolElement
{
    private final List<Option> options = Collections.synchronizedList(new ArrayList<Option>());

    public Options()
    {
        super(ProtocolElement.Type.OPTION_COLLECTION);
    }

    public Options(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.OPTION_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "options", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"option"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                options.add(new Option(element));
            }
        });

        sort();
    }

    public void add(Option option)
    {
        options.add(option);

        sort();
    }

    public int count()
    {
        return options.size();
    }

    public Option get(int index)
    {
        return options.get(index);
    }

    public void remove(int index)
    {
        options.remove(index);
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "options");

        for (Option option : options)
        {
            option.build(serializer);
        }

        serializer.endTag(null, "options");
    }

    private void sort()
    {
        Collections.sort(options, new Comparator<Option>()
        {
            public int compare(Option o1, Option o2)
            {
                if (o1.getSignificance() > o2.getSignificance()) return -1;
                if (o1.getSignificance() < o2.getSignificance()) return 1;
                return 0;
            }
        });
    }
}

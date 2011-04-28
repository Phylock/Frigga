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
public class Reports extends ProtocolElement
{
    private final List<Report> reports = Collections.synchronizedList(new ArrayList<Report>());

    public Reports()
    {
        super(ProtocolElement.Type.REPORT_COLLECTION);
    }

    public Reports(final Element element) throws ProtocolException
    {
        super(ProtocolElement.Type.REPORT_COLLECTION);

        parse(element);
    }

    public void parse(final Element element) throws ProtocolException
    {
        Parser.parse(element, "reports", new ParserListener()
        {
            public String[] acceptedElements(final String name, final Element element)
            {
                return new String[]{"report"};
            }

            public void elementFound(final String name, final Element element) throws ProtocolException
            {
                reports.add(new Report(element));
            }
        });
    }

    public void add(final Report report)
    {
        reports.add(report);
    }

    public Report get(final int index)
    {
        return reports.get(index);
    }

    public Report get(final String what) throws ReportNotFoundException
    {
        for (Report report : reports)
        {
            if (report.is(what)) return report;
        }

        throw new ReportNotFoundException();
    }

    public boolean has(final String what)
    {
        for (Report report : reports)
        {
            if (report.is(what)) return true;
        }

        return false;
    }

    public int count()
    {
        return reports.size();
    }

    @Override
    public void build(final XmlSerializer serializer) throws ProtocolException, IOException
    {
        serializer.startTag(null, "reports");

        for (Report report : reports)
        {
            report.build(serializer);
        }

        serializer.endTag(null, "reports");
    }
}

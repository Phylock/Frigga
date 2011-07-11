package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.Filter;
import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterInput;
import dk.itu.frigga.action.impl.filter.FilterOutput;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-11
 */
public class TimeFilter extends Filter
{
    private String value = "@now";
    private String atLeast = "@now";
    private String atMost = "@now";
    private String formatToUse = "yyyy-MM-dd HH:mm:ss.S";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("value"))
        {
            value = attributes.get("value");
        }

        if (attributes.containsKey("atLeast"))
        {
            atLeast = attributes.get("atLeast");
        }

        if (attributes.containsKey("atMost"))
        {
            atMost = attributes.get("atMost");
        }

        if (attributes.containsKey("format"))
        {
            formatToUse = attributes.get("format");
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        String localValue = context.prepare(value);
        String localAtLeast = context.prepare(atLeast);
        String localAtMost = context.prepare(atMost);
        String localFormatToUse = context.prepare(formatToUse);

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        if (localValue.equals("@now")) localValue = now.toString();
        if (localAtLeast.equals("@now")) localAtLeast = now.toString();
        if (localAtMost.equals("@now")) localAtMost = now.toString();

        DateFormat format = new SimpleDateFormat(localFormatToUse);

        try
        {
            Date dateValue = format.parse(localValue);
            Date dateAtLeast = format.parse(localAtLeast);
            Date dateAtMost = format.parse(localAtMost);

            if (dateValue.compareTo(dateAtLeast) >= 0 && dateValue.compareTo(dateAtMost) <= 0)
            {
                output.useInput(input);
            }
        }
        catch (ParseException e)
        {
            context.reportFilterError(this, "Error parsing date format: " + e.getMessage());
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

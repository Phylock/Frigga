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
    private String atLeast = "00:00:01";
    private String atMost = "00:00:00";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("atLeast"))
        {
            atLeast = attributes.get("atLeast");
        }

        if (attributes.containsKey("atMost"))
        {
            atMost = attributes.get("atMost");
        }
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        String localAtLeast = context.prepare(atLeast);
        String localAtMost = context.prepare(atMost);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        long now = hour * 60 * 60 + minute * 60 + second;

        String[] minParts = localAtLeast.split(":");
        int minHour = Integer.parseInt(minParts[0]);
        int minMinute = Integer.parseInt(minParts[1]);
        int minSecond = Integer.parseInt(minParts[2]);
        long min = minHour * 60 * 60 + minMinute * 60 + minSecond;

        String[] maxParts = localAtMost.split(":");
        int maxHour = Integer.parseInt(maxParts[0]);
        int maxMinute = Integer.parseInt(maxParts[1]);
        int maxSecond = Integer.parseInt(maxParts[2]);
        long max = maxHour * 60 * 60 + maxMinute * 60 + maxSecond;

        if (max > min)
        {
            if (now >= min && now < max)
            {
                output.useInput(input);
            }
        }
        else if (max < min)
        {
            if (now >= min || now < max)
            {
                output.useInput(input);
            }
        }
        else
        {
            if (max == now)
            {
                output.useInput(input);
            }
        }

        return output;
    }

    @Override
    protected boolean allowChildFilters()
    {
        return true;
    }
}

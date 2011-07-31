package dk.itu.frigga.action.impl.filter.filters;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.LocationLocal;

import java.util.Date;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-31
 */
public class SameRoomFilter extends Filter
{
    private String source = "";
    private String tagName = "";

    @Override
    protected void loadFilter(Map<String, String> attributes)
    {
        super.loadFilter(attributes);

        if (attributes.containsKey("source"))
        {
            source = attributes.get("source");
        }

        if (attributes.containsKey("tagName"))
        {
            tagName = attributes.get("tagName");
        }
    }

    private boolean IsInRoom(final Date date, long now)
    {
        if (date != null)
        {
            long delta = now - date.getTime();
            return delta < 3000;
        }

        return true;
    }

    @Override
    protected FilterOutput run(FilterContext context, FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        long now = System.currentTimeMillis();
        FilterOutput remoteOutput = context.getStoredOutput(context.prepare(source));
        String localTag = context.prepare(tagName);

        for (FilterDeviceState state : input)
        {
            if (state.getDevice().getLocalLocations().size() > 0)
            {
                LocationLocal location1 = state.getDevice().getLocalLocations().get(0);
                String room1 = location1.getRoom();
                //boolean isFixed = location1.getUpdated() == null;

                if (IsInRoom(location1.getUpdated(), now))
                {
                    for (FilterDeviceState remote : remoteOutput)
                    {
                        if (remote.getDevice().getLocalLocations().size() > 0)
                        {
                            LocationLocal location2 = remote.getDevice().getLocalLocations().get(0);
                            String room2 = location2.getRoom();
                            if (IsInRoom(location2.getUpdated(), now))
                            {
                                if (room1.equals(room2))
                                {
                                    state.storeTag(localTag, remote);
                                    output.addDevice(state);
                                }
                            }
                        }
                    }
                }
            }
        }

        return output;
    }
}

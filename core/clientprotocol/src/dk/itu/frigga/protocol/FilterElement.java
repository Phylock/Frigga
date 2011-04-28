package dk.itu.frigga.protocol;

/**
 * A filter element is a subset of a filter. The filter element is the
 * building block of a filter. By combining several filter elements we can
 * create a query that enables us to find specific devices.
 *
 * A filter element consists of two parts, a group and a specifier. The group
 * tells us in which bucket to look, and the specifier tells us what to look
 * for. If the group for instance were ROOM, then the specifier could be
 * kitchen; this tells us to find anything put in the bucket kitchen. Later
 * filters might narrow this search even more by telling us we only want lamps
 * from the kitchen and so on.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public final class FilterElement implements Comparable<FilterElement>
{
    public enum Group { REGION, LOCATION, ROOM, DEVICE, ID };

    private final Group group;
    private final String specifier;

    public FilterElement(final String fraction) throws InvalidFilterGroupException {
        String[] fractions = fraction.split(":", 2);
        this.group = stringToGroup(fractions[0]);
        this.specifier = fractions[1];
    }

    public FilterElement(final String group, final String specifier) throws InvalidFilterGroupException {
        this.group = FilterElement.stringToGroup(group);
        this.specifier = specifier;
    }

    public FilterElement(final Group group, final String specifier)
    {
        this.group = group;
        this.specifier = specifier;
    }

    public static Group stringToGroup(final String group) throws InvalidFilterGroupException {
        if (group.equals("region")) return FilterElement.Group.REGION;
        if (group.equals("location")) return FilterElement.Group.LOCATION;
        if (group.equals("room")) return FilterElement.Group.ROOM;
        if (group.equals("device")) return FilterElement.Group.DEVICE;
        if (group.equals("id")) return FilterElement.Group.ID;
        throw new InvalidFilterGroupException(group);
    }

    public static String groupToString(final Group group) throws ProtocolException {
        switch (group)
        {
            case REGION: return "region";
            case LOCATION: return "location";
            case ROOM: return "room";
            case DEVICE: return "device";
            case ID: return "id";
            default: throw new ProtocolException();
        }
    }

    public boolean isGroup(final String group)
    {
        return this.group.equals(group);
    }

    public boolean isSpecifier(final String specifier)
    {
        return this.specifier.equals(specifier);
    }

    public boolean specifierIsAll()
    {
        return specifier.equals("*");
    }

    public boolean specifierIsCurrent()
    {
        return specifier.equals("@this");
    }

    public int compareTo(FilterElement other)
    {
        int result = group.compareTo(other.group);

        if (result == 0)
        {
            if (specifierIsAll() && !other.specifierIsAll())
            {
                // Other is more specific
                result = 1;
            }
            else if (other.specifierIsAll() && !specifierIsAll())
            {
                // This is more specific
                result = -1;
            }
            else if (specifierIsCurrent() && !other.specifierIsCurrent())
            {
                // This is more specific
                result = -1;
            }
            else if (other.specifierIsCurrent() && !specifierIsCurrent())
            {
                // Other is more specific
                result = 1;
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (!(other instanceof FilterElement)) return false;

        FilterElement that = (FilterElement) other;

        if (group != that.group) return false;
        if (specifier != null ? !specifier.equals(that.specifier) : that.specifier != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (specifier != null ? specifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        String g;
        try
        {
            g = groupToString(group);
        }
        catch (ProtocolException e)
        {
            g = "unknown";
        }

        return g + ":" + specifier;
    }
}

package dk.itu.frigga.protocol;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
public final class ResourceID
{
    private final String id;

    public ResourceID(final String id)
    {
        assert(id != null) : "ID can not be null.";

        this.id = id;
    }

    public static ResourceID resourceID(final String id)
    {
        return new ResourceID(id);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ResourceID)) return false;

        ResourceID that = (ResourceID) o;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        return id;
    }
}

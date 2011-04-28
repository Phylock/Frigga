package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: Tommy
 * Date: 10-03-11
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class InvalidFilterGroupException extends ProtocolSyntaxException
{
    private final String group;

    public InvalidFilterGroupException(final String group)
    {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Invalid filter group, wanted one of: region, location, room, device, id; got: '" + group + "'";
    }
}

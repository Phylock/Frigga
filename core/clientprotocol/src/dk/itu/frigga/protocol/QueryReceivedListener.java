package dk.itu.frigga.protocol;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
public interface QueryReceivedListener
{
    public void requiresReceived(final MessageSource source, final MessageResult result, final Requires requires);
    public void lookupsReceived(final MessageSource source, final MessageResult result, final Lookups lookups);
}

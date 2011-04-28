package dk.itu.frigga.protocol;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public interface ProtocolMessageListener
{
    public void messageReceived(final MessageSource source, final MessageResult result);
}

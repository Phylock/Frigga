package dk.itu.frigga.protocol;

/**
 * Use the ProtocolOptionsListener to make a class capable of receiving
 * notifications about the retrieval of an options object.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-03-10
 */
public interface ProtocolOptionsListener
{
    public void optionsReceived(final Peer peer, final Options options);
}

package dk.itu.frigga.protocol;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-13
 */
interface MessageHandler
{
    public void addInitialDelay(long ms);
    public void handle() throws AbortMessageHandlingException, RetryMessageHandlingException, ProtocolException;
    public boolean retry();
}

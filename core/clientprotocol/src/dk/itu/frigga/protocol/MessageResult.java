package dk.itu.frigga.protocol;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
public final class MessageResult
{
    private final FriggaConnection connection;
    private final Message message;
    protected boolean hasResult = false;

    public MessageResult(final FriggaConnection connection, final Message message)
    {
        assert(connection != null) : "Connection can not be null.";
        assert(message != null) : "Message can not be null.";

        this.connection = connection;
        this.message = message;
    }

    public FriggaConnection getConnection()
    {
        return connection;
    }

    public Message getMessage()
    {
        return message;
    }

    public void use()
    {
        hasResult = true;
    }
}

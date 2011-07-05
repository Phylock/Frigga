package dk.itu.frigga.device;

import dk.itu.frigga.FriggaException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class UnknownDeviceVariableException extends FriggaException
{
    private final String variableName;

    public UnknownDeviceVariableException(final String variableName)
    {
        this.variableName = variableName;
    }

    @Override
    public String toString()
    {
        return "UnknownDeviceVariableException{" +
                "variableName='" + variableName + '\'' +
                '}';
    }
}

package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public interface ExecutableResult
{
    public enum ResultType { RESULT_DEVICE, RESULT_VARIABLE, RESULT_CONSTANT }

    public void addDevice(final Device device);
    public void addDevices(final Device[] devices);

    public void setVariable(final String variable);

    public void setConstant(final String constant);

    public ResultType getResultType();

    public boolean hasDevices();
    public boolean hasResult();

    public String getVariable();
    public String getConstant();
    public Device[] getDevices();
}

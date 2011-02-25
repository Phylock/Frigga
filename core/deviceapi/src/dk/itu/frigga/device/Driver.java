/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

/**
 *
 * @author Tommy
 */
public interface Driver {
    public FunctionResult callFunction(Device[] device, Executable function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException;
}

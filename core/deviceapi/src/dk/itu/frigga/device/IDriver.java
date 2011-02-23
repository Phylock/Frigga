/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

/**
 *
 * @author Tommy
 */
public interface IDriver {
    public FunctionResult callFunction(Device[] device, IFunction function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException;
}

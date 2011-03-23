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
    public FunctionResult callFunction(String[] device, String function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException;

    /**
     * This is called whenever a service request a full update of all devices the driver handles
     */
    public void update();

    /**
     * This is called when we request an update of all devices in one or more categories
     * @param devicecategories
     */
    public void update(String[] devicecategories);

    /**
     * This is called when we request an update of a number of known devices
     * @param devices
     */
    public void update(DeviceId[] devices);
}

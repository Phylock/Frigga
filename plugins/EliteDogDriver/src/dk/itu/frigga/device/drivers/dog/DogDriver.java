/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.Executable;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.device.manager.DeviceManager;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class DogDriver implements Driver {
    //External services, initialized by DependencyManager

    private volatile DeviceManager devicemanager;
    private volatile LogService log = new NullLog();
    //Private member variables
    private Connection connection;
    /*** TODO: read parameter, for now assume that the Dog gateway is running on localhost */
    public static String DEFAULT_DOG_ADDRESS = "http://localhost:65300/RPC2";

    public void addDeviceManager(DeviceManager manager) {
        devicemanager = manager;
        connection.getParser().setDeviceManager(manager);
    }

    public void removeDeviceManager(DeviceManager manager) {
        //TODO: nullobject?? wait for devicemanager??
        devicemanager = null;
        connection.getParser().setDeviceManager(null);
    }

    public DogDriver() {
        log.log(LogService.LOG_INFO, "Dog Driver: Initialize");
        connection = new Connection(this);
    }

    public static void main(String[] args) {
        DogDriver driver = new DogDriver();
    }

    public Connection getConnection() {
        return connection;
    }

    /** Implements Driver***/
    @Override
    public FunctionResult callFunction(Device[] devices, Executable function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
        return function.execute(devices, parameters);
    }

    public void update() {
        try {
            DogMessage command = DogProtocol.generateDescribeDevice(null);
            connection.send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }

    public void update(String[] devicecategories) {
        try {
            DogMessage command = DogProtocol.generateDescribeDeviceCategory(devicecategories);
            connection.send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }

    public void update(DeviceId[] devices) {
        try {
            int length = devices.length;
            String[] d = new String[length];
            for (int i = 0; i < length; i++) {
                d[i] = devices[i].toString();
            }
            DogMessage command = DogProtocol.generateDescribeDevices(d);
            connection.send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }
}

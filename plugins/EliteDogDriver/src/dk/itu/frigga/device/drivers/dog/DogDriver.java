/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceManager;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.Executable;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.utility.ReflectionHelper;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.service.log.LogService;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

/**
 *
 * @author phylock
 */
public class DogDriver implements Driver {
    //External services, initialized by DependencyManager

    private LogService log;
    private Publisher event;
    
    //Private member variables
    private Connection connection = null;
    /*** TODO: read parameter, for now assume that the Dog gateway is running on localhost */
    public static String DEFAULT_DOG_ADDRESS = "http://localhost:65300/RPC2";

    public DogDriver() {
    }

    public Connection getConnection() {
        return connection;
    }

    /** Implements Driver **/

    @Override
    public FunctionResult callFunction(Device[] devices, Executable function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
        return function.execute(devices, parameters);
    }

    @Override
    public void update() {
        try {
            DogMessage command = DogProtocol.generateDescribeDevice(null);
            connection.send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }
    
    @Override
    public void update(String[] devicecategories) {
        try {
            DogMessage command = DogProtocol.generateDescribeDeviceCategory(devicecategories);
            connection.send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }

    @Override
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
    /** iPOJO Callbacks **/

    private void validate() {
        if (connection == null) {
            connection = new Connection(this);
        }
        log.log(LogService.LOG_INFO, "DogDriver Validate");
        updateSubclassFields("log", connection, log);
        updateSubclassFields("log", connection.getParser(), log);

        updateSubclassFields("event", DogDeviceManager.instance(), event);

        connection.connect(DogDriver.DEFAULT_DOG_ADDRESS);

    }

    private void invalidate() {
        connection.disconnect();
    }

    /**
     * Update referance in subclass, even if its private
     * @param clazz the class to update
     * @param field the field name
     * @param obj the class instance to update
     * @param value the new value
     */
    private void updateSubclassFields(String field, Object obj, Object value) {
        try {
            ReflectionHelper.updateSubclassFields(field, obj, value);
        } catch (Exception ex) {
            log.log(LogService.LOG_WARNING, String.format("Could not propergate iPOJO field %s to subclass %s", field, obj.getClass().getName()));
        }
    }
}

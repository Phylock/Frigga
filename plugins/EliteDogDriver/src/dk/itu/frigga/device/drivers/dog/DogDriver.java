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
import java.lang.reflect.Field;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class DogDriver implements Driver {
  //External services, initialized by DependencyManager

  private volatile DeviceManager devicemanager;
  private volatile LogService log;
  //Private member variables
  private Connection connection = null;
  
  /*** TODO: read parameter, for now assume that the Dog gateway is running on localhost */
  public static String DEFAULT_DOG_ADDRESS = "http://localhost:65300/RPC2";

  public DogDriver() {

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

  private void validate() {
    if (connection == null) {
      connection = new Connection(this);
    }

    updateSubclassFields(Connection.class, "log", connection, log);
    updateSubclassFields(DogParser.class, "log", connection.getParser(), log);

    connection.getParser().setDeviceManager(devicemanager);


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
  private void updateSubclassFields(Class clazz, String field, Object obj, Object value) {
    try {
      Field f = clazz.getDeclaredField("log");
      f.setAccessible(true);
      f.set(connection, log);
      f.setAccessible(false);
    } catch (Exception ex) {
      log.log(LogService.LOG_WARNING, String.format("Could not propergate iPOJO field %s to subclass %s", field, obj.getClass().getName()));
    }
  }
}

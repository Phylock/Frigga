/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.drivers.dog.protocol.Command;
import dk.itu.frigga.device.drivers.dog.protocol.DogMessage;
import dk.itu.frigga.device.drivers.dog.protocol.DogProtocol;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.utility.ReflectionHelper;
import java.util.Dictionary;
import javax.xml.parsers.ParserConfigurationException;
import org.osgi.service.log.LogService;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

/**
 *
 * @author phylock
 */
public class DogDriver implements Driver {

  private static final String DRIVER_ID = "DogDriver-%s";
  //External services, initialized by DependencyManager
  private LogService log;
  private Publisher event;
  //Private member variables
  private Connection connection = null;
  private String url;
  private String id;

  public DogDriver() {
    connection = new Connection(this);
  }

  public Connection getConnection() {
    return connection;
  }

  /** Implements Driver **/
  @Override
  public FunctionResult callFunction(String[] devices, String function, Parameter... parameters)
          throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
    try {
      //TODO: how to set parameters ??
      Command command = new Command(devices, function, null);
      DogMessage message = DogProtocol.generateCommandMessage(command);
      connection.send(message);
    } catch (ParserConfigurationException ex) {
      log.log(LogService.LOG_ERROR, "failed", ex);
    }

    return new FunctionResult();
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
    log.log(LogService.LOG_INFO, "DogDriver Validate");
    updateSubclassFields("log", connection, log);
    updateSubclassFields("log", connection.getParser(), log);

    updateSubclassFields("event", DogDeviceManager.instance(), event);

    if (url != null && !url.isEmpty()) {
      connection.connect(url);
    }
  }

  private void invalidate() {
    connection.disconnect();
  }

  /**
   * Update reference in subclass, even if its private
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

  public void updated(Dictionary conf) {
    id = parseID(conf.get("felix.fileinstall.filename").toString());
    if (url != null && !url.isEmpty()) {
      connection.connect(url);
    }
  }

  private static String parseID(String filename) {
    int start = filename.lastIndexOf('-') + 1;
    int end = filename.lastIndexOf('.');
    return filename.substring(start, end);
  }

  public String getDriverId() {
    return String.format(DRIVER_ID, id);
  }
}

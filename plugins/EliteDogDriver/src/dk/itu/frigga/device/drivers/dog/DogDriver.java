package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.drivers.dog.protocol.Command;
import dk.itu.frigga.device.drivers.dog.protocol.DogMessage;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.device.drivers.dog.protocol.CommandMessage;
import dk.itu.frigga.device.drivers.dog.protocol.DescribeCategory;
import dk.itu.frigga.device.drivers.dog.protocol.DescribeDevice;
import dk.itu.frigga.device.drivers.dog.protocol.ListDevices;
import dk.itu.frigga.utility.ReflectionHelper;
import java.util.Dictionary;
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

    Command command = new Command(devices, function, parameters);
    DogMessage message = new CommandMessage(command);
    connection.send(message);

    return new FunctionResult();
  }

  @Override
  public void update() {
    DogMessage command = new DescribeDevice(null);
    connection.send(command);
  }

  @Override
  public void update(String[] devicecategories) {
    DogMessage command = new DescribeCategory(devicecategories);
    connection.send(command);
  }

  @Override
  public void update(DeviceId[] devices) {

    int length = devices.length;
    String[] d = new String[length];
    for (int i = 0; i < length; i++) {
      d[i] = devices[i].toString();
    }
    DogMessage command = new ListDevices(d);
    connection.send(command);
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

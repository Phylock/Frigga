/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.got;

import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.LocationUpdate;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.device.VariableChangedEvent;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import dk.itu.frigga.device.model.Point3;
import dk.itu.frigga.got.client.GotClient;
import dk.itu.frigga.got.client.GotClientTcp;
import dk.itu.frigga.got.event.SensorPackage;
import dk.itu.frigga.got.event.SensorPackageListener;
import dk.itu.frigga.got.sim.GotClientSimulate;
import dk.itu.frigga.got.sim.LinearMovement;
import dk.itu.frigga.got.sim.SimulateReciever;
import dk.itu.frigga.got.sim.SimulateTransmitter;
import dk.itu.frigga.got.sim.Simulator;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class GotDriver implements Driver, SensorPackageListener {

  private static final String DRIVER_ID = "GameOnTrack-%s";
  private static final String CATEGORY = "indoor_location";
  //OSGi
  private BundleContext context;
  //iPOJO
  private LogService log;
  private Publisher devent;
  private Publisher vevent;
  //Driver
  private String id;
  private final List<CategoryDescriptor> cd = new LinkedList<CategoryDescriptor>();
  private final List<FunctionDescriptor> fd = new LinkedList<FunctionDescriptor>();
  private final List<VariableDescriptor> vd = new LinkedList<VariableDescriptor>();
  //private
  private GotClient client;
  private String host = "";
  private int port = 0;
  private String room = "";
  private final Map<String, GotDevice> recievers;

  public GotDriver(BundleContext context) {
    this.context = context;
    cd.add(new CategoryDescriptor(CATEGORY, new String[]{"attachedto"}, new String[]{"attach"}));
    fd.add(new FunctionDescriptor("attach", new String[]{"device"}));
    vd.add(new VariableDescriptor("attachedto", "string"));

    recievers = new HashMap<String, GotDevice>();
  }

  //@Validate
  private void validate() {
  }

  //@Invalidate
  private void invalidate() {
  }

  public void updated(Dictionary conf) {
    id = parseID(conf.get("felix.fileinstall.filename").toString());

    try {
      String newhost = conf.get("frigga.got.host").toString();
      int newport = Integer.parseInt(conf.get("frigga.got.port").toString());
      String newroom = conf.get("frigga.got.room").toString();

      if ((!newhost.equals(host) || host == null) && (newport == port || port == 0)) {
        host = newhost;
        port = newport;

        if (client != null) {
          try {
            client.disconnect();
            client.removeListener(this);
          } catch (IOException ex) {
            Logger.getLogger(GotDriver.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        if(newroom.equals(room))
        {
          room = newroom;
        }
        //client = new GotClientTcp(host, port);
        client = new GotClientSimulate(createSimulator());
        client.addListener(this);
        client.connect();
      }
    } catch (Exception ex) {
      log.log(LogService.LOG_ERROR, ex.getLocalizedMessage(), ex);
      throw new IllegalArgumentException(ex);
    }
  }

  private Simulator createSimulator()
  {
    Simulator sim = new Simulator();
    sim.addReciever(new SimulateReciever(1000, 800, 20202, new dk.itu.frigga.got.event.Point3(0, 0, 0)));
    sim.addReciever(new SimulateReciever(1000, 800, 20191, new dk.itu.frigga.got.event.Point3(1000, 0, 0)));
    sim.addReciever(new SimulateReciever(1000, 800, 20195, new dk.itu.frigga.got.event.Point3(1000, 500, 0)));

    sim.addTransmitter(new SimulateTransmitter(new LinearMovement(new dk.itu.frigga.got.event.Point3(1.0,0.0, 0.0), 1000), 11003, 0.91f, new dk.itu.frigga.got.event.Point3(10, 10, 5)));
    //sim.addTransmitter(new SimulateTransmitter(new LinearMovement(new dk.itu.frigga.got.event.Point3(1.0,0.5, 0.0), 50), 11004, 0.95f, new dk.itu.frigga.got.event.Point3(10, 10, 5)));
    return sim;
  }

  /** Implements Driver **/
  public FunctionResult callFunction(String[] device, String function, Parameter... parameters) throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
    if(parameters.length == 1 && "attachedto".equals(function))
    {
      for(String d: device)
      {
        String deviceid = d.split("_")[2];
        if(recievers.containsKey(deviceid))
        {
          GotDevice get = recievers.get(deviceid);
          get.attachedto = parameters[0].getData().toString();
        }
      }
    }
    return new FunctionResult();
  }

  public String getDriverId() {
    return String.format(DRIVER_ID, id);
  }

  public void update() {
    LinkedList<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
    for (GotDevice gd : recievers.values()) {
      dd.add(generateDeviceDescriptor(gd.id));
    }

    DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, cd, fd, vd);
    devent.sendData(due);
  }

  public void update(String[] devicecategories) {
    DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), null, cd, fd, vd);
    devent.sendData(due);
  }

  public void update(DeviceId[] devices) {
    update();
  }

  private DeviceDescriptor generateDeviceDescriptor(long id) {
    return new DeviceDescriptor("GoT " + id, "got_" + id, new String[]{CATEGORY});
  }

  private static String parseID(String filename) {
    int start = filename.lastIndexOf('-') + 1;
    int end = filename.lastIndexOf('.');
    return filename.substring(start, end);
  }

  /*Implements SensorPackageListener*/
  public void packageArrived(SensorPackage sensorpackage) {
    GotDevice d;
    //Is it a known sender?
    if (recievers.containsKey("" + sensorpackage.getSender())) {
      d = recievers.get("" + sensorpackage.getSender());
    }else{
      d = new GotDevice(sensorpackage.getSender());
      recievers.put("" + sensorpackage.getSender(), d);
      LinkedList<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
      dd.add(generateDeviceDescriptor(sensorpackage.getSender()));
      DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, cd, fd, vd);
      devent.sendData(due);
    }
    if (sensorpackage.getValidation() && (!"".equals(d.attachedto))) {
      d.valid_packages++;
      Point3 p = new Point3(sensorpackage.getPosition().getX(), sensorpackage.getPosition().getY(), sensorpackage.getPosition().getZ());
      VariableChangedEvent evt = new VariableChangedEvent();
      evt.getLocation().add(new LocationUpdate(d.attachedto, p));
      vevent.sendData(evt);
    }
  }

  public void unknownPackageArrived(String str) {
    //ignore
  }
}

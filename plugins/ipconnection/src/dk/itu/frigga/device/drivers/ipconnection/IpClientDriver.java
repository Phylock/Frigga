/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import dk.itu.frigga.protocol.FriggaConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class IpClientDriver implements Driver, FriggaConnectionListener {
  //IPojo
  private org.apache.felix.ipojo.handlers.event.publisher.Publisher devent;
  private org.apache.felix.ipojo.handlers.event.publisher.Publisher vevent;
  private LogService log;
  //OSGi
  private final BundleContext context;

  //Private
  private static final String CATEGORY_CLIENT = "client";
  private static final String CATEGORY_IPCLIENT = "ipclient";
  private final List<CategoryDescriptor> cd = new LinkedList<CategoryDescriptor>();
  private final List<FunctionDescriptor> fd = new LinkedList<FunctionDescriptor>();
  private final List<VariableDescriptor> vd = new LinkedList<VariableDescriptor>();
  
  private String id;
  private Server server;
  private final List<FriggaConnectionHandler> clients = new ArrayList<FriggaConnectionHandler>(); //Note: maybe use hashtable

  public IpClientDriver(BundleContext context) {
    this.context = context;

    cd.add(new CategoryDescriptor(CATEGORY_CLIENT, null, new String[]{"showdialog", "closedialog"}));
    fd.add(new FunctionDescriptor("showdialog", new String[]{"id"}));
    fd.add(new FunctionDescriptor("closedialog", new String[]{"id"}));

    cd.add(new CategoryDescriptor(CATEGORY_IPCLIENT, new String[]{"ip"}, new String[]{"showdialog", "closedialog"}));
    fd.add(new FunctionDescriptor("showdialog", new String[]{"id"}));
    fd.add(new FunctionDescriptor("closedialog", new String[]{"id"}));
    vd.add(new VariableDescriptor("ip", "String"));
  }

  public FunctionResult callFunction(String[] device, String function, Parameter... parameters) throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getDriverId() {
    return id;
  }

  public void update() {
    DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), null, cd, fd, vd);
    devent.sendData(due);
  }

  public void update(String[] devicecategories) {
    //TODO: check categories, for now send all
    DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), null, cd, fd, vd);
    devent.sendData(due);
  }

  public void update(DeviceId[] devices) {
  }

  
  private void validate() {
  }
  
  private void invalidate() {
    if(server != null)
      server.deactivate();
  }

  public void updated(Dictionary conf) {
    try {
      id = parseID(conf.get("felix.fileinstall.filename").toString());
      if(server != null)
        server.deactivate();
      
      server = new Server(conf, this);
      server.activate();
    } catch (IOException ex) {
      Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static String parseID(String filename) {
    int start = filename.lastIndexOf('-') + 1;
    int end = filename.lastIndexOf('.');
    return filename.substring(start, end);
  }

  public void newConnection(FriggaConnection connection) {
    clients.add(new FriggaConnectionHandler(connection));
  }
}

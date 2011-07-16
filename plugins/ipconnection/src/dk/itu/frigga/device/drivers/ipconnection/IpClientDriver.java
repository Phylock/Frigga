/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.ipconnection;

import dk.itu.frigga.core.clientapi.ClientManager;
import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdate;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.InvalidFunctionException;
import dk.itu.frigga.device.InvalidParameterException;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.UnknownDeviceException;
import dk.itu.frigga.device.VariableChangedEvent;
import dk.itu.frigga.device.VariableUpdate;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import dk.itu.frigga.protocol.FriggaConnection;
import dk.itu.frigga.protocol.InvalidFilterException;
import dk.itu.frigga.protocol.InvalidFilterGroupException;
import dk.itu.frigga.protocol.Message;
import dk.itu.frigga.protocol.Option;
import dk.itu.frigga.protocol.ProtocolException;
import dk.itu.frigga.protocol.Selection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
  private ClientManager clientmanager;
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
  private final List<FriggaConnectionHandler> unknownclients = new ArrayList<FriggaConnectionHandler>(); //Note: maybe use hashtable
  private final Map<String, FriggaConnectionHandler> clients = new HashMap<String, FriggaConnectionHandler>();

  public IpClientDriver(BundleContext context) {
    this.context = context;

    cd.add(new CategoryDescriptor(CATEGORY_CLIENT, null, new String[]{"showdialog", "closedialog"}));
    fd.add(new FunctionDescriptor("showdialog", new String[]{"id"}));
    fd.add(new FunctionDescriptor("closedialog", new String[]{"id"}));

    cd.add(new CategoryDescriptor(CATEGORY_IPCLIENT, new String[]{"ip"}, new String[]{"showdialog", "closedialog"}));
    vd.add(new VariableDescriptor("ip", "String"));
  }

  public FunctionResult callFunction(String[] devices, String function, Parameter... parameters) throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
    if ("showdialog".equalsIgnoreCase(function)) {
      String type = "";
      String id = "";
      String description = "";
      String current = "";

      Map<String, TempSelection> selections = new HashMap<String, TempSelection>();

      float significance = 0;
      for (Parameter p : parameters) {
        String param = p.getName();
        if ("type".equalsIgnoreCase(param)) {
          type = String.valueOf(p.getData());
        } else if ("id".equalsIgnoreCase(param)) {
          id = String.valueOf(p.getData());
        } else if ("description".equalsIgnoreCase(param)) {
          description = String.valueOf(p.getData());
        } else if ("current".equalsIgnoreCase(param)) {
          current = String.valueOf(p.getData());
        } else if ("significance".equalsIgnoreCase(param)) {
          significance = Float.parseFloat(String.valueOf(p.getData()));
        } else if (param.startsWith("selection.")) {
          int lastidx = param.lastIndexOf(".");
          String subparam = param.substring(lastidx + 1);
          String selection = param.substring(0, lastidx);
          if (!selections.containsKey(selection)) {
            selections.put(selection, new TempSelection());
          }
          if ("filter".equalsIgnoreCase(subparam)) {
            selections.get(selection).filter = String.valueOf(p.getData());
          } else if ("subject".equalsIgnoreCase(subparam)) {
            selections.get(selection).subject = String.valueOf(p.getData());
          } else if ("selected".equalsIgnoreCase(subparam)) {
            selections.get(selection).selected = Boolean.valueOf(String.valueOf(p.getData()));
          }
        }
      }

      Message m = new Message();
      Option opt = new Option(id, current, type, current, description, significance);
      for (TempSelection s : selections.values()) {
        try {
          opt.addSelection(new Selection(s.filter, s.subject, s.selected));
        } catch (InvalidFilterException ex) {
          Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFilterGroupException ex) {
          Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      for (String device : devices) {
        if (clients.containsKey(device)) {
          FriggaConnectionHandler handler = clients.get(device);
          m.getOptions().add(opt);
          try {
            handler.getConnection().sendMessage(m);
          } catch (IOException ex) {
            Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ProtocolException ex) {
            Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InterruptedException ex) {
            Logger.getLogger(IpClientDriver.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    }
    return new FunctionResult();
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
    if (server != null) {
      server.activate();
    }
  }

  private void invalidate() {
    if (server != null) {
      server.deactivate();
    }
  }

  public void updated(Dictionary conf) {
    try {
      id = parseID(conf.get("felix.fileinstall.filename").toString());
      if (server != null) {
        server.deactivate();
      }

      server = new Server(conf, this);
      server.activate();
    } catch (IOException ex) {
      log.log(LogService.LOG_ERROR, null, ex);
    }
  }

  private static String parseID(String filename) {
    int start = filename.lastIndexOf('-') + 1;
    int end = filename.lastIndexOf('.');
    return filename.substring(start, end);
  }

  public void newUnknownConnection(FriggaConnection connection) {
    unknownclients.add(new FriggaConnectionHandler(this, clientmanager, connection));
  }

  public void newIdentifiedConnection(FriggaConnectionHandler handler) {
    if (unknownclients.contains(handler)) {
      unknownclients.remove(handler);
    }
    clients.put(handler.getDevice().toString(), handler);
    //Register device
    LinkedList<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
    DeviceDescriptor device = new DeviceDescriptor(handler.getDevice().toString(), handler.getDevice().toString(), new String[]{CATEGORY_CLIENT, CATEGORY_IPCLIENT});
    dd.add(device);
    DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, cd, fd, vd);
    devent.sendData(due);
    //send device state
    VariableChangedEvent evt = new VariableChangedEvent();
    evt.getState().add(new DeviceUpdate(device.getSymbolic(), true));
    evt.getVariables().add(new VariableUpdate(device.getSymbolic(), "ip", handler.getConnection().getPeer().getHost()));
    vevent.sendData(evt);
  }

  private class TempSelection {

    String filter = "";
    String subject = "";
    boolean selected = false;
  }
}

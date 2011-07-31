/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.eye;

import dk.itu.frigga.device.DeviceId;
import dk.itu.frigga.device.DeviceUpdate;
import dk.itu.frigga.device.DeviceUpdateEvent;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.VariableChangedEvent;
import dk.itu.frigga.device.VariableUpdate;
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.osgi.framework.BundleException;
import org.osgi.service.log.LogService;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class EyeDriver implements Driver {

    private static final String DRIVER_ID = "EyeTrack-%s";
    private static final String CATEGORY = "eyetrack";
    //External services, initialized by DependencyManager
    private LogService log;
    private Publisher devent;
    private Publisher vevent;
    //private
    private String id;
    private DeviceDescriptor device_descriptor = null;
    private final List<CategoryDescriptor> cd = new LinkedList<CategoryDescriptor>();
    private final List<FunctionDescriptor> fd = new LinkedList<FunctionDescriptor>();
    private final List<VariableDescriptor> vd = new LinkedList<VariableDescriptor>();
    private EyeView view;
    private BundleContext context;
    private final EyeChangeListener listener = new EyeChangeListener();
    private String attachedto = "";

    public EyeDriver(BundleContext context) {
        this.context = context;
        cd.add(new CategoryDescriptor(CATEGORY, new String[]{"lookat", "attachedto", "local"}, new String[]{"attach"}));
        fd.add(new FunctionDescriptor("attach", new String[]{"client"}));
        vd.add(new VariableDescriptor("lookat", "string"));
        vd.add(new VariableDescriptor("attachedto", "string"));
        vd.add(new VariableDescriptor("local", "string"));
    }

    /** Implements Driver **/
    @Override
    public FunctionResult callFunction(String[] devices, String function, Parameter... parameters) {
        if ("attach".equalsIgnoreCase(function)) {
            attachedto = (parameters.length > 0) ? parameters[0].getData().toString() : "";
            if (id != null) {
                VariableChangedEvent vce = new VariableChangedEvent();
                vce.getVariables().add(new VariableUpdate(device_descriptor.getSymbolic(), "attachedto", attachedto));
                vevent.sendData(vce);
            }

        }
        return new FunctionResult();
    }

    @Override
    public void update() {
        List<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
        dd.add(device_descriptor);

        DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, cd, fd, vd);
        devent.sendData(due);

        String device_id = device_descriptor.getSymbolic();
        VariableChangedEvent vce = new VariableChangedEvent();
        vce.getState().add(new DeviceUpdate(device_id, true));
        vce.getVariables().add(new VariableUpdate(device_id, "attachedto", attachedto));
        vce.getVariables().add(new VariableUpdate(device_id, "lookat", ""));
        vce.getVariables().add(new VariableUpdate(device_id, "local", ""));
        vevent.sendData(vce);
    }

    @Override
    public void update(String[] devicecategories) {
        DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), null, cd, fd, vd);
        devent.sendData(due);
    }

    @Override
    public void update(DeviceId[] devices) {
        if (device_descriptor != null && Arrays.asList(devices).contains(device_descriptor.getSymbolic())) {
            updateDevice();
        }
    }

    private void updateDevice() {
        List<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
        dd.add(device_descriptor);

        DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, null, null, null);
        devent.sendData(due);
    }

    /** iPOJO Callbacks **/
    private void validate() {
        view = new EyeView();
        view.addListener(listener);
        view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        view.addWindowListener(new EyeViewListener(context));
        view.setVisible(true);
    }

    private void invalidate() {
        view.dispose();
        view = null;
    }

    public void updated(Dictionary conf) {
        id = parseID(conf.get("felix.fileinstall.filename").toString());

        String name = conf.get("frigga.eye.name").toString();
        String symbolic = conf.get("frigga.eye.symbolic").toString();

        listener.setId(symbolic);

        DeviceDescriptor dd = new DeviceDescriptor(name, symbolic, new String[]{CATEGORY});



        if (!dd.equals(device_descriptor)) {
            device_descriptor = dd;
            //updateDevice();
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

    private class EyeViewListener extends WindowAdapter {

        private BundleContext context;

        public EyeViewListener(BundleContext context) {
            this.context = context;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                context.getBundle().stop();
            } catch (BundleException ex) {
                Logger.getLogger(EyeDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class EyeChangeListener implements EyeChange {

        private String id = null;

        public void setId(String id) {
            this.id = id;
        }

        public void selectionChanged(String selection) {
            if (id != null) {
                VariableChangedEvent vce = new VariableChangedEvent();
                vce.getVariables().add(new VariableUpdate(id, "lookat", selection));
                vevent.sendData(vce);
            }
        }

        public void localChanged(Point p) {
            //TODO: disabled for now, this is spamming to much, add some kin of filter or forced delay
      /*if (id != null) {
            VariableChangedEvent vce = new VariableChangedEvent();
            vce.getVariables().add(new VariableUpdate(id, "local", p.toString()));
            vevent.sendData(vce);
            }*/
        }
    }
}

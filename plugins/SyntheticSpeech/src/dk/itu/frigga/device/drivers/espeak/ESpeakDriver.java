/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.espeak;

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
import dk.itu.frigga.device.descriptor.CategoryDescriptor;
import dk.itu.frigga.device.descriptor.DeviceDescriptor;
import dk.itu.frigga.device.descriptor.FunctionDescriptor;
import dk.itu.frigga.device.descriptor.VariableDescriptor;
import dk.itu.frigga.espeak.ESpeak;
import dk.itu.frigga.espeak.ESpeakProcess;
import dk.itu.frigga.espeak.EspeakMessage;
import dk.itu.frigga.espeak.EspeakParam;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 *
 * @author phylock
 */
public class ESpeakDriver implements Driver{

    private static final String DRIVER_ID = "SyntheticSpeech-%s";
    private static final String CATEGORY = "synthetic_speech";
    //constans
    private static final String MESSAGE = "message";
    private static final String AMPLITUDE = "amplitude";
    private static final String PITCH = "pitch";
    private static final String WORDS_PER_MINUTE = "words_per_min";
    private static final String CAPITAL_PITCH = "capital_pitch";
    private static final String VOICE = "voice";
    private static final String CONTAINS_MARKUP = "contains_markup";
    //OSGi
    private BundleContext context;
    //iPOJO
    private LogService log;
    private Publisher devent;
    private Publisher vevent;
    //Driver
    private String id;
    private String device_id;

    private final List<CategoryDescriptor> cd = new LinkedList<CategoryDescriptor>();
    private final List<FunctionDescriptor> fd = new LinkedList<FunctionDescriptor>();
    private final List<VariableDescriptor> vd = new LinkedList<VariableDescriptor>();
    private final List<DeviceDescriptor> dd = new LinkedList<DeviceDescriptor>();
    //private
    private ESpeak espeak;

    public ESpeakDriver(BundleContext context) {
        this.context = context;
        cd.add(new CategoryDescriptor(CATEGORY, new String[]{}, new String[]{"say"}));
        fd.add(new FunctionDescriptor("say", new String[]{MESSAGE, AMPLITUDE, PITCH, WORDS_PER_MINUTE, CAPITAL_PITCH, VOICE, CONTAINS_MARKUP}));
    }

    //@Validate
    private void validate() {
        espeak = new ESpeakProcess();
    }

    //@Invalidate
    private void invalidate() {
        espeak = null;
    }

    public void updated(Dictionary conf) {
        id = parseID(conf.get("felix.fileinstall.filename").toString());
        device_id = "espeak-" + id;
        dd.clear();
        dd.add(new DeviceDescriptor("ESpeak-" + id, device_id, new String[]{CATEGORY}));
    }

    /** Implements Driver **/
    public FunctionResult callFunction(String[] device, String function, Parameter... parameters) throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
        if ("say".equalsIgnoreCase(function)) {
            Map<String, String> param = new HashMap<String, String>();
            for (Parameter parameter : parameters) {
                param.put(parameter.getName(), parameter.getData().toString());
            }
            EspeakMessage message = null;
            EspeakParam eparam = new EspeakParam();
            if (param.containsKey(MESSAGE)) {
                message = new EspeakMessage(param.get(MESSAGE));
            } else if (param.containsKey("")) {
                message = new EspeakMessage(param.get(""));
            }
            if (message != null) {
                if (param.containsKey(AMPLITUDE)) {
                    eparam.setAmplitude(Integer.parseInt(param.get(AMPLITUDE)));
                }
                if (param.containsKey(PITCH)) {
                    eparam.setPitch(Integer.parseInt(param.get(PITCH)));
                }
                if (param.containsKey(WORDS_PER_MINUTE)) {
                    eparam.setWords_per_min(Integer.parseInt(param.get(WORDS_PER_MINUTE)));
                }
                if (param.containsKey(CAPITAL_PITCH)) {
                    eparam.setCapital_pitch(Integer.parseInt(param.get(CAPITAL_PITCH)));
                }
                if (param.containsKey(VOICE)) {
                    eparam.setVoice(param.get(VOICE));
                }
                if (param.containsKey(CONTAINS_MARKUP)) {
                    eparam.setContains_markup(Boolean.parseBoolean(param.get(CONTAINS_MARKUP)));
                }
                try {
                    espeak.say(message, eparam);
                } catch (IOException ex) {
                    log.log(LogService.LOG_ERROR, "could not execute say command in espeak", ex);
                }
            }
        }
        return new FunctionResult();
    }

    public String getDriverId() {
        return String.format(DRIVER_ID, id);
    }

    public void update() {
        DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), dd, cd, fd, vd);
        devent.sendData(due);

        VariableChangedEvent changed = new VariableChangedEvent();
        changed.getState().add(new DeviceUpdate(device_id, true));
        vevent.sendData(log);
    }

    public void update(String[] devicecategories) {
        DeviceUpdateEvent due = new DeviceUpdateEvent(getDriverId(), null, cd, fd, vd);
        devent.sendData(due);
    }

    public void update(DeviceId[] devices) {
        update();
    }

    private static String parseID(String filename) {
        int start = filename.lastIndexOf('-') + 1;
        int end = filename.lastIndexOf('.');
        return filename.substring(start, end);
    }

    /*Implements SensorPackageListener*/
}

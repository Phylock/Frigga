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
import it.polito.elite.domotics.dog2.dog2leash.Dog2JLeash;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlrpc.XmlRpcException;
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
    private DogParser listener;
    private Dog2JLeash dogGateway;
    /*** TODO: read parameter, for now assume that the Dog gateway is running on localhost */
    public static String DEFAULT_DOG_ADDRESS = "http://localhost:65300/RPC2";
    private String session;
    private boolean connected;

    @Override
    public FunctionResult callFunction(Device[] devices, Executable function, Parameter... parameters)
            throws UnknownDeviceException, InvalidFunctionException, InvalidParameterException {
        return function.execute(devices, parameters);
    }

    public DogDriver() {
        log.log(LogService.LOG_INFO, "Dog Driver: Initialize");
        listener = new DogParser(this);
        connected = false;
    }

    public void connect(String address) throws XmlRpcException, IOException {
        dogGateway = new Dog2JLeash();
        session = dogGateway.connect(address);
        dogGateway.addDog2MessageListener(listener);
        connected = true;
    }

    public void disconnect() throws XmlRpcException, IOException {
        if (connected) {
            dogGateway.disconnect();
            connected = false;
        }
    }

    public void send(DogMessage message) {
        String command = message.generateXmlString(session);
        try {
            if (!Dog2JLeash.validate(command)) {
                System.out.println("XmlMessage not valid. See console error for more information");
                return;
            }
            System.out.println("Request:");
            System.out.println(command);
            if (!dogGateway.sendMessage(command)) {
                System.out.println("Wrong session id");
            } else {
                //this.gui.clearXmlTextArea();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setLampState(boolean on) {
        String devices[] = new String[]{"SimpleLamp_lamp7_livingroom"};
        Command commands = new Command(devices, ((on) ? "on" : "off"), null);

        try {
            DogMessage command = DogProtocol.generateCommandMessage(commands);
            send(command);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void LampStatus() {
        String devices[] = new String[]{"SimpleLamp_lamp7_livingroom"};

        try {
            DogMessage command = DogProtocol.generateStatusRequest(devices);
            send(command);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        DogDriver driver = new DogDriver();
        try {
            driver.connect(DEFAULT_DOG_ADDRESS);
            driver.update();
            Thread.sleep(1000 * 5);
            /*driver.LampStatus();
            Thread.sleep(1000 * 5);
            driver.setLampState(true);
            Thread.sleep(1000 * 10);*/



            driver.disconnect();
        } catch (InterruptedException ex) {
            Logger.getLogger(DogDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(DogDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DogDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        try {
            DogMessage command = DogProtocol.generateDescribeDevice(null);
            send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }

    public void update(String[] devicecategories) {
        try {
            DogMessage command = DogProtocol.generateDescribeDeviceCategory(devicecategories);
            send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }

    public void update(DeviceId[] devices) {
        try {
            int length = devices.length;
            String[] d = new String[length];
            for(int i = 0; i < length; i++)
            {
                d[i] = devices[i].toString();
            }
            DogMessage command = DogProtocol.generateDescribeDevices(d);
            send(command);
        } catch (ParserConfigurationException ex) {
            log.log(LogService.LOG_WARNING, null, ex);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog;

import dk.itu.frigga.device.Device;
import dk.itu.frigga.device.FunctionResult;
import dk.itu.frigga.device.Driver;
import dk.itu.frigga.device.Executable;
import dk.itu.frigga.device.Parameter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author phylock
 */
public class Function implements Executable{
    private DogDriver driver;
    private Connection connection;
    private String function;

    public Function(DogDriver driver, Connection connection, String function) {
        this.driver = driver;
        this.connection = connection;
        this.function = function;
    }

    @Override
    public Driver associatedDriver() {
        return driver;
    }

    @Override
    public FunctionResult execute(Device[] devices, Parameter... parameters) {
        try {
            String[] items = new String[devices.length];
            for (int i = 0; i < devices.length; i++) {
                items[i] = devices[i].getId().toString();
            }
            //TODO: how to set parameters ??
            Command command = new Command(items, function, null);
            DogMessage message = DogProtocol.generateCommandMessage(command);
            connection.send(message);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Function.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new FunctionResult();
    }

    @Override
    public String getName() {
        return function;
    }

}

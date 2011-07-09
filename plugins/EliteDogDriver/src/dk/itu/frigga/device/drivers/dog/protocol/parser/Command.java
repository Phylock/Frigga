/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device.drivers.dog.protocol.parser;

import dk.itu.frigga.device.Parameter;

/**
 *
 * @author phylock
 */
public class Command {
    private final String[] devices;
    private final String command;
    private final Parameter[] parameters;

    /**
     *
     * @param devices devices to send the command
     * @param command the command name
     * @param parameters parameters, null if none
     */
    public Command(String[] devices, String command, Parameter[] parameters) {
        this.devices = devices;
        this.command = command;
        this.parameters = parameters;
    }

    public String getCommand() {
        return command;
    }

    public String[] getDevices() {
        return devices;
    }

    public Parameter[] getParameters() {
        return parameters;
    }
}

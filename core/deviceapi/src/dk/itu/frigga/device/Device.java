/*
 * Copyright (c) 2011 Tommy Andersen and Mikkel Wendt-Larsen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package dk.itu.frigga.device;

import java.util.HashMap;

/**
 *
 * @author phylock
 */
public final class Device {
    private final DeviceId id;
    private final DeviceCategory type;
    private final HashMap<String, Variable> variables;
    private final HashMap<String, IVariableListener> listeners;
    private final Location location;
    
    public Device(final DeviceId id,
            final DeviceCategory type,
            final HashMap<String, Variable> variables,
            final HashMap<String, IVariableListener> listeners,
            final Location location)
    {
        this.id = id;
        this.type = type;
        this.variables = variables;
        this.listeners = listeners;
        this.location = location;
    }

    public DeviceId getId() {
        return id;
    }

    public HashMap<String, IVariableListener> getListeners() {
        return listeners;
    }

    public Location getLocation() {
        return location;
    }

    public DeviceCategory getType() {
        return type;
    }

    public boolean isOfType(String type)
    {
        return this.type.equals(type);
    }

    public boolean isOfType(DeviceCategory category)
    {
        return this.type.equals(category);
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }


    
}

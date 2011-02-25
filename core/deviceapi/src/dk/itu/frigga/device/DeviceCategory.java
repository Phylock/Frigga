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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author phylock
 */
public class DeviceCategory {
    private final String type;
    private final HashMap<String, Executable> functions;
    private final String[] variables;
    private final String[] listeners;

    public DeviceCategory(String type, HashMap<String, Executable> functions, String[] variables, String[] listeners) {
        this.type = type;
        this.functions = functions;
        this.variables = variables;
        this.listeners = listeners;
    }

    public final String getTypeString()
    {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        // Compare by reference, this is a fast comparrison, and if true, is
        // always equal, but if false not always not equal.
        if (obj == this) {
            return true;
        }
        // Added possibility to compare with string as well.
        if (obj.getClass() == type.getClass()) {
            if ((this.type == null) ? (obj != null) : !this.type.equals(obj)) {
                return false;
            }
            return true; 
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeviceCategory other = (DeviceCategory) obj;
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    /**
     * Since we only consider the type id string, we simply call it's hashCode
     * function.
     *
     * @return The hashcode integer.
     *
     * @author Tommy Andersen (toan@itu.dk)
     */
    @Override
    public int hashCode() {
        return (this.type != null ? this.type.hashCode() : 0);
    }

}

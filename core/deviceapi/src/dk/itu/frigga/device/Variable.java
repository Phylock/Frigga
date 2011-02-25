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

import java.util.LinkedList;

/**
 *
 * @author Tommy Andersen <toan@itu.dk>
 */
public abstract class Variable {
    protected final LinkedList<VariableListener> listeners = new LinkedList<VariableListener>();
    private final String name;
    protected final Device device;

    public Variable(final Device device, final String name)
    {
        this.name = name;
        this.device = device;
    }

    public void addVariableChangedListener(VariableListener listener)
    {
        listeners.add(listener);
    }

    public void removeVariableChangedListener(VariableListener listener)
    {
        listeners.remove(listener);
    }

    public abstract Object getValue();

    @Override
    public String toString() {
        return "Variable '" + name + "'";
    }
}

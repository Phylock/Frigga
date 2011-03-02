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

package dk.itu.frigga.config;

/**
 *
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Setting
{
    public enum Type
    {
        STRING, NUMBER, BOOLEAN, BINARY
    }

    private Object[] values;
    private String key;
    private Type type;
    private final ConfigManager owner;

    public Setting(final ConfigManager owner,  final String key, final Object[] values, final Type type)
    {
        this.owner = owner;
        this.key = key;
        this.values = values;
        this.type = type;
    }

    public static Type stringToType(final String typeStr) throws WrongSettingTypeException
    {
        if (typeStr.equals("string")) return Type.STRING;
        if (typeStr.equals("number")) return Type.NUMBER;
        if (typeStr.equals("boolean")) return Type.BOOLEAN;
        if (typeStr.equals("binary")) return Type.BINARY;

        throw new WrongSettingTypeException(typeStr);
    }

    public Object getValue()
    {
        if (values == null) return null;
        return values[0];
    }

    public Object getValue(int index)
    {
        return values[index];
    }

    public void setValue(final String value)
    {
        setValue(new String[]{ value });
    }

    public void setValue(final String[] value)
    {
        synchronized (this)
        {
            values = value;
            type = Type.STRING;
        }

        notifyChanged();
    }

    public void setValue(final int value)
    {
        setValue(new int[]{ value });
    }

    public void setValue(final int[] value)
    {
        synchronized (this)
        {
            values = new Double[value.length];
            System.arraycopy(value, 0, values, 0, value.length);
            type = Type.NUMBER;
        }

        notifyChanged();
    }

    public void setValue(final float value)
    {
        setValue(new float[]{ value });
    }

    public void setValue(final float[] value)
    {
        synchronized (this)
        {
            values = new Double[value.length];
            System.arraycopy(value, 0, values, 0, value.length);
            type = Type.NUMBER;
        }

        notifyChanged();
    }

    public void setValue(final double value)
    {
        setValue(new double[]{ value });
    }

    public void setValue(final double[] value)
    {
        synchronized (this)
        {
            values = new Double[value.length];
            System.arraycopy(value, 0, values, 0, value.length);
            type = Type.NUMBER;
        }

        notifyChanged();
    }

    public void setValue(final boolean value)
    {
        setValue(new boolean[]{ value });
    }

    public void setValue(final boolean[] value)
    {
        synchronized (this)
        {
            values = new Boolean[value.length];
            System.arraycopy(value, 0, values, 0, value.length);
            type = Type.BOOLEAN;
        }

        notifyChanged();
    }

    public void setValue(final Object value)
    {
        setValue(new String[]{ value.toString() });
    }

    public void setValue(final Object[] value)
    {
        synchronized (this)
        {
            values = new String[value.length];
            System.arraycopy(value, 0, values, 0, value.length);
            type = Type.STRING;
        }

        notifyChanged();
    }

    public int valueCount()
    {
        if (values == null) return 0;
        return values.length;
    }

    public Type valueType()
    {
        return type;
    }

    public String getKey()
    {
        return key;
    }

    @Override
    public String toString()
    {
        String str = key + " = ";

        if (values.length > 1)
        {
            str = str + "{ ";
        }

        for (int i = 0; i < values.length; i++)
        {
            if (i > 0) str = str + ", ";
            str = str + " " + values[i];
        }

        if (values.length > 1)
        {
            str = str + " }";
        }

        return str;
    }

    private void notifyChanged()
    {
        owner.updateSetting(this);
    }
}

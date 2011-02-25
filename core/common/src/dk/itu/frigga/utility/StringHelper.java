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

package dk.itu.frigga.utility;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author Tommy Andersen (toan@itu.dk)
 */
public class StringHelper
{
    private final static String HEX_STRING = "0123456789abcdef";

    public static byte[] hexStringToByteArray(final String hex)
    {
        boolean hasFullByte = true;
        int b = 0;
        int bufferSize = 0;
        int bytesAdded = 0;

        for (char c : hex.toCharArray())
        {
            if ((c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' || c <= 'F'))
            {
                bufferSize++;
            }
        }

        byte[] result = new byte[bufferSize / 2];

        for (char c : hex.toCharArray())
        {
            int pos = HEX_STRING.indexOf(Character.toLowerCase(c));

            if (pos > -1)
            {
               b = (b << 4) | (pos & 0xFF);
               hasFullByte = !hasFullByte;

               if (hasFullByte)
               {
                   result[bytesAdded] = (byte) (b & 0xFF);
                   b = 0;
                   bytesAdded++;
               }
            }
        }

        return result;
    }

    public static ByteBuffer hexStringToByteBuffer(final String hex)
    {
        byte[] bytes = hexStringToByteArray(hex);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);

        return buffer;
    }
    
    public static String[] splitStringBySeperator(final String str, final String seperator)
    {
        if (str == null)
        {
            return null;
        }

        String sep = seperator;
        if (sep == null) sep = " ";

        if (sep.equals("")) return new String[]{ str };

        int len = str.length();
        int start = 0;
        int pos = str.indexOf(sep, start);

        ArrayList<String> substrings = new ArrayList<String>();

        while (pos > start)
        {
            substrings.add(str.substring(start, pos));
            start = pos + sep.length();
            pos = str.indexOf(sep, start);
        }
        
        substrings.add(str.substring(start, str.length()));

        return substrings.toArray(new String[substrings.size()]);
    }
}

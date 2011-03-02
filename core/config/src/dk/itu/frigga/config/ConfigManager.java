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

import dk.itu.frigga.Singleton;
import dk.itu.frigga.utility.StringHelper;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 *
 * @author Tommy Andersen (toan@itu.dk)
 */
public class ConfigManager extends Singleton {
    private static ConfigManager instance;

    private final DocumentBuilder builder;
    private final HashMap<String, Setting> settings = new HashMap<String, Setting>();
    private final HashMap<String, SettingListener> listeners = new HashMap<String, SettingListener>();

    private ConfigManager() throws ParserConfigurationException
    {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public static synchronized ConfigManager getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new ConfigManager();
            } 
            catch (ParserConfigurationException ex)
            {
                Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    public void attachConfig(final String filename) throws FileNotFoundException, IllegalSettingValueException, WrongSettingTypeException, MissingKeyException, IOException, InvalidXMLConfigException
    {
        attachConfig(new FileInputStream(filename));
    }

    public void attachConfig(InputStream stream) throws IllegalSettingValueException, WrongSettingTypeException, MissingKeyException, IOException, InvalidXMLConfigException
    {
        parseStream(stream);
    }

    public boolean hasSetting(final String name)
    {
        return settings.containsKey(name);
    }

    public Setting getSetting(final String name) throws SettingNotFoundException
    {
        if (settings.containsKey(name))
        {
            return settings.get(name);
        }
        else
        {
            throw new SettingNotFoundException(name);
        }
    }

    protected void updateSetting(final Setting setting)
    {
        // Do Stuff
    }

    private void parseStream(InputStream stream) throws IOException, InvalidXMLConfigException, IllegalSettingValueException, WrongSettingTypeException, MissingKeyException
    {
        try
        {
            Document doc = builder.parse(stream);
            Element root = doc.getDocumentElement();

            Element settingElem = XmlHelper.getFirstChildElement(root);

            while (settingElem != null)
            {
                parseElement(null, settingElem);
                settingElem = XmlHelper.getNextSiblingElement(settingElem);
            }
        }
        catch (SAXException ex)
        {
            throw new InvalidXMLConfigException();
        }

    }

    private void parseElement(Setting parent, Element elem) throws IllegalSettingValueException, WrongSettingTypeException, MissingKeyException, InvalidXMLConfigException
    {
        if (elem.getNodeName().equals("setting"))
        {
            parseSettingElement(parent, elem);
        }
        else if (elem.getNodeName().equals("include"))
        {
            if (elem.hasAttribute("filename"))
            {
                try
                {
                    attachConfig(elem.getAttribute("filename"));
                }
                catch (FileNotFoundException ex)
                {
                    // File not found, we accept that, and continue. We accept
                    // missing files to allow user configurations, that doesn't
                    // require the file to be present.
                }
                catch (IOException ex)
                {
                    // Error reading config file, we accept that and continue.
                }
            }
            else
            {
                // We need a filename to include a file.
                throw new InvalidXMLConfigException();
            }
        }
        else
        {
            // Fail gracefully, so that new tags may be added in the future
            // without severe consequences.
        }
    }

    private void parseSettingElement(Setting parent, Element elem) throws IllegalSettingValueException, WrongSettingTypeException, MissingKeyException
    {
        String key;
        String value = "";
        String type = "string";
        String delimiter = "";
        boolean hasValue = false;
        Object[] values = null;

        if (elem.hasAttribute("key"))
        {
            key = elem.getAttribute("key");
        }
        else
        {
            throw new MissingKeyException();
        }

        if (elem.hasAttribute("value"))
        {
            value = elem.getAttribute("value");
            hasValue = true;
        }

        if (elem.hasAttribute("delimiter"))
        {
            delimiter = elem.getAttribute("delimiter");
        }

        if (elem.hasAttribute("type"))
        {
            type = elem.getAttribute("type");
        }

        if (hasValue)
        {
            values = attributeToValues(value, delimiter, type);
        }

        settings.put(key, new Setting(this, key, values, Setting.stringToType(type)));
    }

    private static Object[] attributeToValues(String value, String delimiter, String typeStr) throws WrongSettingTypeException, IllegalSettingValueException
    {
        String[] values = StringHelper.splitStringBySeperator(value, delimiter);
        Object[] result = values;

        Setting.Type type = Setting.stringToType(typeStr);

        switch (type)
        {
            case NUMBER:
                result = new Double[values.length];

                for (int i = 0; i < values.length; i++)
                {
                    result[i] = new Double(values[i]);
                }
                break;
            case BOOLEAN:
                result = new Boolean[values.length];

                for (int i = 0; i < values.length; i++)
                {
                    if (values[i].equals("true"))
                    {
                        result[i] = true;
                    }
                    else if (values[i].equals("false"))
                    {
                        result[i] = false;
                    }
                    else
                    {
                        throw new IllegalSettingValueException(values[i], new String[] { "true", "false" } );
                    }
                }
                break;
            case BINARY:
                result = new ByteBuffer[values.length];

                for (int i = 0; i < values.length; i++)
                {
                    result[i] = StringHelper.hexStringToByteBuffer(values[i]);
                }
                break;
        }

        return result;
    }
}

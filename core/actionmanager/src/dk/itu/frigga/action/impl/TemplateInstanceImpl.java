package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.Replacement;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.action.TemplateInstance;
import dk.itu.frigga.action.filter.FilterFailedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-07
 */
public class TemplateInstanceImpl implements TemplateInstance
{
    public static final String FILE_COMMENT = "This is an automatically generated file, it is part of the Frigga project.";
    private final String name;
    private final TemplateData template;
    private final String propertiesFilename;
    private final Map<String, String> replacementValues = Collections.synchronizedMap(new HashMap<String, String>());

    public TemplateInstanceImpl(final String name, final TemplateData template)
    {
        this.name = name;
        this.template = template;
        this.propertiesFilename = "conf/instances/" + name + ".prop";

        File propertiesFile = new File(propertiesFilename);
        Properties properties = new Properties();
        boolean propertiesFound = false;

        if (propertiesFile.exists())
        {
            try
            {
                properties.load(new FileInputStream(propertiesFile));
                readReplacementsFromProperties(properties);
                propertiesFound = true;
            }
            catch (IOException e)
            {
                // Ignore
            }
        }

        if (!propertiesFound)
        {
            try
            {
                writeInstanceToProperties(properties);
                properties.store(new FileOutputStream(propertiesFile), FILE_COMMENT);
            }
            catch (IOException e)
            {
                // Ignore
            }
        }
    }

    public String getPropertiesFilename()
    {
        return propertiesFilename;
    }

    public String parseForReplacements(final String source)
    {
        StringBuffer resultString = new StringBuffer();
        try

        {
            Pattern regex = Pattern.compile("\\{([^}\\{]+)\\}");
            Matcher regexMatcher = regex.matcher(source);
            while (regexMatcher.find())
            {
                try
                {
                    String replacement = regexMatcher.group(1);
                    if (replacementValues.containsKey(replacement))
                    {
                        regexMatcher.appendReplacement(resultString, replacementValues.get(replacement));
                    }
                }
                catch (IllegalStateException ex)
                {
                    // appendReplacement() called without a prior successful call to find()
                }
                catch (IllegalArgumentException ex)
                {
                    // Syntax error in the replacement text (unescaped $ signs?)
                }
                catch (IndexOutOfBoundsException ex)
                {
                    // Non-existent backreference used the replacement text
                }
            }
            regexMatcher.appendTail(resultString);
        }
        catch (PatternSyntaxException ex)
        {
            // Syntax error in the regular expression
        }


        return resultString.toString();
    }

    public void removePropertiesFile()
    {
        File propertiesFile = new File(propertiesFilename);
        if (propertiesFile.exists())
        {
            propertiesFile.delete();
        }
    }

    private void writeInstanceToProperties(final Properties properties)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        properties.setProperty("instance.id", name);
        properties.setProperty("instance.creationtime", dateFormat.format(cal.getTime()));
        properties.setProperty("template.name", template.getTemplateInfo().getName());

        updateReplacementsInProperties(properties);
    }

    private void updateReplacementsInProperties(final Properties properties)
    {
        for (Map.Entry<String, String> replacement : replacementValues.entrySet())
        {
            properties.setProperty("replacement." + replacement.getKey(), replacement.getValue());
        }
    }

    private void readReplacementsFromProperties(final Properties properties)
    {
        for (Map.Entry<String, String> replacement : replacementValues.entrySet())
        {
            replacementValues.put(replacement.getKey(), properties.getProperty("replacement." + replacement.getKey(), replacement.getValue()));
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Template getTemplate()
    {
        return template;
    }

    @Override
    public Collection<Replacement> getReplacements()
    {
        return template.getReplacements();
    }

    @Override
    public void setReplacementValue(final Replacement replacement, final String value)
    {
        setReplacementValue(replacement.getName(), value);
    }

    @Override
    public void setReplacementValue(final String replacement, final String value)
    {
        replacementValues.put(replacement, value);

        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream(propertiesFilename));
            updateReplacementsInProperties(properties);
            properties.store(new FileOutputStream(propertiesFilename), FILE_COMMENT);
        }
        catch (IOException e)
        {
            // Ignore
        }
    }

    @Override
    public String getReplacementValue(Replacement replacement)
    {
        return replacementValues.get(replacement.getName());
    }

    @Override
    public String getReplacementValue(String replacement)
    {
        return replacementValues.get(replacement);
    }

    @Override
    public void run() throws FilterFailedException
    {
        template.run(this);
    }
}

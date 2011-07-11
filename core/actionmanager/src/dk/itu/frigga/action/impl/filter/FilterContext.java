package dk.itu.frigga.action.impl.filter;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.impl.TemplateInstanceImpl;
import dk.itu.frigga.action.impl.VariableContainer;
import dk.itu.frigga.action.impl.runtime.TemplateVariable;
import dk.itu.frigga.device.DeviceManager;
import java.sql.SQLException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public class FilterContext
{
    private final FilterDataGenerator filterGenerator;
    private final TemplateInstanceImpl templateInstance;
    private final DeviceManager deviceManager;
    private final Map<String, FilterOutput> storedOutputs = Collections.synchronizedMap(new HashMap<String, FilterOutput>());
    private final Set<String> allowedOutputs = Collections.synchronizedSet(new LinkedHashSet<String>());
    private final Map<String, TemplateVariable> variableValues = Collections.synchronizedMap(new HashMap<String, TemplateVariable>());

    public FilterContext(final DeviceManager deviceManager, final TemplateInstanceImpl instance, final VariableContainer variables)
    {
        this.deviceManager = deviceManager;
        FilterDataGenerator temp = null;
        try
        {
            temp = new FilterDataGenerator(deviceManager);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(FilterContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        filterGenerator = temp;
        templateInstance = instance;

        for (TemplateVariable variable : variables.getVariables())
        {
            variableValues.put(variable.getName(), new TemplateVariable(variable));
        }
    }

    public void setVariableValue(final String name, final String value)
    {
        TemplateVariable variable = variableValues.get(name);
        variable.set(value);
    }

    public TemplateVariable.Value getVariableValue(final String name)
    {
        return variableValues.get(name).getValue();
    }

    public void storeOutput(final String id, final FilterOutput output)
    {
        storedOutputs.put(id, output);
    }

    public boolean hasStoredOutput(final String id)
    {
        return storedOutputs.containsKey(id);
    }

    public FilterOutput getStoredOutput(final String id)
    {
        return storedOutputs.get(id);
    }

    public void allowValidation(final String name)
    {
        allowedOutputs.add(name);
    }

    public FilterOutput getSelectedOutput(final String selection)
    {
        FilterOutput output = new FilterOutput();

        for (Map.Entry<String, FilterOutput> entry : storedOutputs.entrySet())
        {
            if (entry.getKey().matches(selection))
            {
                output.devices.addAll(entry.getValue().devices);
            }
        }

        return output;
    }

    public FilterOutput getAllOutput()
    {
        FilterOutput output = new FilterOutput();

        for (Map.Entry<String, FilterOutput> entry : storedOutputs.entrySet())
        {
            output.devices.addAll(entry.getValue().devices);
        }

        return output;
    }

    public FilterOutput getValidateOutput()
    {
        FilterOutput output = new FilterOutput();

        for (Map.Entry<String, FilterOutput> entry : storedOutputs.entrySet())
        {
            if (allowedOutputs.contains(entry.getKey()))
            {
                output.devices.addAll(entry.getValue().devices);
            }
        }

        return output;
    }

    public DeviceManager getDeviceManager()
    {
        return deviceManager;
    }

    public String prepare(final String input)
    {
        StringBuffer resultString = new StringBuffer();
        try
        {
            Pattern regex = Pattern.compile("(\\{\\$([^}\\{\\$]+)\\})|(\\{#([^}\\{\\$]+)\\})");
            Matcher regexMatcher = regex.matcher(input);
            while (regexMatcher.find())
            {
                try
                {
                    String variable = regexMatcher.group(2);
                    String replacement = regexMatcher.group(4);

                    if (replacement != null)
                    {
                        regexMatcher.appendReplacement(resultString, templateInstance.getReplacementValue(replacement));
                    }
                    else if (variable != null)
                    {
                        regexMatcher.appendReplacement(resultString, getVariableValue(variable).asString());
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

    public FilterOutput run(final Filter filter) throws FilterFailedException
    {
        return runFilter(filter, filterGenerator.getFilterInput());
    }

    protected FilterOutput runFilter(final Filter filter, final FilterInput input) throws FilterFailedException
    {
        if (filter.childFilters.size() > 0)
        {
            switch (filter.mergeMethod())
            {
                case OR:
                    return filter.execute(this, new FilterInput(runOrFilter(filter.childFilters, filterGenerator.getFilterInput())));

                case AND:
                    return filter.execute(this, new FilterInput(runAndFilter(filter.childFilters, input)));
            }
        }

        return filter.run(this, input);
    }

    protected FilterOutput runAndFilter(final List<Filter> filters, final FilterInput input) throws FilterFailedException
    {
        FilterInput inp = input;
        FilterOutput output = new FilterOutput();
        boolean first = true;

        for (Filter filter : filters)
        {
            if (!first)
            {
                inp = new FilterInput(output);
            }

            inp = new FilterInput(runFilter(filter, inp));

            output = runFilter(filter, inp);
            first = false;
        }

        return output;
    }

    protected FilterOutput runOrFilter(final List<Filter> filters, final FilterInput input) throws FilterFailedException
    {
        FilterOutput output = new FilterOutput();

        for (Filter filter : filters)
        {
            output.merge(runFilter(filter, input));
        }

        return output;
    }

    public void debugMsg(final String message)
    {
        // Ignore for now
    }

    public void debugMsg(final String format, final String... parameters)
    {
        debugMsg(String.format(format, (Object[])parameters));
    }

    public void reportError(final String error)
    {
        Logger.getLogger(FilterContext.class.getName()).log(Level.WARNING, error);
    }

    public void reportFilterError(final Filter filter, final String error)
    {
        reportError("Error in filter: " + filter + ", message: " + error);
    }

    public Collection<FilterDeviceState> getUsedStates()
    {
        return filterGenerator.getFilterInput().getDevices();
    }
}

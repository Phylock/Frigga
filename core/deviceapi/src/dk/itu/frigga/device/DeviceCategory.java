package dk.itu.frigga.device;

import java.io.StringWriter;
import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class DeviceCategory
{
    private final Set<String> variables = Collections.synchronizedSet(new HashSet<String>());
    private final String name;

    public DeviceCategory(final String name)
    {
        this.name = name;
    }

    public DeviceCategory(final String name, Collection<String> variables)
    {
        this.name = name;
        this.variables.addAll(variables);
    }

    public DeviceCategory(final DeviceCategory other)
    {
        this.name = other.name;
        variables.addAll(other.variables);
    }

    public void addVariableName(final String variableName)
    {
        variables.add(variableName);
    }

    public Set<String> getVariableNames()
    {
        return variables;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o instanceof String) return name.equals(o);
        if (!(o instanceof DeviceCategory)) return false;

        DeviceCategory that = (DeviceCategory) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
}

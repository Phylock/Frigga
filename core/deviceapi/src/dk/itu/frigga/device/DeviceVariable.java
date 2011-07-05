package dk.itu.frigga.device;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class DeviceVariable
{
    private final String name;
    private final String type;
    private String value;

    public DeviceVariable(final String name, final String type)
    {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "DeviceVariable{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DeviceVariable)) return false;

        DeviceVariable that = (DeviceVariable) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getValue()
    {
        return value;
    }

    public String getType()
    {
        return type;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }
}

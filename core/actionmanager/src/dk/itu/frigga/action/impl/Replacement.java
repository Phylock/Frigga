package dk.itu.frigga.action.impl;

/**
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Replacement
{
    private final String name;
    private final String description;
    private final String type;
    private String value;

    public Replacement(final String name, final String description, final String type)
    {
        this.name = name;
        this.description = description;
        this.type = type;
        value = "";
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "Replacement{" + "name=" + name + ", description=" + description + ", type=" + type + '}';
    }

}

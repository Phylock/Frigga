package dk.itu.frigga.action;

/**
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Replacement
{
    private final String name;
    private final String description;
    private final String type;

    public Replacement(final String name, final String description, final String type)
    {
        this.name = name;
        this.description = description;
        this.type = type;
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

    @Override
    public String toString()
    {
        return "Replacement{" + "name=" + name + ", description=" + description + ", type=" + type + '}';
    }

}

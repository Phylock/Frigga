package dk.itu.frigga.action;


import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

/**
 * TemplateInfo Container Class contains info about a given Template.
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class TemplateInfo
{
    private final String author;
    private final String name;
    private final String site;
    private final String description;

    public TemplateInfo()
    {
        author = "Unknown";
        name = "Unnamed";
        site = "none";
        description = "No description";
    }

    public TemplateInfo(String author, String name, String site, String description)
    {
        this.author = author;
        this.name = name;
        this.site = site;
        this.description = description;
    }

    public TemplateInfo(final Element element)
    {
        String a = "Unknown";
        String n = "Unnamed";
        String s = "none";
        String d = "No description.";

        if (element.getTagName().equals("info"))
        {
            Element elemName = XmlHelper.getFirstChildElement(element, "name");
            Element elemAuthor = XmlHelper.getFirstChildElement(element, "author");
            Element elemSite = XmlHelper.getFirstChildElement(element, "site");
            Element elemDescription = XmlHelper.getFirstChildElement(element, "description");

            if (elemName != null) n = elemName.getTextContent();
            if (elemAuthor != null) a = elemAuthor.getTextContent();
            if (elemSite != null) s = elemSite.getTextContent();
            if (elemDescription != null) d = elemDescription.getTextContent();
        }

        author = a;
        name = n;
        site = s;
        description = d;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getSite()
    {
        return site;
    }

    @Override
    public String toString()
    {
        return "Template: " + name + " by " + author + "\nSite: " + site + "\nDescription: " + description;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateInfo that = (TemplateInfo) o;

        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!name.equals(that.name)) return false;
        if (site != null ? !site.equals(that.site) : that.site != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
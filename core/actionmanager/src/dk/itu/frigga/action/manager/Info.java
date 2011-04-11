package dk.itu.frigga.action.manager;

/**
 *
 * @author phylock
 */
public class Info {
  private String author;
  private String name;
  private String site;
  private String description;

  public Info(String author, String name, String site, String description) {
    this.author = author;
    this.name = name;
    this.site = site;
    this.description = description;
  }

  public String getAuthor() {
    return author;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public String getSite() {
    return site;
  }
}

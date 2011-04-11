package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.utility.StringHelper;
import java.util.List;

/**
 *
 * @author phylock
 */
public class Condition {

  public enum Type {

    And, Or
  }
  private List<Object> items;
  private Type type;

  public void addItem(Object item) {
    items.add(item);
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public String toString() {
    if(items.isEmpty())
      return "";

    String[] list = new String[items.size()];
    for(int i = 0; i < items.size(); i++)
    {
      list[i] = items.get(i).toString();
    }

    return " ( " + StringHelper.ImplodeString(list, parseType(type)) + " ) ";
  }

  private String parseType(Type type) {
    switch (type) {
      case And:
        return " and ";
      case Or:
        return " or ";
      default:
        return " ";
    }
  }
}

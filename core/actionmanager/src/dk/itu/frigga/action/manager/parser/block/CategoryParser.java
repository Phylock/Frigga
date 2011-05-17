/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.block.Category;
import dk.itu.frigga.action.block.Condition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class CategoryParser implements BlockParser {

  private static final String[] POSIBLE_CHILDREN = new String[]{"variable", "and", "or"};

  public Condition parse(Document doc, Element element) {
    String id = element.getAttribute("name");
    Category category = new Category(id);
    if (id.startsWith("!")) {
      category.addReplacement(id.substring(1));
    }
    return category;
  }

  public String[] childElementsTypes() {
    return POSIBLE_CHILDREN;
  }
}

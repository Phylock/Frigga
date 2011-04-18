/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.Replacement;
import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class ReplacementsParser implements Parseable {
  private static final String ELEMENT_REPLACE = "replace";

  public void parse(Template template, Document doc, Element element) {
    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_REPLACE);
    while (current != null) {
      String name = current.getAttribute("name");
      String description = current.getAttribute("description");
      String type = current.getAttribute("type");

      template.getReplacements().put(name, new Replacement(name, description, type));

      current = XmlHelper.getNextSiblingElement(current, ELEMENT_REPLACE);

    }
  }
}

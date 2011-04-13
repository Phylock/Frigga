/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class IncludeParser implements Parseable {
  private static final String ELEMENT_SCRIPT = "script";
  public void parse(Template template, Document doc, Element element) {

    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_SCRIPT);
    while (current != null) {
      String id = current.getAttribute("id");
      String path = current.getAttribute("path");

      template.getScripts().put(id, path);

      current = XmlHelper.getNextSiblingElement(current, ELEMENT_SCRIPT);
    }
  }
}
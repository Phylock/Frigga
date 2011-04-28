/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.Template;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public interface Parseable {
  void parse(Template template, Document doc, Element element);
}

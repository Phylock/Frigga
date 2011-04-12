/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.manager.block.Condition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public interface BlockParser {

  Condition parse(Document doc, Element element);

  String[] childElementsTypes();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.block.Condition;
import dk.itu.frigga.action.block.Script;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class ScriptParser implements BlockParser {

  private static final String[] POSIBLE_CHILDREN = null;

  public Condition parse(Document doc, Element element) {
    String call = element.getAttribute("call");
    String interval = element.getAttribute("interval");
    if (interval != null) {
      return new Script(call, Long.parseLong(interval));
    }
    String schedule = element.getAttribute("schedule");
    if (schedule != null) {
      return new Script(call, schedule);
    }
    return new Script(call);
  }

  public String[] childElementsTypes() {
    return POSIBLE_CHILDREN;
  }
}

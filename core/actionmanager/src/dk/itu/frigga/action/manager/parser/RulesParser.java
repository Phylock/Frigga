/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.RuleTemplate;
import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.utility.XmlHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class RulesParser implements Parseable {

  private static final String RULE_ELEMENT = "rules";
  private static final String ACTION_ELEMENT = "action";
  private static final String VARIABLE_ELEMENT = "variable";
  private static final String IS_EQUAL = "isEqual";
  private static final String IS_NOT_EQUAL = "isNotEqual";
  private static final String IS_GREATER = "isGreater";
  private static final String IS_LESS = "isLess";
  private static final String IS_BETWEEN = "isBetween";
  private static final String[] VARIABLE_CONDITIONS = new String[]{IS_EQUAL, IS_NOT_EQUAL,IS_GREATER,IS_LESS,  IS_BETWEEN};
  private static final Map<String, String> OPERATOR_LOOKUP;

  static {
    Map<String, String> list = new HashMap<String, String>();
    //fill
    list.put(IS_EQUAL, "=");
    list.put(IS_NOT_EQUAL, "<>");
    list.put(IS_GREATER, ">");
    list.put(IS_LESS, "<");
    list.put(IS_BETWEEN, "between");
    //write protection + speed
    OPERATOR_LOOKUP = Collections.unmodifiableMap(list);

  }

  private String parseVariable(Document doc, Element element) {
    String name = element.getAttribute("name");
    String type = element.getAttribute("type");
    Element evalue = XmlHelper.getFirstChildElement(element, VARIABLE_CONDITIONS);
    //TODO: if null throw error
    String tag = evalue.getTagName();
    String operator = OPERATOR_LOOKUP.get(tag);
    String value;
    if (tag.equals(IS_BETWEEN)) {
      String min = evalue.getAttribute("min");
      String max = evalue.getAttribute("max");
      if (type.equals("string")) {
        min = quotes(min);
        max = quotes(max);
      }
      value = String.format(" %s and %s ", min, max);
    } else {
      value = evalue.getTextContent();
      if (type.equals("string")) {
        value = quotes(value);
      }
    }
    return String.format(" %s %s %s ", name, operator, value);
  }

  private String parseDevice(Document doc, Element element, Map<String, String> replacements) {
    String id = element.getAttribute("id");
    String description = element.getAttribute("description");
    if (id.startsWith("!")) {
      replacements.put(id.substring(1), description);
    }
  }

  private Condition parseCondition(Document doc, Element element, Map<String, String> replacements) {
    Condition condition = new Condition();


  }

  private Map<String, String> parseActions(Document doc, Element element) {

    Map<String, String> actions = new HashMap<String, String>();
    Element current = XmlHelper.getFirstChildElement(element, ACTION_ELEMENT);
    while (current != null) {
      String type = current.getAttribute("type");
      String callback = current.getAttribute("callback");

      actions.put(type, callback);

      current = XmlHelper.getNextSiblingElement(current, ACTION_ELEMENT);

    }
    return actions;
  }

  private RuleTemplate parseRule(Document doc, Element element) {
    String id = element.getAttribute("id");
    String description = XmlHelper.getFirstChildElement(element, "description").getTextContent();
    Map<String, String> actions = parseActions(doc, XmlHelper.getFirstChildElement(element, "actions"));
    Map<String, String> replacements = new HashMap<String, String>();
    Condition condition = parseCondition(doc, XmlHelper.getFirstChildElement(element, "condition"), replacements);

    return new RuleTemplate(id, description, actions, condition);
  }

  public void parse(Template template, Document doc, Element element) {

    Element current = XmlHelper.getFirstChildElement(element, RULE_ELEMENT);
    while (current != null) {
      RuleTemplate rule = parseRule(doc, current);
      template.getRules().put(rule.getId(), rule);
      current = XmlHelper.getNextSiblingElement(current, RULE_ELEMENT);
    }
  }

  private static String quotes(String text) {
    return String.format("'%s'", text);
  }
}

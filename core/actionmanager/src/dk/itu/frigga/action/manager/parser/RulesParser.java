/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.Replacement;
import dk.itu.frigga.action.manager.RuleTemplate;
import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.action.manager.block.And;
import dk.itu.frigga.action.manager.block.BaseCondition;
import dk.itu.frigga.action.manager.block.Condition;
import dk.itu.frigga.action.manager.block.Or;
import dk.itu.frigga.action.manager.parser.block.BlockParser;
import dk.itu.frigga.action.manager.parser.block.ConditionParser;
import dk.itu.frigga.action.manager.parser.block.DeviceParser;
import dk.itu.frigga.action.manager.parser.block.VariableParser;
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

  private static final String ELEMENT_REPLACEMENT = "replacement";
  private static final String ELEMENT_RULE = "rule";
  private static final String ELEMENT_ACTION = "action";
  private static final String ELEMENT_VARIABLE = "variable";
  private static final String ELEMENT_DEVICE = "device";
  private static final Map<String, BlockParser> BLOCK_PARSERS;

  static {
    Map<String, BlockParser> parsers = new HashMap<String, BlockParser>();
    //fill
    parsers.put(ELEMENT_VARIABLE, new VariableParser());
    parsers.put(ELEMENT_DEVICE, new DeviceParser());
    parsers.put("and", new ConditionParser(And.class));
    parsers.put("or", new ConditionParser(Or.class));
    parsers.put("condition", new ConditionParser(BaseCondition.class));
    //write protection + speed
    BLOCK_PARSERS = Collections.unmodifiableMap(parsers);
  }

  private Condition parseCondition(Document doc, Element element) {
    String tag = element.getTagName();
    Condition condition = null;

    BlockParser parser = BLOCK_PARSERS.get(tag);
    if (parser != null) {
      //parse current
      condition = parser.parse(doc, element);

      //parse children
      String[] possible_children = parser.childElementsTypes();
      if (possible_children != null) {
        Element current = XmlHelper.getFirstChildElement(element, possible_children);
        while (current != null) {
          condition.addChild(parseCondition(doc, current));
          current = XmlHelper.getNextSiblingElement(current, possible_children);
        }
      }
    }
    return condition;
  }

  private Map<String, String> parseActions(Document doc, Element element) {

    Map<String, String> actions = new HashMap<String, String>();
    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_ACTION);
    while (current != null) {
      String type = current.getAttribute("type");
      String callback = current.getAttribute("callback");

      actions.put(type, callback);

      current = XmlHelper.getNextSiblingElement(current, ELEMENT_ACTION);

    }
    return actions;
  }

  private Map<String, Replacement> parseReplacements(Document doc, Element element) {

    Map<String, Replacement> replacements = new HashMap<String, Replacement>();
    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_REPLACEMENT);
    while (current != null) {
      String name = current.getAttribute("name");
      String description = current.getAttribute("description");
      String type = current.getAttribute("type");


      replacements.put(name, new Replacement(name, description, type));

      current = XmlHelper.getNextSiblingElement(current, ELEMENT_REPLACEMENT);

    }
    return replacements;
  }

  private RuleTemplate parseRule(Document doc, Element element) {
    String id = element.getAttribute("id");
    String description = XmlHelper.getFirstChildElement(element, "description").getTextContent();
    Map<String, String> actions = parseActions(doc, XmlHelper.getFirstChildElement(element, "actions"));
    Map<String, Replacement> replacements = parseReplacements(doc, XmlHelper.getFirstChildElement(element, "replacements"));
    Condition condition = parseCondition(doc, XmlHelper.getFirstChildElement(element, "condition"));

    return new RuleTemplate(id, description, actions, replacements, null, condition);
  }

  public void parse(Template template, Document doc, Element element) {

    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_RULE);
    while (current != null) {
      RuleTemplate rule = parseRule(doc, current);
      template.getRules().put(rule.getId(), rule);
      current = XmlHelper.getNextSiblingElement(current, ELEMENT_RULE);
    }
  }
}

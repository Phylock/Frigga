/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.Action;
import dk.itu.frigga.action.manager.FunctionAction;
import dk.itu.frigga.action.RuleTemplate;
import dk.itu.frigga.action.manager.ScriptAction;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.action.block.And;
import dk.itu.frigga.action.block.BaseCondition;
import dk.itu.frigga.action.block.Condition;
import dk.itu.frigga.action.block.Or;
import dk.itu.frigga.action.manager.parser.block.BlockParser;
import dk.itu.frigga.action.manager.parser.block.CategoryParser;
import dk.itu.frigga.action.manager.parser.block.ConditionParser;
import dk.itu.frigga.action.manager.parser.block.DeviceParser;
import dk.itu.frigga.action.manager.parser.block.ScriptParser;
import dk.itu.frigga.action.manager.parser.block.VariableParser;
import dk.itu.frigga.utility.XmlHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class RulesParser implements Parseable {

  private static final String ELEMENT_RULE = "rule";
  private static final String ELEMENT_ACTION = "action";
  private static final Map<String, BlockParser> BLOCK_PARSERS;

  static {
    Map<String, BlockParser> parsers = new HashMap<String, BlockParser>();
    //fill
    parsers.put("variable", new VariableParser());
    parsers.put("device", new DeviceParser());
    parsers.put("and", new ConditionParser(And.class));
    parsers.put("or", new ConditionParser(Or.class));
    parsers.put("condition", new ConditionParser(BaseCondition.class));
    parsers.put("script", new ScriptParser());
    parsers.put("category", new CategoryParser());

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

  private void parseActionType(Document doc, Element element, List<Action> actions) {
   final String[] ELEMENT_ACTION_TYPES = new String[]{"function", "script"};
    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_ACTION_TYPES);
    while (current != null) {
      String tag = current.getTagName();
      if (ELEMENT_ACTION_TYPES[0].equals(tag)) {
        List<String> params = new LinkedList<String>();
        String selection = current.getAttribute("selection");
        String function = current.getAttribute("function");
        Element param = XmlHelper.getFirstChildElement(current, "param");
        while (param != null) {
          params.add(param.getTextContent().trim());
          param = XmlHelper.getNextSiblingElement(param, ELEMENT_ACTION_TYPES);
        }
        if (params.isEmpty()) {
          actions.add(new FunctionAction(function, selection));
        } else {
          actions.add(new FunctionAction(function, selection, params.toArray(new String[params.size()])));
        }
      } else if (ELEMENT_ACTION_TYPES[1].equals(tag)) {
        String callback = current.getAttribute("callback");
        actions.add(new ScriptAction(callback));
      }
      current = XmlHelper.getNextSiblingElement(current, ELEMENT_ACTION_TYPES);
    }
  }

  private Map<String, List<Action>> parseActions(Document doc, Element element) {

    Map<String, List<Action>> actions = new HashMap<String, List<Action>>();
    Element current = XmlHelper.getFirstChildElement(element, ELEMENT_ACTION);
    while (current != null) {
      String event = current.getAttribute("event");

      List<Action> event_actions;
      if (actions.containsKey(event)) {
        event_actions = actions.get(event);
      } else {
        event_actions = new LinkedList<Action>();
        actions.put(event, event_actions);
      }

      parseActionType(doc, current, event_actions);
      current = XmlHelper.getNextSiblingElement(current, ELEMENT_ACTION);
    }
    return actions;
  }

  private RuleTemplate parseRule(Document doc, Element element) {
    String id = element.getAttribute("id");
    String description = XmlHelper.getFirstChildElement(element, "description").getTextContent();
    Map<String, List<Action>> actions = parseActions(doc, XmlHelper.getFirstChildElement(element, "actions"));
    Condition condition = parseCondition(doc, XmlHelper.getFirstChildElement(element, "condition"));

    return new RuleTemplate(id, description, condition, actions);
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

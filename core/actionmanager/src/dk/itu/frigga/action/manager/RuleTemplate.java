/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.parser.Condition;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author phylock
 */
public class RuleTemplate {

  private String id;
  private String description;
  private Map<String, String> callbacks;
  private Map<String, String> replacements;
  private List<ScriptRule> scriptrules;
  private String selection;

  public RuleTemplate(String id, String description, Map<String, String> callbacks, Condition condition) {
    this.id = id;
    this.description = description;
    this.callbacks = callbacks;
  }

  public Map<String, String> getReplacements() {
    return replacements;
  }

  public Rule createRule(Map<String, String> data) throws MissingReplacementData {
    if (replacements.size() == data.size() && replacements.keySet().containsAll(data.keySet())) {
      String build_rule = selection;
      for (Entry<String, String> entry : data.entrySet()) {
        String key = "!" + entry.getKey();
        build_rule = build_rule.replace(key, entry.getValue());
      }
      //TODO: fix null stuff :d
      return new Rule(build_rule, callbacks, null);
    } else {
      throw new MissingReplacementData("Missing data settings");
    }
  }

  public String getId() {
    return id;
  }
}

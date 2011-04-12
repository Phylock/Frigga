/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.block.Condition;
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
  private Map<String, Replacement> replacements;
  private List<ScriptRule> scriptrules;
  private Condition condition;

  public RuleTemplate(String id, String description, Map<String, String> callbacks, Map<String, Replacement> replacements, List<ScriptRule> scriptrules, Condition condition) {
    this.id = id;
    this.description = description;
    this.callbacks = callbacks;
    this.replacements = replacements;
    this.scriptrules = scriptrules;
    this.condition = condition;
  }

  public Map<String, Replacement> getReplacements() {
    return replacements;
  }

  public Rule createRule(Map<String, String> data) throws MissingReplacementData {
    if (replacements.size() == data.size() && replacements.keySet().containsAll(data.keySet())) {
      //TODO: traverse condition tree and create rule
      return null;
    } else {
      throw new MissingReplacementData("Missing data settings");
    }
  }

  public String getId() {
    return id;
  }

  public Condition getCondition() {
    return condition;
  }

  public String getDescription() {
    return description;
  }
}

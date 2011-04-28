/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.action.block.Condition;
import java.util.List;
import java.util.Map;


/**
 *
 * @author phylock
 */
public class RuleTemplate {
    /* Info */
  private String id;
  private String description;
  /* parsed */
  private Condition condition;
  private Map<String, List<Action>> actions;

  public RuleTemplate(String id, String description, Condition condition, Map<String, List<Action>> actions) {
    this.id = id;
    this.description = description;
    this.condition = condition;
    this.actions = actions;
  }

  public Map<String, List<Action>> getActions() {
    return actions;
  }

  public Condition getCondition() {
    return condition;
  }

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "RuleTemplate{" + "id=" + id + "description=" + description + '}';
  }
}

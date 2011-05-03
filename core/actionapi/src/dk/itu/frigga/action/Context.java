/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phylock
 */
public class Context {

  private final String id;
  private final Template template;
  private final Map<String, String> replacements;
  private final Map<String, Rule> rules;

  public Context(String id, Template template, Map<String, String> replacements) {
    this.id = id;
    this.template = template;
    this.replacements = replacements;
    this.rules = new HashMap<String, Rule>();
  }

  public Template getTemplate() {
    return template;
  }

  public Map<String, String> getReplacements() {
    return replacements;
  }

  public void addRule(Rule rule)
  {
    rules.put(rule.getID(), rule);
  }
}

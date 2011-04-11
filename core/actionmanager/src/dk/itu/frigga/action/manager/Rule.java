/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import java.util.Map;

/**
 *
 * @author phylock
 */
public class Rule {
  private String rule;
  private Map<String, String> scripts;
  private Map<String, String> callbacks;

  public Rule(String rule, Map<String, String> scripts, Map<String, String> callbacks) {
    this.rule = rule;
    this.scripts = scripts;
    this.callbacks = callbacks;
  }
}

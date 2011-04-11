package dk.itu.frigga.action.manager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phylock
 */
public class Template {

  private Info info;
  private Map<String, String> scripts;
  private Map<String, RuleTemplate> rules;

  public Template() {
    scripts = new HashMap<String, String>();
    rules = new HashMap<String, RuleTemplate>();
  }

  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public Map<String, RuleTemplate> getRules() {
    return rules;
  }

  public Map<String, String> getScripts() {
    return scripts;
  }
}

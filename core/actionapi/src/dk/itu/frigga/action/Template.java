package dk.itu.frigga.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Container class for a loaded template, this can be used to compile context
 * @author phylock
 */
public class Template {
  private Info info;
  private final Map<String, RuleTemplate> rules;
  private final Map<String, File> include;
  private final Map<String, Replacement> replacements;

  public Template() {
    rules = new HashMap<String, RuleTemplate>();
    include = new HashMap<String, File>();
    replacements = new HashMap<String, Replacement>();
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

  public Map<String, File> getInclude() {
    return include;
  }

  public Map<String, Replacement> getReplacements() {
    return replacements;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Template {\n ");
    sb.append(info.toString());
    sb.append("\n Include: \n");
    for(Entry<String, File> entry : include.entrySet())
    {
      sb.append("  ");
      sb.append(entry.getKey());
      sb.append(" = ");
      sb.append(entry.getValue().getAbsoluteFile());
      sb.append("\n");
    }

    sb.append("\n Replacements: \n");
    for(Replacement entry : replacements.values())
    {
      sb.append("  ");
      sb.append(entry.toString());
      sb.append("\n");
    }

    sb.append("\n Rules { \n");
    for(RuleTemplate entry : rules.values())
    {
      sb.append("  ");
      sb.append(entry.toString());
      sb.append("\n");
    }

    sb.append(" }\n}");

    return sb.toString();
  }
}

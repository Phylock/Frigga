/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.block;

import dk.itu.frigga.utility.IDGenerator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class BaseCondition implements Condition {
  protected List<Condition> children = null;
  protected List<String> replacements = null;
  private final long unique;
  private static final IDGenerator generator = new IDGenerator();
  public BaseCondition() {
    unique = generator.nextID();
  }

  public void traverse(Visitor visitor) {
    if (!(visitor.preVisit(this))) {
      return;
    }

    if (children != null && !children.isEmpty()) {
      for (Condition con : children) {
        con.traverse(visitor);
      }
    }

    visitor.postVisit(this);
  }

  public void addChild(Condition child) {
    if (children == null) {
      children = new LinkedList<Condition>();
    }
    children.add(child);
  }

  public void removeChild(Condition child) {
    if (children != null) {
      children.remove(child);
    }
  }

  public boolean hasReplacement()
  {
    return replacements != null && !replacements.isEmpty();
  }

  public List<String> getReplacements() {
    return replacements;
  }

  public void addReplacement(String replacement)
  {
    if(replacements == null)
    {
      replacements = new LinkedList<String>();
    }
    replacements.add(replacement);
  }

  public boolean hasChildren() {
    return children != null && !children.isEmpty();
  }

  public long getUniqueId() {
    return unique;
  }
}

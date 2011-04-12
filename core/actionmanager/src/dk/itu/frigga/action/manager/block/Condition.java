/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.block;

/**
 *
 * @author phylock
 */
public interface Condition extends Traversable {

  public void addChild(Condition child);

  public void removeChild(Condition child);

  public boolean hasReplacement();

  void addReplacement(String replacement);
}

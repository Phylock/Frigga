/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

/**
 *
 * @author phylock
 */
public class ScriptAction implements Action{
  private final String callback;

  public ScriptAction(String callback) {
    this.callback = callback;
  }
  
  public void execute() {
    //TODO: get context
  }
}

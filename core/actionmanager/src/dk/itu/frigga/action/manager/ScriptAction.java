/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.Action;

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

    @Override
    public String getEvent()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

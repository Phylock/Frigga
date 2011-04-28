/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.Action;
import dk.itu.frigga.utility.StringHelper;

/**
 *
 * @author phylock
 */
public class FunctionAction implements Action{
  private final String name;
  private final String selection;
  private final String[] params;

  public FunctionAction(String name, String selection) {
    this.name = name;
    this.selection = selection;
    params = null;
  }


  public FunctionAction(String name, String selection, String[] params) {
    this.name = name;
    this.selection = selection;
    this.params = params;
  }

  public void execute() {
    //TODO: access devicemanager and call function
    System.out.println(String.format("Call selection %s with function: %s (%s)", selection, name, StringHelper.implodeString(params, ", ")));
  }
}

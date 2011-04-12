/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.block.Condition;
import dk.itu.frigga.action.manager.block.Visitor;

/**
 *
 * @author phylock
 */
public class TreeVisitor implements Visitor{
  private int intent = 0;
  public boolean preVisit(Condition condition) {
    printIntent(intent, "-");
    System.out.println(condition.getClass().getSimpleName());
    intent ++;
    return true;
  }

  public void postVisit(Condition condition) {
    intent--;
  }

  private static void printIntent(int intent, String c)
  {
    for(int i = 0; i < intent; i++)
    {
      System.out.print(c);
    }
  }
}

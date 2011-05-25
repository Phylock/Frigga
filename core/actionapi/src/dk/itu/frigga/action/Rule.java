/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import java.util.List;

/**
 *
 * @author phylock
 */
public interface Rule {
  public enum State
  {
    Invalid, Valid
  }

  public String getID();
  public List<ConditionResult> check();
  
}

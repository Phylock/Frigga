/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

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

}

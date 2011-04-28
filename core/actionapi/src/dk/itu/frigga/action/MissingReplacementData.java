/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.FriggaException;

/**
 *
 * @author phylock
 */
public class MissingReplacementData extends FriggaException{

  public MissingReplacementData(String message) {
    super(message);
  }
}

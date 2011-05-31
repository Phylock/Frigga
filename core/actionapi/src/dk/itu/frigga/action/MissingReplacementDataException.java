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
public class MissingReplacementDataException extends FriggaException{

  public MissingReplacementDataException(String message) {
    super(message);
  }
}

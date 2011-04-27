/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga;

/**
 *
 * @author Tommy
 */
public abstract class FriggaException extends Exception {

  public FriggaException(Throwable cause) {
    super(cause);
  }

  public FriggaException(String message, Throwable cause) {
    super(message, cause);
  }

  public FriggaException(String message) {
    super(message);
  }

  public FriggaException() {
    super();
  }
}

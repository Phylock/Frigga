/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

import dk.itu.frigga.FriggaException;

/**
 *
 * @author phylock
 */
public class FriggaDeviceException extends FriggaException{
  public FriggaDeviceException(Throwable cause) {
    super(cause);
  }

  public FriggaDeviceException(String message, Throwable cause) {
    super(message, cause);
  }

  public FriggaDeviceException(String message) {
    super(message);
  }

  public FriggaDeviceException() {
    super();
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.proxy;

/**
 *
 * @author phylock
 */
public abstract class LazyLoadProxy<T> {

  private T obj = null;

  public synchronized T get() {
    if (obj == null) {
      obj = initialize();
    }
    return obj;
  }

  abstract T initialize();
}

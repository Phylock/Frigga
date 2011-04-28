package dk.itu.frigga.utility;

/**
 *
 * @author phylock
 */
public class IDGenerator {
  private long value = 0;

  public synchronized long nextID()
  {
    return ++value;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager.block;

/**
 *
 * @author phylock
 */
public class Device extends BaseCondition{
  private String id;

  public Device(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.block;

/**
 *
 * @author phylock
 */
public class Category extends BaseCondition{
  private final String category;

  public Category(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }
}

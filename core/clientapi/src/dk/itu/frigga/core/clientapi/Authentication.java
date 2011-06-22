/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.core.clientapi;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class Authentication {
  private final String answer;

  public Authentication(String answer) {
    this.answer = answer;
  }

  public String getAnswer() {
    return answer;
  }
}

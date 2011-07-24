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
  private final String username;
  private final String passstring;

  public Authentication(String answer, String username, String passstring) {
    this.answer = answer;
    this.username = username;
    this.passstring = passstring;
  }

  public String getAnswer() {
    return answer;
  }

  public String getPassstring() {
    return passstring;
  }

  public String getUsername() {
    return username;
  }
}

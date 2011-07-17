/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.core.clientapi;

import dk.itu.frigga.protocol.Selection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class RequestFunctionCall implements UserRequest {

  private List<Selection> selections;
  private String function;
  private Map<String, Object> params;

  public RequestFunctionCall(List<Selection> selections, String function, Map<String, Object> params) {
    this.selections = selections;
    this.function = function;
    this.params = params;
  }

  public String getFunction() {
    return function;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public List<Selection> getSelections() {
    return selections;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.block.Condition;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phylock
 */
public class Rule {
  /* Info */
  private String id;
  private String description;
  /* parsed */
  private Context context;
  private Condition condition;
  private Map<String, List<Action>> actions;
  /* internal */

}

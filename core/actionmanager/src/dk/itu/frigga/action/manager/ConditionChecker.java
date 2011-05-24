/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ConditionResult;

/**
 *
 * @author phylock
 */
public interface ConditionChecker {
  ConditionResult[] check();
}

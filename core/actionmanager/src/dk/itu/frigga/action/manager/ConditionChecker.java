/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ConditionResult;

import java.util.List;

/**
 *
 * @author phylock
 */
public interface ConditionChecker {
  List<ConditionResult> check();
}

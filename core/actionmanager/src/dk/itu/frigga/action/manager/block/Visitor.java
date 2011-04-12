/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager.block;

/**
 *
 * @author phylock
 */
public interface Visitor {
  	boolean preVisit(Condition condition);
	void postVisit(Condition condition);
}

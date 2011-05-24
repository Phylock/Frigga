/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.action.block.Device;

/**
 *
 * @author phylock
 */
public interface ConditionResult {
  Device getDevice(String sid) throws SidNotFoundException;
}

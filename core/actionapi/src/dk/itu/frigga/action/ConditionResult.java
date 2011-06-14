/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

/**
 * Store the result of a condition, this result is a collection of devices, each given a unique id.
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public interface ConditionResult
{
    public int getScopeIdCount();
    public String getScopeId(final int index) throws SidNotFoundException;
    public Device getDevice(final String scopeId) throws SidNotFoundException;
}

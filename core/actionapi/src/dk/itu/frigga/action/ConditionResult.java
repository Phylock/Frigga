/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.action.block.Device;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk),
 *         Tommy Andersen (toan@itu.dk)
 */
public interface ConditionResult
{
    int getSectionIdCount();
    String getSectionId(final int index) throws SidNotFoundException;
    Device getDevice(final String sectionId) throws SidNotFoundException;
}

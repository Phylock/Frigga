/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

/**
 *
 * @author Tommy
 */
public class DataGroupNotFoundException extends DataException
{
    private final String groupName;

    public DataGroupNotFoundException(final String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public String toString()
    {
        return "DataGroupNotFoundException: '" + groupName + "'";
    }
}

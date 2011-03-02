/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

/**
 *
 * @author Tommy
 */
public class UnknownDataDriverException extends DataException
{
    private final String dataDriver;

    public UnknownDataDriverException(final String dataDriver)
    {
        this.dataDriver = dataDriver;
    }

    @Override
    public String toString()
    {
        return "UnknownDataDriverException: '" + dataDriver + "'";
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga;

/**
 *
 * @author Tommy Andersen (toan@itu.dk)
 */
public abstract class Singleton
{
    @Override
    protected final Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }
}

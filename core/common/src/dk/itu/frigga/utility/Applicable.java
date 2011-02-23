/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.utility;

/**
 *
 * @author Tommy
 */
public interface Applicable<T>
{
    /**
     * Applies a given filter to an item in a collection. This function
     * determines whether the filter is fullfilled or not. The filter itself is
     * determined by the implementation of this interface. This item will match
     * the filter if the apply function returns true.
     *
     * @param input The object to test against the filter.
     *
     * @return Returns true if the input matches the filter.
     */
    public boolean apply(final T input);
}

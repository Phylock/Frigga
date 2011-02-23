/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.utility;

import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * @author Tommy
 */
public final class Filtering {
    public static <T> boolean isIn(final T[] array, final T element)
    {
        for (T elem : array)
        {
            if (elem == element) return true;
        }

        return false;
    }

    public static <T> Iterable<T> filter(Iterable<T> unfiltered, Applicable<? super T> predicate)
    {
        LinkedList<T> result = new LinkedList<T>();

        for (Iterator<T> iter = unfiltered.iterator(); iter.hasNext(); )
        {
            T obj = iter.next();

            if (predicate.apply(obj))
            {
                result.add(obj);
            }
        }

        return result;
    }
}

package dk.itu.frigga.action.filter;

import dk.itu.frigga.action.runtime.Selection;

import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-21
 */
public class Source
{
    private final List<Selection> data = new LinkedList<Selection>();


    public Source(List<Selection> selection)
    {
        data.addAll(selection);
    }

    public boolean hasData()
    {
        return data.size() > 0;
    }
}

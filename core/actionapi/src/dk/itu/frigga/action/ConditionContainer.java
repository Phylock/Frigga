package dk.itu.frigga.action;

import dk.itu.frigga.action.block.Condition;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-24
 */
public class ConditionContainer
{
    private final List<Condition> conditions = Collections.synchronizedList(new LinkedList<Condition>());

    public void parse(final Element element)
    {

    }
}

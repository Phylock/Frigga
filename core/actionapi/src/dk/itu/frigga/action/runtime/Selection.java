package dk.itu.frigga.action.runtime;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-07
 */
public abstract class Selection
{
    public enum Comparison { LESS_THAN, EQUAL_TO, GREATER_THAN, NOT_COMPARABLE}

    public abstract Comparison compare(final Selection other);
}

package dk.itu.frigga.protocol;

import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
interface ProtocolConstraint
{
    public boolean isSatisfied();
    public Element getChild() throws ConstraintFailedException;
}

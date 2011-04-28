package dk.itu.frigga.protocol;

import org.w3c.dom.Element;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
final class ConstraintFactory
{
    public static boolean isConstraint(final Element element)
    {
        return element.getNodeName().equals("constraint");
    }

    public static ProtocolConstraint construct(final Element element) throws ProtocolException
    {
        assert(element != null) : "Element can not be null.";

        if (element.getNodeName().equals("constraint"))
        {
            if (!element.hasAttribute("type")) throw new ProtocolSyntaxException("Type is required.");

            String type = element.getAttribute("type");
            if (type.equals("version"))
            {
                return new VersionConstraint(element);
            }
        }
        else
        {
            throw new UnexpectedElementException("constraint", element.getNodeName());
        }

        throw new ProtocolException("No suitable constraint types found.");
    }
}

package dk.itu.frigga.protocol;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
class Parser
{
    private static boolean isInArray(final Object[] haystack, final Object needle)
    {
        for (final Object hay : haystack)
        {
            if (hay.equals(needle)) return true;
        }

        return false;
    }

    private static String joinArray(final Object[] array, final String seperator)
    {
        final StringBuilder result = new StringBuilder();
        boolean hasFirst = false;

        for (Object obj : array)
        {
            if (hasFirst)
            {
                result.append(seperator);
            }

            result.append(obj.toString());
            hasFirst = true;
        }

        return result.toString();
    }

    public static void parse(final Element element, final String expected, final ParserListener listener) throws ProtocolException
    {
        parse(element, expected, listener, true);
    }

    public static void parse(final Element element, final String expected, final ParserListener listener, final boolean allowRootConstraint) throws ProtocolException
    {
        assert(element != null) : "Element can not be null.";
        assert(listener != null) : "Listener can not be null.";

        Element usedElement = element;
        boolean haveElement = true;

        if (allowRootConstraint && ConstraintFactory.isConstraint(usedElement))
        {
            try
            {
                usedElement = ConstraintFactory.construct(usedElement).getChild();
            }
            catch (ProtocolException e)
            {
                haveElement = false;
            }
        }

        if (haveElement)
        {
            if (!usedElement.getNodeName().equals(expected))
            {
                throw new UnexpectedElementException(expected, usedElement.getNodeName());
            }

            for (Node child = usedElement.getFirstChild(); child != null; child = child.getNextSibling())
            {
                if (child.getNodeType() == Node.ELEMENT_NODE)
                {
                    if (ConstraintFactory.isConstraint((Element) child))
                    {
                        try
                        {
                            Element childElement = ConstraintFactory.construct((Element) child).getChild();
                            final String[] acceptedElements = listener.acceptedElements(usedElement.getNodeName(), usedElement);
                            final String name = childElement.getNodeName();
                            if (isInArray(acceptedElements, name))
                            {
                                parse(childElement, name, listener, true);
                            }
                            else
                            {
                                throw new UnexpectedElementException(joinArray(acceptedElements, ", "), name);
                            }
                        }
                        catch (ProtocolSyntaxException e)
                        {
                            throw e;
                        }
                        catch (ProtocolException e)
                        {
                            // Ignore
                        }
                    }
                    else
                    {
                        final String name = child.getNodeName();
                        final String[] acceptedElements = listener.acceptedElements(usedElement.getNodeName(), usedElement);

                        if (isInArray(acceptedElements, name))
                        {
                            listener.elementFound(name, (Element)child);
                        }
                        else
                        {
                            throw new UnexpectedElementException(joinArray(acceptedElements, ", "), name);
                        }
                    }
                }
            }
        }
    }
}

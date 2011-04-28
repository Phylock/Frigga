package dk.itu.frigga.protocol;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-12
 */
class VersionConstraint implements ProtocolConstraint
{
    private final boolean hasMinProtocolVersion;
    private final boolean hasMaxProtocolVersion;
    private final int minProtocolVersion;
    private final int maxProtocolVersion;
    private final Element element;

    public VersionConstraint(final int minProtocolVersion, final int maxProtocolVersion, final Element element)
    {
        assert(element != null) : "Element can not be null.";
        assert(maxProtocolVersion > minProtocolVersion) : "Max protocol version must be larger than min protocol version.";
        assert(minProtocolVersion > 0 && maxProtocolVersion > 0) : "Protocol versions must be greater than zero.";

        this.minProtocolVersion = minProtocolVersion;
        this.maxProtocolVersion = maxProtocolVersion;
        this.hasMinProtocolVersion = true;
        this.hasMaxProtocolVersion = true;

        this.element = element;
    }

    public VersionConstraint(final int minProtocolVersion, final Element element)
    {
        assert(element != null) : "Element can not be null.";
        assert(minProtocolVersion > 0) : "Min. protocol versions must be greater than zero.";

        this.minProtocolVersion = minProtocolVersion;
        this.maxProtocolVersion = 0;
        this.hasMinProtocolVersion = true;
        this.hasMaxProtocolVersion = false;

        this.element = element;
    }

    public VersionConstraint(final int minProtocolVersion, final boolean hasMinProtocolVersion,
                             final int maxProtocolVersion, final boolean hasMaxProtocolVersion,
                             final Element element)
    {
        assert(element != null) : "Element can not be null.";
        assert((hasMinProtocolVersion && hasMaxProtocolVersion && maxProtocolVersion > minProtocolVersion) || !hasMinProtocolVersion || !hasMaxProtocolVersion) : "Max protocol version must be larger than min protocol version.";
        assert((hasMinProtocolVersion && minProtocolVersion > 0) || !hasMinProtocolVersion) : "Min. protocol versions must be greater than zero.";
        assert((hasMaxProtocolVersion && maxProtocolVersion > 0) || !hasMaxProtocolVersion) : "Max. protocol versions must be greater than zero.";

        this.minProtocolVersion = minProtocolVersion;
        this.maxProtocolVersion = maxProtocolVersion;
        this.hasMinProtocolVersion = hasMinProtocolVersion;
        this.hasMaxProtocolVersion = hasMaxProtocolVersion;

        this.element = element;
    }

    public VersionConstraint(final Element element) throws ProtocolSyntaxException
    {
        assert(element != null) : "Element can not be null.";

        hasMinProtocolVersion = element.hasAttribute("from");
        hasMaxProtocolVersion = element.hasAttribute("to");

        if (hasMinProtocolVersion) minProtocolVersion = Integer.parseInt(element.getAttribute("from"));
        else minProtocolVersion = 0;

        if (hasMaxProtocolVersion) maxProtocolVersion = Integer.parseInt(element.getAttribute("to"));
        else maxProtocolVersion = 0;

        if (hasMinProtocolVersion && hasMaxProtocolVersion && minProtocolVersion > maxProtocolVersion)
        {
            throw new ProtocolSyntaxException("To can not be greater than from");
        }

        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                this.element = (Element)child;
                return;
            }
        }

        throw new ProtocolSyntaxException("Constraint has not children.");
    }

    public boolean isSatisfied()
    {
        if (hasMinProtocolVersion && ProtocolInformation.VERSION < minProtocolVersion) return false;
        if (hasMaxProtocolVersion && ProtocolInformation.VERSION > maxProtocolVersion) return false;

        return true;
    }

    public Element getChild() throws ConstraintFailedException
    {
        if (isSatisfied()) return element;
        throw new ConstraintFailedException();
    }
}

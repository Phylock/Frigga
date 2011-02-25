/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.utility;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Tommy
 */
public final class XmlHelper
{
    public static Element getFirstElement(NodeList list)
    {
        if (list.getLength() == 0)
        {
            return null;
        }

        Node node = list.item(0);
        while (node.getNodeType() != Node.ELEMENT_NODE)
        {
            if (node.getNextSibling() == null)
            {
                return null;
            }
            node = node.getNextSibling();
        }
        return (Element) node;
    }

    public static Element getFirstChildElement(Element elem)
    {
        Node node = elem.getFirstChild();

        if (node != null)
        {
            while (node.getNodeType() != Node.ELEMENT_NODE)
            {
                node = node.getNextSibling();

                if (node == null) return null;
            }
        }

        return (Element)node;
    }

    public static Element getFirstChildElement(Element element, String name)
    {
        Element elem = getFirstChildElement(element);

        if (elem != null)
        {
            if (!elem.getNodeName().equals(name))
            {
                elem = getNextSiblingElement(elem, name);
            }
        }

        return elem;
    }

    public static Element getFirstChildElement(Element element, String[] names)
    {
        Element elem = getFirstChildElement(element);

        if (elem != null)
        {
            if (!Filtering.isIn(names, element.getNodeName()))
            {
                elem = getNextSiblingElement(elem, names);
            }
        }

        return elem;
    }

    public static Element getNextSiblingElement(Element element)
    {
        Node node = element.getNextSibling();

        if (node != null)
        {
            while (node.getNodeType() != Node.ELEMENT_NODE)
            {
                node = node.getNextSibling();

                if (node == null) return null;
            }
        }
        
        return (Element) node;
    }

    public static Element getNextSiblingElement(Element element, String name)
    {
        Element elem = getNextSiblingElement(element);

        if (elem != null)
        {
            while (!elem.getNodeName().equals(name))
            {
                elem = getNextSiblingElement(elem);

                if (elem == null) return null;
            }
        }

        return elem;
    }

    public static Element getNextSiblingElement(Element element, String[] names)
    {
        Element elem = getNextSiblingElement(element);

        if (elem != null)
        {
            while (!Filtering.isIn(names, element.getNodeName()))
            {
                elem = getNextSiblingElement(elem);

                if (elem == null) return null;
            }
        }

        return elem;
    }
}

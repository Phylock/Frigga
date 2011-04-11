package dk.itu.frigga.utility;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlHelper {

  /**
   * Returns the first Element node in a NodeList
   * @param list the list to search
   * @return the element or null if none is found
   */
  public static Element getFirstElement(NodeList list) {
    if (list.getLength() == 0) {
      return null;
    }

    Node node = list.item(0);
    while (node.getNodeType() != Node.ELEMENT_NODE) {
      if (node.getNextSibling() == null) {
        return null;
      }
      node = node.getNextSibling();
    }
    return (Element) node;
  }

  /**
   * Returns the first Child of the type Element
   * @param elem the elements to search
   * @return the element or null if none is found
   */
  public static Element getFirstChildElement(Element elem) {
    Node node = elem.getFirstChild();

    if (node != null) {
      while (node.getNodeType() != Node.ELEMENT_NODE) {
        node = node.getNextSibling();

        if (node == null) {
          return null;
        }
      }
    }

    return (Element) node;
  }

  /**
   * Returns the first child Element with a given name
   * @param element the elements to search
   * @param name the name to search for
   * @return the element or null if none is found
   */
  public static Element getFirstChildElement(Element element, String name) {
    Element elem = getFirstChildElement(element);

    if (elem != null) {
      if (!elem.getNodeName().equals(name)) {
        elem = getNextSiblingElement(elem, name);
      }
    }

    return elem;
  }

  /**
   * Returns the first child Element which equals one of the names in a list
   * @param element the elements to search
   * @param name the array of names to search for
   * @return the element or null if none is found
   */
  public static Element getFirstChildElement(Element element, String[] names) {
    Element elem = getFirstChildElement(element);

    if (elem != null) {
      if (!Filtering.isIn(names, element.getNodeName())) {
        elem = getNextSiblingElement(elem, names);
      }
    }

    return elem;
  }

  /**
   * Get the next element
   * @param element current element
   * @return the element or null if none is found
   */
  public static Element getNextSiblingElement(Element element) {
    Node node = element.getNextSibling();

    if (node != null) {
      while (node.getNodeType() != Node.ELEMENT_NODE) {
        node = node.getNextSibling();

        if (node == null) {
          return null;
        }
      }
    }

    return (Element) node;
  }

  /**
   * Get the next element with a given name
   * @param element the elements to search
   * @param name the array of names to search for
   * @return the element or null if none is found
   */
  public static Element getNextSiblingElement(Element element, String name) {
    Element elem = getNextSiblingElement(element);

    if (elem != null) {
      while (!elem.getNodeName().equals(name)) {
        elem = getNextSiblingElement(elem);

        if (elem == null) {
          return null;
        }
      }
    }

    return elem;
  }

  /**
   * Get the next element which equals one of the names in a list
   * @param element the elements to search
   * @param name the array of names to search for
   * @return the element or null if none is found
   */
  public static Element getNextSiblingElement(Element element, String[] names) {
    Element elem = getNextSiblingElement(element);

    if (elem != null) {
      while (!Filtering.isIn(names, element.getNodeName())) {
        elem = getNextSiblingElement(elem);

        if (elem == null) {
          return null;
        }
      }
    }

    return elem;
  }
}

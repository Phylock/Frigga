/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.Template;
import dk.itu.frigga.proxy.LazyLoadProxy;
import dk.itu.frigga.utility.XmlHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class TemplateParser {

  private DocumentBuilderFactory factory = null;
  private static final Map<String, Parseable> parsers;
  private static final String[] ELEMENTS = new String[]{"info", "replacements", "includes", "rules"};

  static {
    Map<String, Parseable> list = new HashMap<String, Parseable>();
    //fill
    list.put(ELEMENTS[0], new InfoParser());
    list.put(ELEMENTS[1], new ReplacementsParser());
    list.put(ELEMENTS[2], new IncludeParser());
    list.put(ELEMENTS[3], new RulesParser());
    //write protection + speed
    parsers = Collections.unmodifiableMap(list);

  }

  private DocumentBuilderFactory getFactory() {
    if (factory == null) {
      factory = DocumentBuilderFactory.newInstance();
    }
    return factory;
  }

  public Template parse(File file) throws SAXException, IOException, ParserConfigurationException {
    return parse(getFactory().newDocumentBuilder().parse(file));
  }

  public Template parse(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
    return parse(getFactory().newDocumentBuilder().parse(stream));
  }

  private Template parse(Document doc) throws SAXException, IOException {
    Template template = new Template();
    Element root = XmlHelper.getFirstChildElement((Element) doc.getDocumentElement(), "template");

    Element current = XmlHelper.getFirstChildElement(root, ELEMENTS);
    while (current != null) {
      String name = current.getNodeName();
      if (parsers.containsKey(name)) {
        parsers.get(name).parse(template, doc, current);
      }
      current = XmlHelper.getNextSiblingElement(current, ELEMENTS);
    }
    return template;
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.utility.XmlHelper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
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

  private DocumentBuilder builder;
  private static final Map<String, Parseable> parsers;
  private static final String[] ELEMENTS = new String[]{"info", "include", "rules"};

  static {
    Map<String, Parseable> list = new HashMap<String, Parseable>();
    //fill
    list.put(ELEMENTS[0], new InfoParser());
    list.put(ELEMENTS[1], new IncludeParser());
    list.put(ELEMENTS[2], new RulesParser());
    //write protection + speed
    parsers = Collections.unmodifiableMap(list);

  }

  public TemplateParser() throws ParserConfigurationException {
    builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }

  public Template parse(File file) throws SAXException, IOException {
    Template template = new Template();
    Document doc = builder.parse(file);
    Element root = (Element) doc.getDocumentElement();
    Element current = XmlHelper.getFirstChildElement(root, ELEMENTS);
    while (current != null) {
      String name = current.getNodeName();
      if (parsers.containsKey(name)) {
        parsers.get(name).parse(null, doc, current);
      }
      current = XmlHelper.getNextSiblingElement(current, ELEMENTS);
    }
    return template;
  }
}

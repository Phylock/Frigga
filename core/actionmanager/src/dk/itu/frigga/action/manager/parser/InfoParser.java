/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.TemplateInfo;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class InfoParser implements Parseable{
  private static final String[] ELEMENTS = new String[]{"author","name","site","description"};

  public void parse(Template template, Document doc, Element element) {
    String author = "";
    String name = "";
    String site = "";
    String description = "";

    Element current = XmlHelper.getFirstChildElement(element, ELEMENTS);
    while(current != null)
    {
      String ename = current.getNodeName();
      String evalue = current.getTextContent();
      if(ename.equals(ELEMENTS[0]))
      {
        author = evalue;
      }else if(ename.equals(ELEMENTS[1]))
      {
        name = evalue;
      }else if(ename.equals(ELEMENTS[2]))
      {
        site = evalue;
      }else if(ename.equals(ELEMENTS[3]))
      {
        description = evalue;
      }
      current = XmlHelper.getNextSiblingElement(current, ELEMENTS);
    }
    template.setTemplateInfo(new TemplateInfo(author, name, site, description));
  }
}

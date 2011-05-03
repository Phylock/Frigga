/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public interface ActionManager {

  public static final String ACTION_TEMPLATE_KEY = "frigga.action.template";
  public static final String ACTION_ID_KEY = "frigga.action.id";

  void CompileTemplate(File action) throws FileNotFoundException, IOException;
  void CompileTemplate(String id, Template template, Map<String, String> replace);

  /**
   * Parse a template file and add it to its list of known templates
   * @param file the file to parse
   * @return the id of the loaded template
   * @throws IOException
   * @throws SAXException
   */
  String LoadTemplate(File file) throws IOException, SAXException;

  Context getContext(String id);

  Collection<Template> getTemplates();

  Template getTemplate(String id);

  boolean hasTemplate(String id);

  void removeTemplate(String id);
}

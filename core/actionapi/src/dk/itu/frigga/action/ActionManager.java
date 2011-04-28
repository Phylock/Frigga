/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action;

import dk.itu.frigga.action.Context;
import dk.itu.frigga.action.Template;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public interface ActionManager {

  void CompileTemplate(Template template, Map<String, String> replace);

  void LoadTemplate(File file) throws IOException, SAXException;

  Context getContext(String id);

  Template getTemplate(String id);

}

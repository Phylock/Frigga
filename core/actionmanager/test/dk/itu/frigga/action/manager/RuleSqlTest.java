/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.parser.TemplateParser;
import dk.itu.frigga.action.Context;
import dk.itu.frigga.action.Rule;
import dk.itu.frigga.action.Template;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class RuleSqlTest {

    public RuleSqlTest() {
    }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

  /**
   * Test of check method, of class RuleSql.
   */
  @Test
  public void testCheck() throws Exception{
    final String file = "../../../../notes/template_basic.xml";
    System.out.println("testParse - file: " + file);

    Map<String, String> r = new HashMap<String, String>();
    r.put("tv", "tv1");
    
    Template t = parseFile(file);
    Context c = new Context(t, r);
    RuleSql instance = new RuleSql(c, t.getRules().get("rule1"));
    Rule.State s = instance.check();
    
  }
  private Template parseFile(String file) throws ParserConfigurationException, SAXException, IOException {
    File f = new File(file);
    TemplateParser instance = new TemplateParser();
    return instance.parse(f);
  }
}
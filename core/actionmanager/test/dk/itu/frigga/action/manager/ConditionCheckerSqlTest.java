/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.parser.TemplateParser;
import dk.itu.frigga.action.Template;
import java.io.File;
import java.io.IOException;
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
public class ConditionCheckerSqlTest {

    public ConditionCheckerSqlTest() {
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

  @Test
  public void testConditionCheckerBasic() throws Exception{
    final String file = "../../../../notes/template_basic.xml";
    System.out.println("testParse - file: " + file);

    Template result = parseFile(file);
    //result.getRules().get("rule1").getCondition().traverse(new PrintConditionTree());
    ConditionCheckerSql cc = new ConditionCheckerSql(result.getRules().get("rule1").getCondition());
  }

  @Test
  public void testConditionCheckerScript() throws Exception{
    final String file = "../../../../notes/template_script.xml";
    System.out.println("testParse - file: " + file);

    Template result = parseFile(file);
    //result.getRules().get("rule1").getCondition().traverse(new PrintConditionTree());
    ConditionCheckerSql cc = new ConditionCheckerSql(result.getRules().get("rule1").getCondition());
  }
  private Template parseFile(String file) throws ParserConfigurationException, SAXException, IOException {
    File f = new File(file);
    TemplateParser instance = new TemplateParser();
    return instance.parse(f);
  }

}
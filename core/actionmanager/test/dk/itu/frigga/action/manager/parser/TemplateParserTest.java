/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.PrintConditionTree;
import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.action.manager.block.Condition;
import dk.itu.frigga.action.manager.block.Visitor;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class TemplateParserTest {

  public TemplateParserTest() {
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
  public void tearDown() throws IOException {
  }

  /**
   * Test of parse method, of class TemplateParser.
   */
  @Test
  public void testParseBasic() throws Exception {
    final String file = "../../../../notes/template_basic.xml";
    System.out.println("testParse - file: " + file);

    Template result = parseFile(file);
    System.out.println(result);
    //print condition structure
    result.getRules().get("rule1").getCondition().traverse(new PrintConditionTree());

    ConditionVerification cv = new ConditionVerification();
    result.getRules().get("rule1").getCondition().traverse(cv);
    assertTrue(cv.validate());

  }

    /**
   * Test of parse method, of class TemplateParser.
   */
  @Test
  public void testParseScript() throws Exception {
    final String file = "../../../../notes/template_script.xml";
    System.out.println("testParse - file: " + file);

    Template result = parseFile(file);

    System.out.println(result);
    //print condition structure
    result.getRules().get("rule1").getCondition().traverse(new PrintConditionTree());

    ConditionVerification cv = new ConditionVerification();
    result.getRules().get("rule1").getCondition().traverse(cv);
    assertTrue(cv.validate());

  }

  private Template parseFile(String file) throws ParserConfigurationException, SAXException, IOException {
    File f = new File(file);
    TemplateParser instance = new TemplateParser();
    return instance.parse(f);
  }

  private class ConditionVerification implements Visitor {

    private int level = 0;
    private boolean validate = true;

    public boolean preVisit(Condition condition) {
      level++;
      //TODO: validate :d
      return true;
    }

    public void postVisit(Condition condition) {
      level--;
    }

    public boolean validate() {
      return level == 0 && validate;
    }
  }
}

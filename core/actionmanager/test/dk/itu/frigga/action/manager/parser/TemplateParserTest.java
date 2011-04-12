/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser;

import dk.itu.frigga.action.manager.Template;
import dk.itu.frigga.action.manager.block.Condition;
import dk.itu.frigga.action.manager.block.Visitor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phylock
 */
public class TemplateParserTest {

  public TemplateParserTest() {
  }
  private InputStream is = null;

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    is = TemplateParserTest.class.getResourceAsStream("action.xml");
  }

  @After
  public void tearDown() throws IOException {
    is.close();
    is = null;
  }

  /**
   * Test of parse method, of class TemplateParser.
   */
  @Test
  public void testParse() throws Exception {
    System.out.println("parse");
    TemplateParser instance = new TemplateParser();
    Template result = instance.parse(is);

    //print condition structure
    result.getRules().get("").getCondition().traverse(new TreeVisitor());

    ConditionVerification cv = new ConditionVerification();
    result.getRules().get("").getCondition().traverse(cv);
    assertTrue(cv.validate());

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

package dk.itu.frigga.action;

import dk.itu.frigga.action.runtime.TemplateVariable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-02
 */
public class VariableContainerTest
{
    private final static int ADD_VARIABLE_COUNT = 1000;
    private VariableContainer variableContainer;

    @Before
    public void setUp() throws Exception
    {
        variableContainer = new VariableContainer();

        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            TemplateVariable variable = new TemplateVariable("Test" + i);

            int choose = (int)(Math.random() * 3.0);
            if (i < 301) choose = 0;
            else if (i < 601) choose = 1;
            else if (i < 901) choose = 2;

            if (choose == 0)
            {
                variable.set("String #" + i);
            }
            else if (choose == 1)
            {
                int d = ((i - 300) % 28) + 1;
                int m = ((i - 300) % 12);

                Calendar calendar = Calendar.getInstance();
                calendar.set(2011, m, d);
                variable.set(calendar.getTime());
            }
            else if (choose == 2)
            {
                double d = ((double)i) / 10.0;
                variable.set(d);
            }

            variableContainer.addVariable(variable);
        }
    }

    @After
    public void tearDown() throws Exception
    {
        variableContainer = null;
    }

    @Test
    public void testAddVariable() throws Exception
    {
        TemplateVariable original = new TemplateVariable("Test");
        original.set("Test value");
        variableContainer.addVariable(original);
        TemplateVariable found = variableContainer.getVariable("Test");
        assertEquals("Variable name mismatch", "Test", found.getName());
        assertEquals("Variable value mismatch", "Test value", found.getValue().asString());
        assertEquals("Variable type mismatch", "string", found.getValue().getType());

        TemplateVariable replacement = new TemplateVariable("Test");
        replacement.set(5.5);
        variableContainer.addVariable(replacement);
        TemplateVariable found2 = variableContainer.getVariable("Test");
        assertEquals("Variable name mismatch", "Test", found2.getName());
        assertEquals("Variable value mismatch", 5.5, found2.getValue().asNumber(), 0.0);
        assertEquals("Variable type mismatch", "number", found2.getValue().getType());
    }

    @Test
    public void testGetVariable() throws Exception
    {
        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            TemplateVariable variable = variableContainer.getVariable("Test" + i);
            String type = variable.getValue().getType();
            int choice = 0;

            if (i < 301) choice = 0;
            else if (i < 601) choice = 1;
            else if (i < 901) choice = 2;
            else
            {
                if (type.equals("string")) choice = 0;
                else if (type.equals("date")) choice = 1;
                else if (type.equals("number")) choice = 2;
                else fail("Wrong type");
            }

            assertEquals("Variable name mismatch", "Test" + i, variable.getName());
            if (choice == 0)
            {
                assertEquals("Variable value mismatch", "String #" + i, variable.getValue().asString());
                assertEquals("Variable type mismatch", "string", variable.getValue().getType());
            }
            else if (choice == 1)
            {
                int d = ((i - 300) % 28) + 1;
                int m = ((i - 300) % 12);

                Calendar calendar = Calendar.getInstance();
                Calendar varCal = Calendar.getInstance();
                calendar.set(2011, m, d);
                varCal.setTime(variable.getValue().asDate());

                assertEquals("Year mismatch", 2011, varCal.get(Calendar.YEAR));
                assertEquals("Month mismatch", m, varCal.get(Calendar.MONTH));
                assertEquals("Day mismatch", d, varCal.get(Calendar.DAY_OF_MONTH));

                assertEquals("Variable type mismatch", "date", variable.getValue().getType());
            }
            else
            {
                double d = ((double)i) / 10.0;

                assertEquals("Variable value mismatch", d, variable.getValue().asNumber(), 0.0);
                assertEquals("Variable type mismatch", "number", variable.getValue().getType());
            }
        }
    }

    @Test
    public void testRemoveVariable() throws Exception
    {
        Set<String> variableNamesBefore = variableContainer.getVariableNames();

        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            assertTrue("Missing pre required variable name", variableNamesBefore.contains("Test" + i));
        }

        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            if ((i & 1) == 1)
            {
                variableContainer.removeVariable("Test" + i);
                assertNull("Variable returned should be null", variableContainer.getVariable("Test" + i));
            }
        }

        Set<String> variableNamesAfter = variableContainer.getVariableNames();

        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            boolean variableExist = variableNamesAfter.contains("Test" + i);

            if ((i & 1) == 1)
            {
                assertFalse("Removed variable still exists", variableExist);
            }
            else
            {
                assertTrue("Kept variable removed", variableExist);
            }
        }
    }

    @Test
    public void testGetVariableNames() throws Exception
    {
        Set<String> variableNames1 = variableContainer.getVariableNames();

        for (int i = 1; i < (ADD_VARIABLE_COUNT + 1); i++)
        {
            assertTrue("Missing pre required variable name", variableNames1.contains("Test" + i));
        }

        for (String variableName : variableNames1)
        {
            assertNotNull("Given variable does not exist", variableContainer.getVariable(variableName));
        }
    }

    @Test
    public void testGetVariables() throws Exception
    {
        Collection<TemplateVariable> variables = variableContainer.getVariables();

        for (TemplateVariable variable : variables)
        {
            assertNotNull("Returned variable not found.", variableContainer.getVariable(variable.getName()));
            assertTrue("Variable does not have correct name", variable.getName().substring(0, 4).equals("Test"));
        }
    }

    @Test
    public void testParse() throws Exception
    {
        String xml = "<variables>\n" +
                "<variable name=\"$client\" source=\"tvselection.client\" />\n" +
                "<variable name=\"$client_category\" source=\"tvselection.client.category\" />\n" +
                "</variables>";

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);

        variableContainer.parse(document.getDocumentElement());
    }
}

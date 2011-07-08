package dk.itu.frigga.action;

import dk.itu.frigga.action.impl.ReplacementContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-02
 */
public class ReplacementContainerTest
{
    private final static int ADD_REPLACEMENT_COUNT = 1000;
    private ReplacementContainer replacementContainer;

    @Before
    public void runBeforeEveryTest()
    {
        replacementContainer = new ReplacementContainer();

        for (int i = 1; i < (ADD_REPLACEMENT_COUNT + 1); i++)
        {
            Replacement replacement = new Replacement("Test " + i, "Description of test " + i, "string");
            replacementContainer.addReplacement(replacement);
        }
    }

    @After
    public void runAfterEveryTest()
    {
        replacementContainer = null;
    }

    @Test
    public void testAddReplacement() throws Exception
    {
        int selection = (int)(Math.random() * ADD_REPLACEMENT_COUNT) + 1;
        Replacement replacement = replacementContainer.getReplacement("Test " + selection);
        assertTrue("Retrieved illegal selection", replacement.getDescription().equals("Description of test " + selection));
    }

    @Test
    public void testRemoveReplacement() throws Exception
    {
        int selection = (int)(Math.random() * ADD_REPLACEMENT_COUNT) + 1;
        replacementContainer.removeReplacement(new Replacement("Test " + selection, "Different description of test " + selection, "string"));
        Set<String> replacementNames = replacementContainer.getReplacementNames();

        for (int i = 1; i < (ADD_REPLACEMENT_COUNT + 1); i++)
        {
            boolean hasTestI = replacementNames.contains("Test " + i);

            if (i == selection) assertFalse("Contains replacement, that should have been removed", hasTestI);
            else assertTrue("Does not contain replacement, that should have been there", hasTestI);
        }

        assertNull("None existing replacement returned object reference instead of null.", replacementContainer.getReplacement("Test " + selection));
    }

    @Test
    public void testGetReplacement() throws Exception
    {
        Set<String> replacementNames = replacementContainer.getReplacementNames();

        for (String replacementName : replacementNames)
        {
            Replacement replacement = replacementContainer.getReplacement(replacementName);
            assertNotNull("Replacement that should exist in replacement container was null", replacement);
            assertEquals("The replacement returned was not the replacement requested", replacement.getName(), replacementName);
        }
    }

    @Test
    public void testHasReplacement() throws Exception
    {
        for (int i = 1; i < (ADD_REPLACEMENT_COUNT + 1); i++)
        {
            assertTrue("Does not contain replacement, that should have been there", replacementContainer.hasReplacement("Test " + i));
        }

        for (int i = (ADD_REPLACEMENT_COUNT + 2); i < (ADD_REPLACEMENT_COUNT + 2000); i++)
        {
            assertFalse("Contains replacement, that should not be there", replacementContainer.hasReplacement("Test " + i));
        }
    }

    @Test
    public void testGetReplacementNames() throws Exception
    {
        Set<String> replacementNames = replacementContainer.getReplacementNames();
        for (int i = 1; i < (ADD_REPLACEMENT_COUNT + 1); i++)
        {
            assertTrue("Does not contain replacement, that should have been there", replacementNames.contains("Test " + i));
        }
    }

    @Test
    public void testGetReplacements() throws Exception
    {
        Collection<Replacement> replacements = replacementContainer.getReplacements();
        assertEquals("Wrong number of replacements.", ADD_REPLACEMENT_COUNT, replacements.size());
    }

    /*@Test
    public void testPrepare() throws Exception
    {
        String source = "Insert replacement here: {Test 1} and here {Test 2} and here {Test 3} ignore trailing '{' character and insert here: {Test 4} instead.";
        String expected = "Insert replacement here: 1 and here 2 and here 3 ignore trailing '{' character and insert here: 4 instead.";
        String full = "";
        String fullExpected = "";

        for (int i = 1; i < (ADD_REPLACEMENT_COUNT + 1); i++)
        {
            if (i > 1)
            {
                full = full + " ";
                fullExpected = fullExpected + " ";
            }
            full = full + "{Test " + i + "}";
            fullExpected = fullExpected + i;
        }

        //assertEquals("Replacements not replaced properly", expected, replacementContainer.prepare(source));
        //assertEquals("Replacements missed", fullExpected, replacementContainer.prepare(full));

    } */

    @Test
    public void testParse() throws Exception
    {
        String xml = "<replacements>\n" +
                "<replace name=\"tv\" description=\"a tv\" type=\"string\"/>\n" +
                "<replace name=\"eyetrack\" description=\"an eyetracker to control\" type=\"string\"/>\n" +
                "</replacements>";

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);

        replacementContainer.removeAllReplacements();
        replacementContainer.parse(document.getDocumentElement());

        assertEquals("Wrong number of replacements loaded", 2, replacementContainer.getReplacementNames().size());
        assertTrue("Unable to find replacement: tv", replacementContainer.hasReplacement("tv"));
        assertTrue("Unable to find replacement: eyetrack", replacementContainer.hasReplacement("eyetrack"));
    }
}

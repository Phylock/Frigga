package dk.itu.frigga.action;

import dk.itu.frigga.action.impl.ConditionContainer;
import dk.itu.frigga.action.impl.filter.DefaultFilterFactory;
import dk.itu.frigga.action.impl.filter.filters.*;
import dk.itu.frigga.action.impl.filter.FilterFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class ConditionContainerTest
{
    private ConditionContainer conditionContainer;
    private FilterFactory filterFactory;

    @Before
    public void setUp() throws Exception
    {
        filterFactory = new DefaultFilterFactory();
        filterFactory.registerFilterType("whereEqual", EmptyFilter.class);
        filterFactory.registerFilterType("variable", EmptyFilter.class);
        filterFactory.registerFilterType("location", EmptyFilter.class);
        filterFactory.registerFilterType("isCategory", IsCategoryFilter.class);
        filterFactory.registerFilterType("hasVariable", HasVariableFilter.class);
        filterFactory.registerFilterType("constant", EmptyFilter.class);
        filterFactory.registerFilterType("and", AndFilter.class);
        filterFactory.registerFilterType("or", OrFilter.class);

        conditionContainer = new ConditionContainer();
    }

    @After
    public void tearDown() throws Exception
    {
        conditionContainer = null;
    }

    @Test
    public void testParse() throws Exception
    {
        final String xml = "<condition id=\"tvselection\">\n" +
                "                    <whereEqual>\n" +
                "                        <variable name=\"symbolic\" type=\"string\" sid=\"devicetv\">\n" +
                "                            <or>\n" +
                "                                <constraint minVersion=\"1\">\n" +
                "                                    <location type=\"local\" where=\"work_room1\"/>  <!-- Filter -->\n" +
                "                                    <location type=\"local\" where=\"work_room2\" position=\"10,10,10\" radius=\"1m\"/>  <!-- Filter -->\n" +
                "                                </constraint>\n" +
                "                                <location type=\"global\" position=\"10,10,10\" radius=\"1m\"/>  <!-- Filter -->\n" +
                "                            </or>\n" +
                "                            <isCategory category=\"tv\"/>  <!-- Filter -->\n" +
                "                            <hasVariable name=\"symbolic\" type=\"string\"/>  <!-- Filter -->\n" +
                "                        </variable>\n" +
                "                        <variable name=\"lookat\" type=\"string\" sid=\"deviceeyetrack\">\n" +
                "                            <isCategory category=\"eyetracker\"/>  <!-- Filter -->\n" +
                "                            <hasVariable name=\"lookat\" type=\"string\"/>  <!-- Filter -->\n" +
                "                        </variable>\n" +
                "                        <constant name=\"name_of_tv\" value=\"flatpanel_livingroom\" />\n" +
                "                    </whereEqual>\n" +
                "                </condition>";

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);

        conditionContainer.parse(document.getDocumentElement(), filterFactory);
    }

    @Test
    public void testAddFilter() throws Exception
    {

    }

    @Test
    public void testRemoveFilter() throws Exception
    {

    }

    @Test
    public void testHasFilter() throws Exception
    {

    }

    @Test
    public void testGetFilters() throws Exception
    {

    }

    @Test
    public void testRegisterNamedFilter() throws Exception
    {

    }

    @Test
    public void testUnregisterNamedFilter() throws Exception
    {

    }

    @Test
    public void testGetNamedFilter() throws Exception
    {

    }
}

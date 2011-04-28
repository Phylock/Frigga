/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.block.Condition;
import dk.itu.frigga.action.block.Variable;
import dk.itu.frigga.utility.XmlHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author phylock
 */
public class VariableParser implements BlockParser {

  private static final String IS_EQUAL = "isEqual";
  private static final String IS_NOT_EQUAL = "isNotEqual";
  private static final String IS_GREATER = "isGreater";
  private static final String IS_LESS = "isLess";
  private static final String IS_BETWEEN = "isBetween";
  private static final String[] VARIABLE_CONDITIONS = new String[]{IS_EQUAL, IS_NOT_EQUAL, IS_GREATER, IS_LESS, IS_BETWEEN};

  private static final Map<String, Variable.Type> TYPE_LOOKUP;
  private static final Map<String, Variable.Comparison> COMPARISON_LOOKUP;

  static {
    Map<String, Variable.Type> type = new HashMap<String, Variable.Type>();
    //fill
    type.put("string", Variable.Type.String);
    type.put("numeric", Variable.Type.Numeric);
    type.put("date", Variable.Type.Date);
    //write protection + speed
    TYPE_LOOKUP = Collections.unmodifiableMap(type);

    Map<String, Variable.Comparison> comparison = new HashMap<String, Variable.Comparison>();
    //fill
    comparison.put(IS_EQUAL, Variable.Comparison.IsEqual);
    comparison.put(IS_NOT_EQUAL, Variable.Comparison.IsNotEqual);
    comparison.put(IS_GREATER, Variable.Comparison.IsGreater);
    comparison.put(IS_LESS, Variable.Comparison.IsLess);
    comparison.put(IS_BETWEEN, Variable.Comparison.IsBetween);
    //write protection + speed
    COMPARISON_LOOKUP = Collections.unmodifiableMap(comparison);
  }

  public Condition parse(Document doc, Element element) {
    String name = element.getAttribute("name");
    Variable.Type type = TYPE_LOOKUP.get(element.getAttribute("type"));
    Element evalue = XmlHelper.getFirstChildElement(element, VARIABLE_CONDITIONS);
    //TODO: if null throw error
    Variable.Comparison comparison = COMPARISON_LOOKUP.get(evalue.getTagName());

    if (comparison.equals(Variable.Comparison.IsBetween)) {
      String min = evalue.getAttribute("min");
      String max = evalue.getAttribute("max");
      return new Variable(comparison, type, name, min, max);
    } else {
      String value = evalue.getTextContent();
      return new Variable(comparison, type, name, value);
    }
  }

  public String[] childElementsTypes() {
    return null;
  }
}

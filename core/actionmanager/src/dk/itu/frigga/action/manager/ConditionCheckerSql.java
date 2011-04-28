/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.block.And;
import dk.itu.frigga.action.block.Condition;
import dk.itu.frigga.action.block.Device;
import dk.itu.frigga.action.block.Or;
import dk.itu.frigga.action.block.Script;
import dk.itu.frigga.action.block.Variable;
import dk.itu.frigga.action.block.Visitor;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phylock
 */
public class ConditionCheckerSql {

  private String selection;

  public ConditionCheckerSql(Condition condition) {
    ConditionVisitor v = new ConditionVisitor();
    condition.traverse(v);
    selection = v.getSelection();
  }

  public String getSelection() {
    return selection;
  }

  private class ConditionVisitor implements Visitor {

    private StringBuilder sb = new StringBuilder();
    private List<String> scripts = new LinkedList<String>();
    private String current_operator = "AND";
    private String current_device = "";
    private int intent = 0;
    private boolean opened = false;

    public boolean preVisit(Condition condition) {
      intent++;
      if (condition.hasChildren()) {
        sb.append("(");
        opened = true;
      }

      if (condition instanceof And) {
        current_operator = "AND";
      } else if (condition instanceof Or) {
        current_operator = "OR";
      } else if (condition instanceof Device) {
        current_device = ((Device) condition).getId();
      } else if (condition instanceof Variable) {
        addOperator();
        parseVariable((Variable) condition);
        opened = false;
      } else if (condition instanceof Script) {
        addOperator();
        sb.append(String.format("$%d$", condition.getUniqueId()));
        opened = false;
      }
      return true;
    }

    private void addOperator() {
      if (!opened) {
        sb.append(String.format(" %s ", current_operator));
      }
    }

    private void parseVariable(Variable v) {
      String comparison = "";
      switch(v.getComparison())
      {
        case IsEqual:
         comparison = " = " + parseValueType(v.getValue(), v.getType());
         break;
        case IsGreater:
          comparison = " < " + parseValueType(v.getValue(), v.getType());
          break;
        case IsLess:
          comparison = " > " + parseValueType(v.getValue(), v.getType());
          break;
        case IsNotEqual:
          comparison = " != " + parseValueType(v.getValue(), v.getType());
          break;
        case IsBetween:
          comparison = String.format("BETWEEN %s AND %s", parseValueType(v.getValue(), v.getType()),parseValueType(v.getOptional_value(), v.getType()));
          break;
      }

      sb.append(String.format("(SELECT count(*) FROM device_variable as dv, variabletype as vt, device as d where vt.varname = '%s' and vt.id = dv.variable_id AND d.symbolic = '%s' and dv.device_id = d.id AND dv.variable_value %s )", v.getName(), current_device, comparison));
    }

    private String parseValueType(String value, Variable.Type type)
    {
      if(type.equals(Variable.Type.String))
      {
        return String.format("'%s'", value);
      }
      else
      {
        return value;
      }
    }

    public void postVisit(Condition condition) {
      intent--;
      if (condition.hasChildren()) {
        sb.append(")");
      }
    }



    public String getSelection() {
      return sb.toString();
    }

    public String[] getScripts() {
      return scripts.toArray(new String[scripts.size()]);
    }
  }
}

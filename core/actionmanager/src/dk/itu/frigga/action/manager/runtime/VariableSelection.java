package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.runtime.Selection;
import dk.itu.frigga.device.model.Device;

import java.util.Date;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-07
 */
public class VariableSelection extends DeviceSelection
{
    public enum VariableType { TYPE_STRING, TYPE_NUMERIC, TYPE_DATE }

    private final String variableName;
    private final Object variableValue;
    private final VariableType variableType;

    public VariableSelection(final Device device, final String name, final Object value, final VariableType type)
    {
        super(device);

        variableName = name;
        variableValue = value;
        variableType = type;
    }

    @Override
    public Comparison compare(Selection other)
    {
        if (other instanceof VariableSelection)
        {
            VariableSelection o = (VariableSelection) other;
            int comp = 0;

            if (o.variableType == variableType)
            {
                switch (variableType)
                {
                    case TYPE_STRING:
                        comp = ((String)variableValue).compareToIgnoreCase((String)o.variableValue);
                        break;

                    case TYPE_NUMERIC:
                        comp = ((Double)variableValue).compareTo((Double) o.getVariableValue());
                        break;

                    case TYPE_DATE:
                        comp = ((Date)variableValue).compareTo((Date) o.getVariableValue());
                        break;

                    default:
                        return Comparison.NOT_COMPARABLE;
                }
            }

            if (comp < 0)
            {
                return Comparison.LESS_THAN;
            }
            else if (comp > 0)
            {
                return Comparison.GREATER_THAN;
            }
            return Comparison.EQUAL_TO;
        }

        return Comparison.NOT_COMPARABLE;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public Object getVariableValue()
    {
        return variableValue;
    }

    public VariableType getVariableType()
    {
        return variableType;
    }

    @Override
    public String toString()
    {
        String type = "unknown";

        switch (variableType)
        {
            case TYPE_STRING:
                type = "string";
                break;

            case TYPE_NUMERIC:
                type = "number";
                break;

            case TYPE_DATE:
                type = "date";
                break;
        }

        return "Variable: " + type + " " + getDevice().getSymbolic() + "." + variableName + " = '" + variableValue + "'";
    }
}

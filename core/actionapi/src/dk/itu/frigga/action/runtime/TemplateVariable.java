package dk.itu.frigga.action.runtime;

import org.w3c.dom.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-22
 */
public class TemplateVariable
{
    private final String name;
    private Value value = new Value();
    private List<VariableChangedListener> variableChangedListeners = Collections.synchronizedList(new LinkedList<VariableChangedListener>());

    public class Value
    {
        private boolean hasValue = false;
        private String value;
        private String type;

        public Value()
        {
            type = "variant";
        }

        public Value(final Value other)
        {
            hasValue = other.hasValue;
            value = other.value;
            type = other.type;
        }

        public void setType(final String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }

        public void unset()
        {
            hasValue = false;
            value = "";
        }

        public boolean isSet()
        {
            return hasValue;
        }

        public String asString()
        {
            return value;
        }

        public double asNumber()
        {
            return Double.parseDouble(value);
        }

        public Date asDate() throws ParseException
        {
            return DateFormat.getInstance().parse(value);
        }

        @Override
        public String toString()
        {
            if (!hasValue) return "null";

            return "(" + type + ")'" + value + "'";
        }
    }

    public TemplateVariable(final String name)
    {
        this.name = name;
    }

    public TemplateVariable.Value getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public void set(final String value)
    {
        Value oldValue = new Value(this.value);

        this.value.value = value;
        this.value.hasValue = true;

        for (VariableChangedListener listener : variableChangedListeners)
        {
            listener.variableValueChanged(this, this.value, oldValue);
        }
    }

    public void set(final double value)
    {
        set(Double.toString(value));
    }

    public void set(final Date value)
    {
        set(value.toString());
    }

    public void parse(final Element element)
    {

    }

    public void addVariableChangedListener(final VariableChangedListener listener)
    {
        variableChangedListeners.add(listener);
    }

    public void removeVariableChangedListener(final VariableChangedListener listener)
    {
        variableChangedListeners.remove(listener);
    }
}

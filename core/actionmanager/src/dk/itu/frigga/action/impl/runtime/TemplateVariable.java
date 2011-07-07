package dk.itu.frigga.action.impl.runtime;

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
    private String name;
    private String source;
    private Value value = new Value();
    private List<VariableChangedListener> variableChangedListeners = Collections.synchronizedList(new LinkedList<VariableChangedListener>());

    public class Value
    {
        private boolean hasValue = false;
        private Object value;
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
            return value.toString();
        }

        public double asNumber() throws InvalidVariableTypeException
        {
            if (value instanceof Double)
            {
                return (Double)value;
            }
            if (value instanceof String)
            {
                return Double.parseDouble((String)value);
            }

            throw new InvalidVariableTypeException();
        }

        public Date asDate() throws ParseException, InvalidVariableTypeException
        {
            if (value instanceof Date)
            {
                return (Date)value;
            }
            if (value instanceof String)
            {
                return DateFormat.getInstance().parse((String)value);
            }

            throw new InvalidVariableTypeException();
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

    public String getSource()
    {
        return source;
    }

    private void set(final Object value, final String type)
    {
        Value oldValue = new Value(this.value);

        this.value.value = value;
        this.value.type = type;
        this.value.hasValue = true;

        for (VariableChangedListener listener : variableChangedListeners)
        {
            listener.variableValueChanged(this, this.value, oldValue);
        }
    }

    public void set(final String value)
    {
        set(value, "string");
    }

    public void set(final double value)
    {
        set(value, "number");
    }

    public void set(final Date value)
    {
        set(value, "date");
    }

    public void parse(final Element element)
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (element.getTagName().equals("variable"))
        {
            name = element.getAttribute("name");
            source = element.getAttribute("source");
            set(element.getAttribute("value"));
        }
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager.parser.block;

import dk.itu.frigga.action.block.Condition;

import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author phylock
 */
public class ConditionParser/*<T extends Condition>*/ implements BlockParser
{

    private Class type;

    //TODO: figure out how to get class of generic type so we can avoid the parameter and use generic instead
    public ConditionParser(Class type)
    {
        this.type = type;
    }

    private static final String[] POSSIBLE_CHILDREN = new String[]{"variable", "device", "category", "location", "global", "and", "or"};

    public Condition parse(Document doc, Element element)
    {
        try
        {
            return (Condition) type.newInstance();
        } catch (InstantiationException ex)
        {
            return null;
        } catch (IllegalAccessException ex)
        {
            return null;
        }
    }

    public String[] childElementsTypes()
    {
        return POSSIBLE_CHILDREN;
    }
}

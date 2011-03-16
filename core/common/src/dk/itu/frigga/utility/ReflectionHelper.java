/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.utility;

import java.lang.reflect.Field;

/**
 *
 * @author phylock
 */
public class ReflectionHelper {

    /**
     * Update reference in subclass, even if its private
     * @param clazz the class to update
     * @param field the field name
     * @param obj the class instance to update
     * @param value the new value
     */
    public static void updateSubclassFields(Class clazz, String field, Object obj, Object value) throws NoSuchFieldException, IllegalArgumentException {
        try {
            Field f = clazz.getDeclaredField(field);
            boolean access = f.isAccessible();
            f.setAccessible(true);
            f.set(obj, value);
            f.setAccessible(access);
        } catch (IllegalAccessException ex) {
            //Will never happen, because accessibility is set to true
        }
    }

    /**
     * Update reference in subclass, even if its private, the main class
     * type is used, if the field belongs to a parent class please specify
     * @param field the field name
     * @param obj the class instance to update, and where class type is retrieved from
     * @param value the new value
     */
    public static void updateSubclassFields(String field, Object obj, Object value) throws NoSuchFieldException, IllegalArgumentException {
        updateSubclassFields(obj.getClass(), field, obj, value);
    }
}

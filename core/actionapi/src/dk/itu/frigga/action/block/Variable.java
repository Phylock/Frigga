/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.block;

/**
 *
 * @author phylock
 */
public class Variable extends BaseCondition {

  public enum Comparison {

    IsEqual, IsNotEqual, IsGreater, IsLess, IsBetween, IsOfCategory;
  }

  public enum Type {

    String, Numeric, Date
  }
  private Comparison comparison;
  private Type type;
  private String name;
  private String value;
  private String optional_value;

  public Variable(Comparison comparison, Type type, String name, String value) {
    this.comparison = comparison;
    this.type = type;
    this.name = name;
    this.value = value;
    this.optional_value = "";
  }

  public Variable(Comparison comparison, Type type, String name, String value, String optional_value) {
    this.comparison = comparison;
    this.type = type;
    this.name = name;
    this.value = value;
    this.optional_value = optional_value;
  }

  public Comparison getComparison() {
    return comparison;
  }

  public Type getType() {
    return type;
  }

  public String getOptional_value() {
    return optional_value;
  }

  public String getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
  
}

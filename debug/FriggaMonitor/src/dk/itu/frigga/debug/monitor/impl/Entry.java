/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.debug.monitor.impl;

import java.util.Date;

/**
 *
 * @author phylock
 */
public class Entry {
  public enum Type
  {
    Debug, Info, Warning, Error
  }
  private Type type;
  private Date time;
  private String bundle;
  private String message;
  private String error_message;
  private String error_trace;

  public Entry(Type type, Date time, String bundle, String message, Throwable error) {
    this(type, time, bundle, message);
    if(error != null){
      this.error_message = error.getLocalizedMessage();
      this.error_trace = ""; //TODO: parse stacktrace
    }
  }

  public Entry(Type type, Date time, String bundle, String message) {
    this.type = type;
    this.time = time;
    this.message = message;
    this.bundle = bundle;
    this.error_message = "";
    this.error_trace = "";
  }

  public String getErrorMessage() {
    return error_message;
  }

  public String getErrorTrace() {
    return error_trace;
  }

  public String getMessage() {
    return message;
  }

  public Date getTime() {
    return time;
  }

  public Type getType() {
    return type;
  }

  public String getBundle() {
    return bundle;
  }

  @Override
  public String toString() {
    return "Entry{" + "type=" + type + ", time=" + time + ", bundle=" + bundle + ", message=" + message + '}';
  }
}

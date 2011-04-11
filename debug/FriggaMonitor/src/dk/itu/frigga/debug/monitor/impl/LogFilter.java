/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.debug.monitor.impl;

import javax.swing.RowFilter;

/**
 *
 * @author phylock
 */
public class LogFilter extends RowFilter<Object, Object>{
  private boolean showError = true;
  private boolean showWarning = true;
  private boolean showInfo = true;
  private boolean showDebug = true;

  private String bundleFilter = "";
  private String messageFilter = "";

  public LogFilter(boolean debug, boolean info, boolean warning, boolean error) {
    showDebug = debug;
    showError = error;
    showInfo = info;
    showWarning = warning;
  }


  public LogFilter() {
  }

  public String getBundleFilter() {
    return bundleFilter;
  }

  public void setBundleFilter(String bundleFilter) {
    this.bundleFilter = bundleFilter.toLowerCase();
  }

  public String getMessageFilter() {
    return messageFilter;
  }

  public void setMessageFilter(String messageFilter) {
    this.messageFilter = messageFilter.toLowerCase();
  }

  public boolean isShowDebug() {
    return showDebug;
  }

  public void setShowDebug(boolean showDebug) {
    this.showDebug = showDebug;
  }

  public boolean isShowError() {
    return showError;
  }

  public void setShowError(boolean showError) {
    this.showError = showError;
  }

  public boolean isShowInfo() {
    return showInfo;
  }

  public void setShowInfo(boolean showInfo) {
    this.showInfo = showInfo;
  }

  public boolean isShowWarning() {
    return showWarning;
  }

  public void setShowWarning(boolean showWarning) {
    this.showWarning = showWarning;
  }

  @Override
  public boolean include(Entry entry) {
    boolean show = true;
    LogEntryModel row = ((LogTableModel)entry.getModel()).getRowEntry((Integer)entry.getIdentifier());
    LogEntryModel.Type type = row.getType();
    String bundle = row.getBundle();
    String message = row.getMessage();
    switch(type)
    {
      case Debug:
        show &= showDebug;
        break;
      case Error:
        show &= showError;
        break;
      case Info:
        show &= showInfo;
        break;
      case Warning:
        show &= showWarning;
        break;
    }

    if(!bundleFilter.isEmpty() && !bundle.equals(bundleFilter))
    {
      show &= false;
    }

    if(!messageFilter.isEmpty() && !message.toLowerCase().contains(messageFilter))
    {
      show &= false;
    }

    return show;
  }
}

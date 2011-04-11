/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.monitor.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author phylock
 */
public class LogTableModel extends AbstractTableModel {

  public static final int TYPE_INDEX = 0;
  public static final int TIME_INDEX = 1;
  public static final int BUNDLE_INDEX = 2;
  public static final int MESSAGE_INDEX = 3;
  public static final int ERROR_INDEX = 4;
  private static final String[] COLUMNS = new String[]{"", "Time", "Bundle","Message", "Error"};
  private List<Entry> entries;

  public LogTableModel() {
    entries = new ArrayList<Entry>();
  }

  public int getRowCount() {
    return entries.size();
  }

  @Override
  public String getColumnName(int column) {
    return COLUMNS[column];
  }

  public int getColumnCount() {
    return COLUMNS.length;
  }

  @Override
  public Class getColumnClass(int column) {
    switch (column) {
      case TYPE_INDEX:
        return ImageIcon.class;
      case TIME_INDEX:
        return Date.class;
      case BUNDLE_INDEX:
      case MESSAGE_INDEX:
      case ERROR_INDEX:
        return String.class;
      default:
        return Object.class;
    }
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Entry row = entries.get(rowIndex);
    switch (columnIndex) {
      case TYPE_INDEX:
        return TypeImageIcon.getIcon(row.getType());
      case TIME_INDEX:
        return row.getTime();
      case BUNDLE_INDEX:
        return row.getBundle();
      case MESSAGE_INDEX:
        return row.getMessage();
      case ERROR_INDEX:
        return row.getErrorMessage();
      default:
        return new Object();
    }
  }

  public void addEntry(Entry entry) {
    entries.add(entry);
  }
}

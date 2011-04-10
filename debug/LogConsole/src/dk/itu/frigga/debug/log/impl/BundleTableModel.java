/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.log.impl;

import javax.swing.table.AbstractTableModel;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 *
 * @author phylock
 */
public class BundleTableModel extends AbstractTableModel {

  public static final int ID_INDEX = 0;
  public static final int SYMBOLIC_INDEX = 1;
  public static final int STATE_INDEX = 2;
  private static final String[] COLUMNS = new String[]{"#", "Symbolic", "State"};
  private static final String[] STATES = new String[]{"UNINSTALLED", "INSTALLED", "RESOLVED", "STARTING", "STOPPING", "ACTIVE", ""};
  private BundleContext bc;

  public BundleTableModel(BundleContext bc) {
    this.bc = bc;
  }

  public int getRowCount() {
    return bc.getBundles().length;
  }

  @Override
  public String getColumnName(int column) {
    return COLUMNS[column];
  }

  public int getColumnCount() {
    return COLUMNS.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Bundle bundle = bc.getBundles()[rowIndex];
    switch (columnIndex) {
      case ID_INDEX:
        return bundle.getBundleId();
      case SYMBOLIC_INDEX:
        return bundle.getSymbolicName();
      case STATE_INDEX:
        return parseState(bundle.getState());
      default:
        return "";
    }
  }

  private String parseState(int state) {
    int testbit = 1;
    int index;
    for (index = 0; index < STATES.length - 1; index++) {
      if ((state & testbit) != 0) {
        break;
      }
      testbit <<= 1;
    }

    return STATES[index];

  }
}

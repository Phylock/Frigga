/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.debug.log.impl;

import java.text.DateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author phylock
 */
public class DateCellRenderer extends DefaultTableCellRenderer {
    DateFormat formatter;
    public DateCellRenderer() { super(); }

  @Override
    public void setValue(Object value) {
        if (formatter==null) {
            formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        }
        setText((value == null) ? "" : formatter.format((Date)value));
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.debug.monitor.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author phylock
 */
public class TypeImageIcon {

  private static ImageIcon debug;
  private static ImageIcon info;
  private static ImageIcon warning;
  private static ImageIcon error;

  static {
    try {
      debug = new ImageIcon(ImageIO.read(TypeImageIcon.class.getResourceAsStream("debug.png")));
      info = new ImageIcon(ImageIO.read(TypeImageIcon.class.getResourceAsStream("info.png")));
      warning = new ImageIcon(ImageIO.read(TypeImageIcon.class.getResourceAsStream("warning.png")));
      error = new ImageIcon(ImageIO.read(TypeImageIcon.class.getResourceAsStream("error.png")));
    } catch (IOException ex) {
      Logger.getLogger(TypeImageIcon.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static ImageIcon getIcon(LogEntryModel.Type type)
  {
    switch(type)
    {
      case Debug:
        return debug;
      case Error:
        return error;
      case Info:
        return info;
      case Warning:
        return warning;
      default:
        return info;
    }
  }
}

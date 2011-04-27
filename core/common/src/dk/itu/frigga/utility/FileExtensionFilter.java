/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.utility;

import java.io.File;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author phylock
 */
public class FileExtensionFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
  private Set<String> extensions;
  private String description;

  public FileExtensionFilter(String[] extensions)
  {
  this(extensions, "");
  }
  
  public FileExtensionFilter(String[] extensions, String description) {
    HashSet<String> set = new HashSet<String>();
    set.addAll(Arrays.asList(extensions));
    this.extensions = Collections.unmodifiableSet(set);
    this.description = description;
  }

  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = FileHelper.getExtension(f);
    if (extension != null) {
      return extensions.contains(extension);
    }
    return false;
  }

  public String getDescription() {
    return description;
  }
}

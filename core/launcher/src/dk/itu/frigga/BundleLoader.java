/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

/**
 *
 * @author phylock
 */
public class BundleLoader {

  public static void loadDirectory(Framework framework, File directory) {
    BundleContext bc = framework.getBundleContext();
    File[] bundles = directory.listFiles(new FileFilter() {

      public boolean accept(File f) {
        if (f.isDirectory()) {
          return false;
        }
        String name = f.getName().toLowerCase();
        return name.endsWith("jar");
      }
    });
    //Install Bundles
    List<Bundle> installed = new ArrayList<Bundle>();
    if (bundles != null) {
      for (File f : bundles) {
        try {
          installed.add(bc.installBundle("file:" + f.toString()));
        } catch (BundleException ex) {
          System.out.println("Bundle load error: " + ex);
        }
      }
    }
    //Start Bundles
    for(Bundle bundle: installed)
    {
      try {
        bundle.start();
      } catch (BundleException ex) {
        System.out.println("Bundle start error: " + ex);
      }
    }
  }
}

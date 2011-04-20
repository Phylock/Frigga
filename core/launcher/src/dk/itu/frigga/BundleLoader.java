/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

/**
 *
 * @author phylock
 */
public class BundleLoader {

  public static CoreLoadInfo loadDirectory(Framework framework, final File directory) {
    CoreLoadInfo info = new CoreLoadInfo();
    long start = System.nanoTime();
    BundleContext bc = framework.getBundleContext();

    List<File> files = Arrays.asList(directory.listFiles(new BundleFilter()));
    Map<File, Bundle> bundles = new HashMap<File, Bundle>();
    for (Bundle bundle : bc.getBundles()) {
      if (bundle.getLocation().startsWith("file:")) {
        File f = new File(bundle.getLocation().substring(5));
        if (f.getParentFile().equals(directory)) {
          bundles.put(f, bundle);
        }
      }
    }
    List<File> bundle_keys = new ArrayList<File>(bundles.keySet());

    List<File> installs = new LinkedList<File>();
    List<File> uninstalls = new LinkedList<File>();
    List<File> updates = new LinkedList<File>();
    List<Bundle> installed = new LinkedList<Bundle>();

    uncommonElements(files, bundle_keys, installs);
    commonElements(files, bundle_keys, updates);
    uncommonElements(bundle_keys, files, uninstalls);

    //Uninstall
    info.uninstalled = 0;
    for (File uninstall : uninstalls) {
      try {
        Bundle b = bundles.get(uninstall);
        b.uninstall();
        info.uninstalled++;
      } catch (BundleException ex) {
        Logger.getLogger(BundleLoader.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    //Update
    info.updated = 0;
    for (File update : updates) {
      Bundle b = bundles.get(update);
      if (b.getLastModified() < update.lastModified()) {
        try {
          b.update();
          info.updated++;
        } catch (BundleException ex) {
          Logger.getLogger(BundleLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    //Install Bundles
    info.installed = 0;
    for (File install : installs) {
      try {
        installed.add(bc.installBundle("file:" + install.toString()));
        info.installed++;
      } catch (BundleException ex) {
        Logger.getLogger(BundleLoader.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    //Start installed Bundles
    for (Bundle bundle : installed) {
      if (bundle.getState() != Bundle.ACTIVE) {
        try {
          bundle.start();
        } catch (BundleException ex) {
          Logger.getLogger(BundleLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    info.duration = (double)(System.nanoTime() - start) / 1000000;
    return info;
  }

  private static class BundleFilter implements FileFilter {

    public boolean accept(File f) {
      if (f.isDirectory()) {
        return false;
      }
      String name = f.getName().toLowerCase();
      return name.endsWith("jar");
    }
  }

  public static <T> void commonElements(final List<T> l1, final List<T> l2, final List<T> result) {
    result.clear();
    Set<T> lookup = new HashSet<T>(l2);
    for (T item : l1) {
      if (lookup.contains(item)) {
        result.add(item);
      }
    }
  }

  public static <T> void uncommonElements(final List<T> l1, final List<T> l2, final List<T> result) {
    result.clear();
    Set<T> lookup = new HashSet<T>(l2);
    for (T item : l1) {
      if (!lookup.contains(item)) {
        result.add(item);
      }
    }
  }
}

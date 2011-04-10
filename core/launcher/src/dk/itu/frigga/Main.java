/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 *
 * @author phylock
 */
public class Main {

  private static final String VERSION = "0.1.0.SNAPSHOT";
  private static final String PROJECTNAME = "Frigga";
  private static final String FELIX_CONFIG_DIR = "conf/";
  private static final String FELIX_CONFIG_APPLICATION_DIR = FELIX_CONFIG_DIR + "application/";
  private static final String FELIX_CONFIG_FILE = FELIX_CONFIG_DIR + "config.properties";
  
  private Framework framework = null;
  private Properties config;

  public void load() {
    config = new Properties();
    loadSystemSettings(config);
    loadMasterSettings(config);
    loadBundleSettings(config);
  }

  private void loadSystemSettings(Properties config) {
    config.putAll(System.getProperties());
  }

  private void loadMasterSettings(Properties config) {
    try {
      Properties master = new Properties();
      File file = new File(FELIX_CONFIG_FILE);
      if (file.exists()) {
        master.load(new FileInputStream(file));
        config.putAll(master);
        config.put("felix.fileinstall.dir", FELIX_CONFIG_APPLICATION_DIR);
        config.put("felix.fileinstall.filter", "*.cfg");
      }
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void loadBundleSettings(Properties config) {
  }

  public void initialize() throws Exception {
    initializeFramework();
    initializeCore();
    initializePlugins();
  }

  private void initializeFramework() throws Exception {
    framework = getFrameworkFactory().newFramework(config);
    framework.init();
    framework.start();
    AutoProcessor.process(config, framework.getBundleContext());
  }

  private void initializeCore() throws Exception {
    BundleLoader.loadDirectory(framework, new File("bundle"));
  }

  private void initializePlugins() throws Exception {
    BundleLoader.loadDirectory(framework, new File("plugins"));
  }

  public void printBanner() {
    System.out.println(String.format("Initializing %s (v. %s)", PROJECTNAME, VERSION));

  }

  public void run() throws Exception {
    framework.waitForStop(0);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException {
    try {
      Main main = new Main();
      main.printBanner();
      main.load();
      main.initialize();

      //blocking call, returns at system exit
      main.run();

      System.exit(0);
    } catch (Exception ex) {
      System.err.println("Could not create framework: " + ex);
      ex.printStackTrace();
      System.exit(-1);
    }
  }

  /** Helper **/
  private static FrameworkFactory getFrameworkFactory() throws Exception {
    ServiceLoader loader = ServiceLoader.load(FrameworkFactory.class);
    if (loader.iterator().hasNext()) {
      return (FrameworkFactory) loader.iterator().next();
    } else {
      throw new Exception("Could not find framework factory.");
    }
  }
}

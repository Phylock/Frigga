/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 *
 * @author phylock
 */
public class Main {

  private static final String VERSION = "0.1.0.SNAPSHOT";
  private static final String PROJECTNAME = "Frigga";
  private static final String FELIX_CONFIG_FILE = "conf/config.properties";
  private static final String FELIX_CONFIG_PROPERTIES = "felix.config.properties";
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
  }

  private void initializeCore() throws Exception {
  }

  private void initializePlugins() throws Exception {
  }

  public void printBanner() {
    System.out.println(String.format("Initializing %s (v. %s)", PROJECTNAME, VERSION));

  }

  public void run() throws Exception {
    framework.init();
    AutoProcessor.process(config, framework.getBundleContext());
    framework.start();
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

      //blocking call, returns at sustem exit
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga;

import java.io.File;
import java.io.FileInputStream;
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
  //TODO: the default one, allow overwrite from argument
  private static final String CONFIG_DIRECTORY_KEY ="frigga.config.dir";
  private static final String DEFAULT_CONFIG_DIR = "conf/";
  private static final String CONFIG_APPLICATION_DIR = "application/";
  private static final String CONFIG_FILE = "config.properties";
  private final String config_dir;

  public Main(String config_dir) {
    if(!config_dir.endsWith("/"))
    {
      config_dir += "/";
    }
    this.config_dir = config_dir;
  }
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
      config.put(CONFIG_DIRECTORY_KEY, config_dir);
      File file = new File(config_dir + CONFIG_FILE);
      if (file.exists()) {
        master.load(new FileInputStream(file));
        config.putAll(master);
        //Start looking for instance config
        //TODO: move to configuration bundle
        config.put("felix.fileinstall.dir", config_dir + CONFIG_APPLICATION_DIR);
        config.put("felix.fileinstall.filter", ".*\\.cfg");
        config.put("felix.fileinstall.log.level", "-1");

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
    AutoProcessor.process(config, framework.getBundleContext());
  }

  private void initializeCore() throws Exception {
    System.out.print("Initialize Core ... ");
    CoreLoadInfo info = BundleLoader.loadDirectory(framework, new File("bundle"));
    System.out.printf("%f ms\n", info.duration);
    System.out.printf("Installed: %d, Updated: %d, Uninstalled: %d\n", info.installed, info.updated, info.uninstalled);

  }

  private void initializePlugins() throws Exception {
    //BundleLoader.loadDirectory(framework, new File("plugins"));
  }

  public void printBanner() {
    System.out.println(String.format("Initializing %s (v. %s)", PROJECTNAME, VERSION));

  }

  public void run() throws Exception {
    framework.start();
    framework.waitForStop(0);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException {
    String dir;
    if (args.length == 0) {
      dir = DEFAULT_CONFIG_DIR;
    } else {
      dir = args[0];
    }
    try {
      Main main = new Main(dir);

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

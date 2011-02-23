/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class Main {
    private static final String FELIX_CONFIG_FILE = "conf/config.properties";
    private static final String FELIX_CONFIG_PROPERTIES = "felix.config.properties";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Initialize
        File conf = new File(FELIX_CONFIG_FILE);
        String test = "file:" + conf.getAbsolutePath();
        System.setProperty(FELIX_CONFIG_PROPERTIES, "file:" + conf.getAbsolutePath());
        
        if(!conf.exists())
        {
            //TODO: Generate default
        }


        //Felix wrapper
        try {
            org.apache.felix.main.Main.main(args);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

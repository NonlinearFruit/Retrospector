/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import static retrospector.util.UtilityCloset.getPath2JarFolder;

/**
 * Read and write property files.
 * @author nonfrt
 */
public class PropertyManager {
    public static final String configPath="Retrospector.config";
    private static Configuration config = null;

    public static class Configuration{
        public static enum prop{DEFAULT_USER,MAX_RATING,DEFAULT_RATING};
        
        private String defaultUser;
        private Integer maxRating;
        private Integer defaultRating;
        private String[] categories;
        
        public Configuration(){
            defaultUser = "Ben";
            maxRating = 10;
            defaultRating = 6;
        }
        
        public Configuration(String user, Integer maxRate, Integer defaultRate){
            defaultUser = user;
            maxRating = maxRate;
            defaultRating = defaultRate;
        }

        public String getDefaultUser() {
            return defaultUser;
        }

        public void setDefaultUser(String defaultUser) {
            this.defaultUser = defaultUser;
        }

        public Integer getMaxRating() {
            return maxRating;
        }

        public void setMaxRating(Integer maxRating) {
            this.maxRating = maxRating;
        }

        public Integer getDefaultRating() {
            return defaultRating;
        }

        public void setDefaultRating(Integer defaultRating) {
            this.defaultRating = defaultRating;
        }

        public String[] getCategories() {
            return categories;
        }

        public void setCategories(String[] categories) {
            this.categories = categories;
        }
        
    }
    
    public static void saveProperties(Configuration config) throws IOException,URISyntaxException{
        PropertyManager.config = config;
        Properties prop = new Properties();
        prop.setProperty(Configuration.prop.DEFAULT_USER.name(), config.getDefaultUser());
        prop.setProperty(Configuration.prop.MAX_RATING.name(), config.getMaxRating().toString());
        prop.setProperty(Configuration.prop.DEFAULT_RATING.name(), config.getDefaultRating().toString());
        FileOutputStream out = new FileOutputStream(getPath2JarFolder()+configPath);
        saveProperties(prop, out);
    }
    
    public static Configuration loadProperties(){
        if(PropertyManager.config!=null)
            return config;
        try{
            FileInputStream in = new FileInputStream(getPath2JarFolder()+configPath);
            Properties prop;
            prop = loadProperties(in);
            config = new Configuration(
                    prop.getProperty(Configuration.prop.DEFAULT_USER.name()),
                    Integer.parseInt(prop.getProperty(Configuration.prop.MAX_RATING.name())),
                    Integer.parseInt(prop.getProperty(Configuration.prop.DEFAULT_RATING.name()))
            );
            return config;
        } catch(IOException|NumberFormatException|URISyntaxException e) {
            System.err.println("Loading Config File Failed!");
            Configuration dfault = new Configuration();
            try{ saveProperties(dfault); } catch(IOException|URISyntaxException ex) {} // Since there isn't a config file, make one!
            return dfault;
        }
    }
    
    public static Configuration forceLoadProperties(boolean tf) throws IOException,URISyntaxException{
        if(tf)
            config = null;
        return loadProperties();
    }
    
    public static void saveProperties(Properties prop, FileOutputStream output) throws IOException {
        try {
            prop.store(output, null);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    public static Properties loadProperties(FileInputStream input) throws IOException {
        Properties prop = new Properties();

        try {
            prop.load(input);
            return prop;
        } finally {
            if (input != null)
                input.close();
        }
    }
}


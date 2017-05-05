/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Read and write property files.
 * @author nonfrt
 */
public class PropertyManager {
    public static final String retroFolder=System.getProperty("user.home")+"/Retrospector";
    private static final String configPath=retroFolder+"/Retrospector.config";
    private static Configuration config = null;

    public static class Configuration{
        public static enum prop{DEFAULT_USER,MAX_RATING,DEFAULT_RATING,CATEGORIES,FACTOIDS,GITHUB_USER};
        
        private String defaultUser;
        private String githubUser;
        private Integer maxRating;
        private Integer defaultRating;
        private String[] categories;
        private String[] factoids;

        public Configuration(){
            defaultUser = "Ben";
            maxRating = 10;
            defaultRating = 6;
            githubUser = "";
            categories = new String[]{
                "Movie",
                "TV Series",
                "Book",
                "Comic",
                "Podcast",
                "YouTube",
                "Poem",
                "Music",
                "Video Game",
                "Tabletop Game",
                "Product",
                "Other"
            };
            factoids = new String[]{
                "Genre",
                "Year",
                "Creator"
            };
        }
        
        public Configuration(String user, String github, Integer maxRate, Integer defaultRate, String[] category, String[] factoid){
            defaultUser = user;
            githubUser = github;
            maxRating = maxRate;
            defaultRating = defaultRate;
            categories = category;
            factoids = factoid;
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
        
        public String[] getFactoids() {
            return factoids;
        }

        public void setFactoids(String[] factoids) {
            this.factoids = factoids;
        }
        
        public String getGithubUser() {
            return githubUser;
        }
        
        public void setGithubUser(String github) {
            githubUser = github;
        }
        
    }
    
    public static void saveProperties(Configuration config) throws IOException,URISyntaxException{
        PropertyManager.config = config;
        Properties prop = new Properties();
        prop.setProperty(Configuration.prop.DEFAULT_USER.name(), config.getDefaultUser());
        prop.setProperty(Configuration.prop.GITHUB_USER.name(), config.getGithubUser());
        prop.setProperty(Configuration.prop.MAX_RATING.name(), config.getMaxRating().toString());
        prop.setProperty(Configuration.prop.DEFAULT_RATING.name(), config.getDefaultRating().toString());
        prop.setProperty(Configuration.prop.CATEGORIES.name(), String.join(",",config.getCategories()));
        prop.setProperty(Configuration.prop.FACTOIDS.name(), String.join(",",config.getFactoids()));
        File directory = new File(retroFolder);
            if (!directory.exists())
                directory.mkdir();
        FileOutputStream out = new FileOutputStream(configPath);
        saveProperties(prop, out);
    }
    
    public static Configuration loadProperties(){
        if(PropertyManager.config!=null)
            return config;
        try{
            FileInputStream in = new FileInputStream(configPath);
            Properties prop;
            prop = loadProperties(in);
            config = new Configuration(
                    prop.getProperty(Configuration.prop.DEFAULT_USER.name()),
                    prop.getProperty(Configuration.prop.GITHUB_USER.name()),
                    Integer.parseInt(prop.getProperty(Configuration.prop.MAX_RATING.name())),
                    Integer.parseInt(prop.getProperty(Configuration.prop.DEFAULT_RATING.name())),
                    prop.getProperty(Configuration.prop.CATEGORIES.name()).split(","),
                    prop.getProperty(Configuration.prop.FACTOIDS.name()).split(",")
            );
            return config;
        } catch(IOException|NumberFormatException e) {
            System.err.println("Loading Config File Failed!");
            Configuration dfault = new Configuration();
            try{ saveProperties(dfault); } catch(IOException|URISyntaxException ex) {System.err.println(ex.getMessage());} // Since there isn't a config file, make one!
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


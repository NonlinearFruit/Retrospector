/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import retrospector.Retrospector;
import retrospector.fxml.CoreController.Chartagories;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Media.Type;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class UtilityCloset {
    
    public static final String separator = "\t";
    public static final String alternative  = "    ";
    
    public static String cleanUp(String string){
        return string.replaceAll(separator, alternative);
    }
    
    public static String getPath2JarFolder() throws URISyntaxException{
        String path = Retrospector.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        if(path.indexOf(File.separator)!=-1)
            return path.substring(0,path.lastIndexOf(File.separator)+File.separator.length());
        return path;
    }
    
    public static String readFile(String filepath)
    {
        String content="";
        try {
            content = new Scanner(new File(filepath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException ex) {
            System.err.println("Read File FAILED: File could not be read: <ExteriorInteraction.readFile>");
        }
        return content;
    }
    
    public static void writeFile(String filepath, String text)
    {
        try (PrintWriter out = new PrintWriter(filepath)) {
            out.println(text);
        } catch(FileNotFoundException e) {
            System.err.println("Read File FAILED: File could not be read: <ExteriorInteraction.readFile>");
        }
    }
    
    public static boolean isSameMedia(Chartagories option, Media one, Media two){
        boolean isSame = false;
        
        String titleOne = one.getTitle();
        String creatorOne = one.getCreator();
        String seasonOne = one.getSeasonId();
        String episodeOne = one.getEpisodeId();
        String categoryOne = one.getCategory();
        Type typeOne = one.getType();
        
        String titleTwo = two.getTitle();
        String creatorTwo = two.getCreator();
        String seasonTwo = two.getSeasonId();
        String episodeTwo = two.getEpisodeId();
        String categoryTwo = two.getCategory();
        Type typeTwo = two.getType();
        
        switch(option){
            case CATEGORY:
                isSame = categoryOne.equals(categoryTwo);
                break;
            case CREATOR:
                isSame = creatorOne.equals(creatorTwo);
                break;
            case SERIES:
            case MINISERIES:
                if(
                        titleOne.equals(titleTwo) &&
                        creatorOne.equals(creatorTwo) &&
                        categoryOne.equals(categoryTwo) &&
                        typeOne.equals(typeTwo)
                )
                    isSame = true;
                break;
            case SEASON:
                if(
                        titleOne.equals(titleTwo) &&
                        creatorOne.equals(creatorTwo) &&
                        seasonOne.equals(seasonTwo) &&
                        categoryOne.equals(categoryTwo) &&
                        typeOne.equals(typeTwo)
                )
                    isSame = true;
                break;
            case CURRENT_MEDIA:
            default:
                if(
                        titleOne.equals(titleTwo) &&
                        creatorOne.equals(creatorTwo) &&
                        seasonOne.equals(seasonTwo) &&
                        episodeOne.equals(episodeTwo) &&
                        categoryOne.equals(categoryTwo) &&
                        typeOne.equals(typeTwo)
                )
                    isSame = true;
                break;
                
        }
        
        return isSame;
    }
    
}

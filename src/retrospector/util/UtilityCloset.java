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
import retrospector.model.Media.Category;
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
    
    public static String mediaToString(Media media){
        String string = "";
        string += cleanUp(media.getTitle())+separator;
        string += cleanUp(media.getCreator())+separator;
        string += cleanUp(media.getSeasonId())+separator;
        string += cleanUp(media.getEpisodeId())+separator;
        string += cleanUp(media.getCategory().toString())+separator;
        string += cleanUp(media.getType().toString())+separator;
        string += cleanUp(media.getDescription());
        return string;
    }
    
    public static Media stringToMedia(String string){
        Media media = new Media();
        String[] items = string.split(separator);
        media.setTitle(items[0]);
        media.setCreator(items[1]);
        media.setSeasonId(items[2]);
        media.setEpisodeId(items[3]);
        media.setCategory(Media.Category.valueOf(items[4]));
        media.setType(Media.Type.valueOf(items[5]));
        media.setDescription(items[6]);
        return media;
    }
    
    public static String reviewToString(Review review){
        String string = "";
        string += cleanUp(review.getUser())+separator;
        string += cleanUp(review.getReview())+separator;
        string += cleanUp(review.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))+separator;
        string += cleanUp(review.getRating().toString());
        return string;
    }
    
    public static Review stringToReview(String string){
        Review review = new Review();
        String[] items = string.split(separator);
        review.setUser(items[0]);
        review.setReview(items[1]);
        review.setDate(LocalDate.parse(items[2], DateTimeFormatter.ISO_LOCAL_DATE));
        review.setRating(BigDecimal.valueOf(Double.valueOf(items[3])));
        return review;
    }
    
    public static boolean isSameMedia(Chartagories option, Media one, Media two){
        boolean isSame = false;
        
        String titleOne = one.getTitle();
        String creatorOne = one.getCreator();
        String seasonOne = one.getSeasonId();
        String episodeOne = one.getEpisodeId();
        Category categoryOne = one.getCategory();
        Type typeOne = one.getType();
        
        String titleTwo = two.getTitle();
        String creatorTwo = two.getCreator();
        String seasonTwo = two.getSeasonId();
        String episodeTwo = two.getEpisodeId();
        Category categoryTwo = two.getCategory();
        Type typeTwo = two.getType();
        
        switch(option){
            case CATEGORY:
                isSame = categoryOne.equals(categoryTwo);
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
            case ALL_SEASONS:
                if(
                        titleOne.equals(titleTwo) &&
                        creatorOne.equals(creatorTwo) &&
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import retrospector.util.PropertyManager;
import retrospector.util.UtilityCloset;

/**
 *
 * @author nonfrt
 */
public class DataManager {
    
    public static final String saveFolder = "saves"+File.separator;
    
    public static ObservableList<Media> media = FXCollections.observableArrayList(
                new Media("The Imitation Game","",Media.Category.MOVIE,Media.Type.SINGLE, new ArrayList<Review>(){{
                        add(new Review(BigDecimal.valueOf(4.3)));
                        add(new Review(BigDecimal.valueOf(5.0)));
                        add(new Review(BigDecimal.valueOf(4.5)));
                }}),
                new Media("Ender's Game","",Media.Category.MOVIE,Media.Type.SINGLE, new ArrayList<Review>(){{
                        add(new Review(BigDecimal.valueOf(8.0),LocalDate.of(1990,4,4)));
                        add(new Review(BigDecimal.valueOf(7.6),LocalDate.of(2020,5,5)));
                        add(new Review(BigDecimal.valueOf(6.0),LocalDate.of(2015, Month.MARCH, 14)));
                }}),
                new Media("The Walking Dead","",Media.Category.TV_SERIES,Media.Type.SERIES, new ArrayList<Review>(){{
                        add(new Review(BigDecimal.valueOf(10.0)));
                        add(new Review(BigDecimal.valueOf(9.4)));
                        add(new Review(BigDecimal.valueOf(9.0)));
                }})
        );

    public static String getDefaultUser(){
        return PropertyManager.loadProperties().getDefaultUser();
    }
    
    public static Integer getMaxRating(){
        return PropertyManager.loadProperties().getMaxRating();
    }
    
    public static Integer getDefaultRating(){
        return PropertyManager.loadProperties().getDefaultRating();
    }
    
    public static ObservableList<Media> getMedia(){
        return media;
    }
    
    public static ObservableList<Review> getReviews(){
        ObservableList<Review> reviews = FXCollections.observableArrayList();
        for (Media media : getMedia()) {
            reviews.addAll(media.getReviews());
        }
        return reviews;
    }
    
    public static ObservableList<String> getUsers(){
        ObservableList<String> users = FXCollections.observableArrayList();
        for (Review review : getReviews()) {
            if(!users.contains(review.getUser()))
                users.add(review.getUser());
        }
        return users;
    }
    
    public static void save(List<Media> media){
        try{
            String location = UtilityCloset.getPath2JarFolder()+saveFolder;
            int countM = 0;
            for (Media medium : media) {
                int countR = 0;
                String file = location+"Media"+countM+".txt";
                UtilityCloset.writeFile(file, UtilityCloset.mediaToString(medium));
                countM += 1;
                for (Review review : medium.getReviews()) {
                    String reviewFile = location+"Media"+countM+"-R"+countR+".txt";
                    UtilityCloset.writeFile(reviewFile,UtilityCloset.reviewToString(review));
                    countR += 1;
                }
            }
        }
        catch(URISyntaxException ex){
            System.err.println("Jar location could not be found");
        }
    }
    
    public static ObservableList<Media> load(){
        ObservableList<Media> media = FXCollections.observableArrayList();
        try{
            String location = UtilityCloset.getPath2JarFolder()+saveFolder;
            try(Stream<Path> paths = Files.walk(Paths.get(location))) {
                paths.filter(Files::isRegularFile)
                    .forEach(System.out::println);
            } catch(IOException e) {
                System.err.println("Jar location could not be I/O-ed");
            }
        } catch(URISyntaxException ex) {
            System.err.println("Jar location could not be found");
        }
        return media;
    }
    
    public static void clearSaves(){
        try{
            Arrays.stream(new File(UtilityCloset.getPath2JarFolder()+saveFolder).listFiles())
                    .forEach(file->file.delete());
        } catch(Exception e) {
            System.err.println("Jar location could not be I/O-ed");
        }
    }
}

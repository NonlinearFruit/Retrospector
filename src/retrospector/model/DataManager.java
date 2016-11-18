/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public class DataManager {
    
    public static ObservableList<Media> media = FXCollections.observableArrayList(
                new Media("The Imitation Game","",Media.Category.MOVIE,Media.Type.SINGLE, new ArrayList<Review>(){{
                        add(new Review(BigDecimal.valueOf(4.3)));
                        add(new Review(BigDecimal.valueOf(5.0)));
                        add(new Review(BigDecimal.valueOf(4.5)));
                }}),
                new Media("Ender's Game","",Media.Category.MOVIE,Media.Type.SINGLE, new ArrayList<Review>(){{
                        add(new Review(BigDecimal.valueOf(8.0),LocalDate.MAX));
                        add(new Review(BigDecimal.valueOf(7.6),LocalDate.MIN));
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
}

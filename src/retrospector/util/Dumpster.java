/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class Dumpster {
    
    public static final Integer earliestYear = 2005;
    public static final String[] users = new String[]{DataManager.getDefaultUser(),"Joe","Jan","Jim","Jordan"};
    public static final Integer numOfReviewDefault = 3;
//    public static final String[] users = new String[]{DataManager.getDefaultUser()};
    private static Random random = new Random();
    
    public static Review getRandomReview(){
        Review r = new Review();
        r.setRating(random.nextInt(10)+1);
        r.setUser(users[random.nextInt(users.length)]);
        r.setDate(LocalDate.of(random.nextInt(LocalDate.now().getYear()-earliestYear)+earliestYear,random.nextInt(12)+1,random.nextInt(28)+1));
        return r;
    }
    
    public static Media getRandomMedia(int numOfReviews){
        Media m = new Media();
        String[] uuid =UUID.randomUUID().toString().split("-");
        m.setTitle(uuid[0]);
        m.setCreator(uuid[1]);
        if(random.nextBoolean()){
            m.setEpisode(uuid[2]);
            if(random.nextBoolean()){
                m.setSeason(uuid[3]);
                m.setType(Media.Type.SERIES);
            }
            m.setType(Media.Type.MINISERIES);
        } else {
            m.setType(Media.Type.SINGLE);
        }
        m.setCategory(DataManager.getCategories()[random.nextInt(DataManager.getCategories().length)]);
        
        for (int i = 0; i < numOfReviews; i++) {
            m.getReviews().add(getRandomReview());
        }
        
        return m;
    }
    
    public static void createMedia(int numOfMedia){
        createMedia(numOfMedia,numOfReviewDefault);
    }
    
    public static void createMedia(int numOfMedia,int numOfReviewsEach){
        for (int i = 0; i < numOfMedia; i++) {
            DataManager.createDB(getRandomMedia(numOfReviewsEach));
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.dumpster;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import retrospector.model.DataManager;
import retrospector.model.Factoid;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class Dumpster {
    
    private Integer earliestYear = 2005;
    private String[] users = new String[]{DataManager.getDefaultUser(),"Joe","Jan","Jim","Jordan"};
    private Random random = new Random();
    private RandomString descriptionGenerator;
    private RandomString reviewStringGenerator;
    
    
    private List<Media> media;
    
    public Dumpster(int numOfMedia, int numOfReviews, int numOfFacts, int lenDescription, int lenReview) {
        descriptionGenerator = new RandomString(lenDescription);
        reviewStringGenerator = new RandomString(lenReview);
        
        media = new ArrayList<>();
        for (int i = 0; i < numOfMedia; i++) {
            Media m = getRandomMedia();
            for (int j = 0; j < numOfReviews; j++) {
                m.getReviews().add(getRandomReview());
            }
            for (int j = 0; j < numOfFacts; j++) {
                m.getFactoids().add(getRandomFactoid());
            }
            media.add(m);
        }
    }
    
    public Review getRandomReview(){
        Review r = new Review();
        r.setRating(getRating());
        r.setUser(users[random.nextInt(users.length)]);
        r.setDate(LocalDate.of(random.nextInt(LocalDate.now().getYear()-earliestYear)+earliestYear,random.nextInt(12)+1,random.nextInt(28)+1));
        r.setReview(reviewStringGenerator.nextString());
        return r;
    }
    
    public Factoid getRandomFactoid(){
        Factoid f = new Factoid();
        f.setTitle(DataManager.getFactiodTypes()[random.nextInt(DataManager.getFactiodTypes().length)]);
        f.setContent(UUID.randomUUID().toString());
        return f;
    }
    
    public Media getRandomMedia(){
        Media m = new Media();
        String[] uuid = UUID.randomUUID().toString().split("-");
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
        m.setDescription(descriptionGenerator.nextString());
        return m;
    }
    
    public void dump(){
        for (Media m : media) {
            DataManager.createDB(m);
        }
    }
    
    private int getNormal(int mean, int std, int min, int max) {
        int result = (int) random.nextGaussian()*std + mean;
        if (result < min)
            return min;
        if (result > max)
            return max;
        return result;
    }
    
    private int getRating() {
        return getNormal(6,2,1,10);
    }
}

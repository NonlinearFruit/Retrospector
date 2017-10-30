/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.text.DateFormatter;

/**
 *
 * @author nonfrt
 */
public class Review {
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    private LocalDate date;
    private String user;
    private String review;
    private Integer rating;
    private Integer mediaId;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Review(){
        this(DataManager.getDefaultRating());
    }
    
    public Review(Integer rating){
        this(rating, LocalDate.now());
    }
    
    public Review(Integer rating, LocalDate date){
        this(rating,date,DataManager.getDefaultUser());
    }
    
    public Review(Integer rating, LocalDate date, String user){
        this(rating,date,user,"");
    }
    
    public Review(Integer rating, LocalDate date, String user, String review){
        setRating(rating);
        setDate(date);
        setUser(user);
        setReview(review);
    }
    
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public void morphInto(Review r) {
        if (r.getRating() != null) {
            this.setRating(r.getRating());
        }
        if (r.getDate() != null) {
            this.setDate(r.getDate());
        }
        if (r.getUser() != null) {
            this.setUser(r.getUser());
        }
        if (r.getReview() != null) {
            this.setReview(r.getReview());
        }
    }
    
    @Override
    public String toString(){
        
        String rating = getRating()==null? "??": getRating().toString();
        String user = getUser()==null || getUser().equals("")? "??": getUser();
        String date = getDate()==null? "??": FORMATTER.format(getDate());
        
        return rating+" by "+user+" on "+date;
    }
    
    public void clone(Review review){
        setId(review.getId());
        setMediaId(review.getMediaId());
        setUser(review.getUser());
        setReview(review.getReview());
        setDate(review.getDate());
        setRating(review.getRating());
    }
}

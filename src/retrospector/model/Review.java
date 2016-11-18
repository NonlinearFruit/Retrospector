/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author nonfrt
 */
public class Review {
    private LocalDate date;
    private String user;
    private String review;
    private BigDecimal rating;

    public Review(){
        this(BigDecimal.valueOf(DataManager.getDefaultRating()));
    }
    
    public Review(BigDecimal rating){
        this(rating, LocalDate.now());
    }
    
    public Review(BigDecimal rating, LocalDate date){
        this(rating,date,DataManager.getDefaultUser());
    }
    
    public Review(BigDecimal rating, LocalDate date, String user){
        this(rating,date,user,"");
    }
    
    public Review(BigDecimal rating, LocalDate date, String user, String review){
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

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
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
        String date = getDate()==null? "??": getDate().toString();
        
        return rating+" by "+user+" on "+date;
    }
}

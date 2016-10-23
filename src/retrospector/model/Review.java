/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.time.LocalDate;

/**
 *
 * @author nonfrt
 */
public class Review {
    private LocalDate date;
    private String user;
    private String review;
    private Integer rating;

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
        String date = getDate()==null? "??": getDate().toString();
        
        return rating+" by "+user+" on "+date;
    }
}

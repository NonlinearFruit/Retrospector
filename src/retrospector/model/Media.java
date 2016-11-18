/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nonfrt
 */
public class Media {
    public static enum Category{MOVIE,TV_SERIES,BOOK,PODCAST,YOUTUBE,POEM,MUSIC,PRODUCT,OTHER}
    public static enum Type{MINISERIES,SERIES,SINGLE}
    
    private List<Review> reviews;
    private String title;
    private String description;
    private String creator;
    private Category category;
    private Type type;
    private String seasonId;
    private String episodeId;
    
    public Media(){
        this("");
    }
    
    public Media(String title){
        this(title,"");
    }
    
    public Media(String title, String creator){
        this(title, creator, Category.MOVIE);
    }
    
    public Media(String title, String creator, Category category){
        this(title, creator, category, Type.SINGLE);
    }
    
    public Media(String title, String creator, Category category, Type type){
        this(title, creator, category, type, new ArrayList<>());
    }
    
    public Media(String title, String creator, Category category, Type type, List<Review> reviews){
        setTitle(title);
        setCreator(creator);
        setCategory(category);
        setType(type);
        setReviews(reviews);
        setSeasonId("");
        setEpisodeId("");
        setDescription("");
    }

    public List<Review> getReviews() {
        return reviews;
    }

    private void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }
    
    public BigDecimal getAverageRating() {
        if(reviews.size()==0)
            return new BigDecimal(DataManager.getDefaultRating());
        BigDecimal mean = BigDecimal.ZERO;
        for (Review review : reviews) {
            if(review.getRating()!=null)
                mean = mean.add(review.getRating());
        }
        mean = mean.divide(BigDecimal.valueOf(reviews.size()),new MathContext(2, RoundingMode.HALF_UP));
        return mean;
    }
    
    @Override
    public String toString() {
        String title = getTitle()==null||getTitle().equals("")? "??": getTitle();
        String season = getSeasonId()==null||getSeasonId().equals("")? "": " "+getSeasonId();
        String episode = getEpisodeId()==null||getEpisodeId().equals("")? "": ": "+getEpisodeId();
        String creator = getCreator()==null||getCreator().equals("")? "??": getCreator();
        return title+season+episode+" by "+creator;
    }
}

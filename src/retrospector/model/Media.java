/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public class Media {
    // public static enum Category{MOVIE,TV_SERIES,BOOK,PODCAST,YOUTUBE,POEM,MUSIC,VIDEO_GAME,TABLETOP_GAME,PRODUCT,OTHER}
    public static enum Type{MINISERIES,SERIES,SINGLE}
    
    private List<Review> reviews;
    private String title;
    private String description;
    private String creator;
    private String category;
    private Type type;
    private String seasonId;
    private String episodeId;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Media(){
        this("");
    }
    
    public Media(String title){
        this(title,"");
    }
    
    public Media(String title, String creator){
        this(title, creator, PropertyManager.loadProperties().getCategories()[0]);
    }
    
    public Media(String title, String creator, String category){
        this(title, creator, category, Type.SINGLE);
    }
    
    public Media(String title, String creator, String category, Type type){
        this(title, creator, category, type, new ArrayList<>());
    }
    
    public Media(String title, String creator, String category, Type type, List<Review> reviews){
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
    
    /**
     * Takes the average of all the ratings.
     * @return 
     */
    public BigDecimal getAverageRating() {
        if(reviews.size()==0)
            return BigDecimal.ZERO;
        BigDecimal mean = BigDecimal.ZERO;
        for (Review review : reviews) {
            if(review.getRating()!=null)
                mean = mean.add(review.getRating());
        }
        mean = mean.divide(BigDecimal.valueOf(reviews.size()),new MathContext(2, RoundingMode.HALF_UP));
        return mean;
    }
    
    /**
     * Retrieves the default user's most recent rating of this media. If none
     * is found, then it returns a 0.
     * @return 
     */
    public BigDecimal getCurrentRating(){
        return
            getReviews().stream()
                .filter(r->DataManager.getDefaultUser().equals(r.getUser()))
                .sorted( (x,y) -> -1*Long.signum(x.getDate().toEpochDay()-y.getDate().toEpochDay()) )
                .findFirst()
                .orElse(new Review(BigDecimal.ZERO))
                .getRating();
    }
    
    /**
     * Retrieves the default user's first rating of this media. If none is
     * found, then it returns 0.
     * @return 
     */
    public BigDecimal getOriginalRating(){
        return
            getReviews().stream()
                .filter(r->DataManager.getDefaultUser().equals(r.getUser()))
                .sorted( (x,y) -> Long.signum(x.getDate().toEpochDay()-y.getDate().toEpochDay()) )
                .findFirst()
                .orElse(new Review(BigDecimal.ZERO))
                .getRating();
    }
    
    @Override
    public String toString() {
        String title = getTitle()==null||getTitle().equals("")? "??": getTitle();
        String season = getSeasonId()==null||getSeasonId().equals("")? "": " "+getSeasonId();
        String episode = getEpisodeId()==null||getEpisodeId().equals("")? "": ": "+getEpisodeId();
        String creator = getCreator()==null||getCreator().equals("")? "??": getCreator();
        return title+season+episode+" by "+creator;
    }
    
    public void clone(Media media){
        setId(media.getId());
        setTitle(media.getTitle());
        setCreator(media.getCreator());
        setSeasonId(media.getSeasonId());
        setEpisodeId(media.getEpisodeId());
        setDescription(media.getDescription());
        setCategory(media.getCategory());
        setType(media.getType());
        getReviews().clear();
        getReviews().addAll(media.getReviews());
    }
}

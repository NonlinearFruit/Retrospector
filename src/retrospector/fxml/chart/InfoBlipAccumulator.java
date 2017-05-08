/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import retrospector.model.Factoid;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class InfoBlipAccumulator {
    
    private int media;
    private int reviews;
    private int facts;
    private int users;
    private long days;
    private int titles;
    private int creators;
    private int singles;
    private int minis;
    private int series;
    private double perMonth;
    private double aveCurrent;
    private double aveAll;
    
    private Set<String> titleSet = new HashSet<>();
    private Set<String> creatorSet = new HashSet<>();
    private Set<String> userSet = new HashSet<>();
    private LocalDate earliest = LocalDate.now();
    private LocalDate latest = LocalDate.now();
        
    public void accumulate(Media aMedia){
        switch (aMedia.getType()) {
            case SINGLE:
                singles++;
                break;
            case MINISERIES:
                minis++;
                break;
            case SERIES:
                series++;
                break;
        }
        media++;
        aveCurrent += aMedia.getCurrentRating().intValue();
        titleSet.add(aMedia.getTitle() + aMedia.getCreator());
        creatorSet.add(aMedia.getCreator());
    }
    
    public void accumulate(Review aReview){
        if (aReview.getDate().isBefore(earliest)) {
            earliest = aReview.getDate();
        }
        if (aReview.getDate().isAfter(latest)) {
            latest = aReview.getDate();
        }
        reviews++;
        aveAll += aReview.getRating().intValue();
        userSet.add(aReview.getUser());
    }
    
    public void accumulate(Factoid aFact) {
        facts++;
    }
    
    private void calc(){
        titles = titleSet.size();
        creators = creatorSet.size();
        users = userSet.size();
        aveAll = reviews == 0 ? 0 : aveAll / reviews;
        aveCurrent = media == 0 ? 0 : aveCurrent / media;
        days = ChronoUnit.DAYS.between(earliest, LocalDate.now()) + 1;
        perMonth = days < 2 ? 0 : (media + 0.0) / days * 30;
    }
    
    public VBox getInfo(){
        // Setup
        VBox box = new VBox();
        
        calc();
        
        // Stats
        box.getChildren().addAll(
            new Text(media+" Media"),
            new Text(reviews+" Review(s)"),
            new Text(facts+" Fact(s)"),
            new Text(users+" User(s)"),  
            new Text(days+" Days"),
            new Text(titles + " Titles"),
            new Text(creators + " Creators"),
            new Text(singles+" Single(s)"),
            new Text(minis+" Mini(s)"),
            new Text(series+" Serie(s)"),
            new Text(String.format("%.2f", perMonth)+" / Month"),
            new Text(String.format("%.2f", aveCurrent)+" Current"),
            new Text(String.format("%.2f", aveAll)+" All")
        );
        
        return box;
    }

    public int getMedia() {
        return media;
    }

    public int getReviews() {
        return reviews;
    }
    
    public int getFacts() {
        return facts;
    }

    public int getUsers() {
        return users;
    }

    public long getDays() {
        return days;
    }

    public int getTitles() {
        return titles;
    }

    public int getCreators() {
        return creators;
    }

    public int getSingles() {
        return singles;
    }

    public int getMinis() {
        return minis;
    }

    public int getSeries() {
        return series;
    }

    public double getPerMonth() {
        return perMonth;
    }

    public double getAveCurrent() {
        return aveCurrent;
    }

    public double getAveAll() {
        return aveAll;
    }

    public LocalDate getEarliest() {
        return earliest;
    }
    
    
}

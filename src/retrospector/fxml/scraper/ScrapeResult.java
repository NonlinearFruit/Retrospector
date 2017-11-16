/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.scraper;

/**
 *
 * @author nonfrt
 */
public class ScrapeResult {
    
    public enum Type {Media,Series,Season,Episode}
    
    private Type type;
    private String display;
    private String identifier;

    public ScrapeResult(String display, String id) {
        this(display, id, Type.Media);
    }
    
    public ScrapeResult(String display, String id, Type type) {
        this.display = display;
        this.identifier = id;
        this.type = type;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public void setTypeMedia() {
        this.type = type.Media;
    }
    
    public boolean isMedia() {
        return this.type.equals(type.Media);
    }
    
    public void setTypeSeries() {
        this.type = type.Series;
    }
    
    public boolean isSeries() {
        return this.type.equals(type.Series);
    }
    
    public void setTypeSeason() {
        this.type = type.Season;
    }
    
    public boolean isSeason() {
        return this.type.equals(type.Season);
    }
    
    public void setTypeEpisode() {
        this.type = type.Episode;
    }
    
    public boolean isEpisode() {
        return this.type.equals(type.Episode);
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
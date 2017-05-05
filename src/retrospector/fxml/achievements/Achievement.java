/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.util.function.Supplier;
import retrospector.util.JohnnyCache;

/**
 *
 * @author nonfrt
 */
public class Achievement {
    private String symbol;
    private String title;
    private String description;
    private JohnnyCache<Integer> unlocked;
    private Integer tier;

    public Achievement(String symbol) {
        this(symbol,"");
    }
    
    public Achievement(String symbol, String title) {
        this(symbol,title,"");
    }
    
    public Achievement(String symbol, String title, String description) {
        this(symbol, title, description, 1);
    }
    
    public Achievement(String symbol, String title, String description, Integer tier) {
        this(symbol, title, description, tier, ()->0);
    }
    
    public Achievement(String symbol, String title, String description, Integer tier, Supplier<Integer> unlocked) {
        this.symbol = symbol;
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.unlocked = new JohnnyCache(unlocked);
    }
    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public Boolean isUnlocked() {
        return getProgress()>=100;
    }
    
    public Integer getProgress() {
        return unlocked.getValue();
    }

    public void setUnlocked(Supplier<Integer> unlocked) {
        this.unlocked = new JohnnyCache(unlocked);
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }
    
}

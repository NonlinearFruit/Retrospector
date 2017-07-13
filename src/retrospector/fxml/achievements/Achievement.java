/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

/**
 *
 * @author nonfrt
 */
public class Achievement {
    
    public static final Integer MAX_PROGRESS = 100;
    
    public static Integer scaleToFit(Integer current, Integer goal) {
        return (int)(current*1.0/goal*MAX_PROGRESS);
    }
    
    private static String clean(String x) {
        return x.toLowerCase();
    }
    
    public static boolean isMatch(String x, String y) {
        return clean(x).equals(clean(y));
    }
    
    public static boolean isContained(String container, String containee) {
        return clean(container).contains(clean(containee));
    }
    
    private String symbol;
    private String title;
    private String description;
    private String hint;
    private Integer progress;
    private Integer tier;
    private boolean showable;

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
        this(symbol, title, description, tier, 0);
    }
    
    public Achievement(String symbol, String title, String description, Integer tier, Integer progress) {
        this.symbol = symbol;
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.progress = progress;
        this.showable = true;
        this.hint = "?";
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer unlockProgress) {
        this.progress = unlockProgress;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public boolean isShowable() {
        return showable;
    }

    public void setShowable(boolean showable) {
        this.showable = showable;
    }
    
    public boolean isUnlocked() {
        return getProgress()>=MAX_PROGRESS;
    }
}

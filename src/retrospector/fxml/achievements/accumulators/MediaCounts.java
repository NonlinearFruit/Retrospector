/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.fxml.chart.StatsTabController;
import retrospector.model.DataManager;
import retrospector.model.Media;

/**
 *
 * @author nonfrt
 */
public class MediaCounts extends Accumulator<Media>{

    // Smallest
    private Achievement renaissance;
    private Achievement rounded;
    private Achievement jack;
    
    // Largest
    private Achievement academic;
    private Achievement enthusiast;
    private Achievement dabbler;
            
    // Total
    private Achievement connoisseur;
    private Achievement collector;
    private Achievement hobbiest;
    
    // Types
    private Achievement diversify;
    private Achievement spectrum;
    
    // Vars
    private Map<String,Integer> mediaCount;
    private Integer mediaTotal;
    private Integer categoryThreshold;
    
    public MediaCounts() {
        String hint = "Master of none";
        renaissance = new Achievement("","Renaissance Man","1,000 media in each category",1);
        renaissance.setHint(hint);
        rounded = new Achievement("","Well Rounded","100 media in each category",2);
        rounded.setHint(hint);
        jack = new Achievement("","Jack of All Trades","10 media in each category",3);
        jack.setHint(hint);
        
        hint = "Specialize";
        academic = new Achievement("","Academic","Category with 10,000 media",1);
        academic.setHint(hint);
        enthusiast = new Achievement("","Enthusiast","Category with 1,000 media",2);
        enthusiast.setHint(hint);
        dabbler = new Achievement("","Dabbler","Category with 100 media",3);
        dabbler.setHint(hint);
        
        hint = "Gotta catch them all";
        connoisseur = new Achievement("","Connoisseur","Collect 10,000 media",1);
        connoisseur.setHint(hint);
        collector = new Achievement("","Collector","Collect 1,000 media",2);
        collector.setHint(hint);
        hobbiest = new Achievement("","Hobbyist","Collect 100 media",3);
        hobbiest.setHint(hint);
        
        spectrum = new Achievement("","Spectrum","Have more categories than colors",1);
        spectrum.setShowable(false);
        hint = "Scattergories";
        diversify = new Achievement("","Diversify","Have 5 categories",3);
        diversify.setHint(hint);
        
        mediaTotal = 0;
        categoryThreshold = 5;
        mediaCount = new HashMap<>();
        for (String category : DataManager.getCategories()) {
            mediaCount.put(category, 0);
        }
    }
    
    @Override
    public void accumulate(Media item) {
        Long defaultReviews = item.getReviews()
                .stream()
                .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                .count();
        
        Integer value = mediaCount.getOrDefault(item.getCategory(), 0);
        value += defaultReviews>0? 1:0;
        mediaCount.put(item.getCategory(), value);

        mediaTotal += defaultReviews>0? 1:0;
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        Integer largest = Collections.max(mediaCount.values());
        Integer smallest = Collections.min(mediaCount.values());
        Integer numCategories = DataManager.getCategories().length;
        if (numCategories < categoryThreshold)
            smallest = 0;
        
        renaissance.setProgress(Achievement.scaleToFit(smallest, 1000));
        rounded.setProgress(Achievement.scaleToFit(smallest, 100));
        jack.setProgress(Achievement.scaleToFit(smallest, 10));
        
        academic.setProgress(Achievement.scaleToFit(largest, 10000));
        enthusiast.setProgress(Achievement.scaleToFit(largest, 1000));
        dabbler.setProgress(Achievement.scaleToFit(largest, 100));
        
        connoisseur.setProgress(Achievement.scaleToFit(mediaTotal, 10000));
        collector.setProgress(Achievement.scaleToFit(mediaTotal, 1000));
        hobbiest.setProgress(Achievement.scaleToFit(mediaTotal, 100));
        
        spectrum.setProgress(Achievement.scaleToFit(numCategories, StatsTabController.colors.length+1));
        diversify.setProgress(Achievement.scaleToFit(numCategories, 5));
        
        return super.getShowableAchievements(
                connoisseur, collector, hobbiest, 
                renaissance, rounded, jack, 
                academic, enthusiast, dabbler,
                spectrum, diversify
        );
    }
}

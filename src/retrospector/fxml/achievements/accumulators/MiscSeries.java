/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.DataManager;
import retrospector.model.Media;

/**
 *
 * @author nonfrt
 */
public class MiscSeries extends Accumulator<Media>{

    private Achievement inconsistent;
    private Achievement masterpiece;
    
    // Category -> Title -> Ratings
    private Map<String,Map<String,List<Integer>>> mapRatings;
    // Category -> Title -> Count
    public Map<String,Map<String,Integer>> mapCounts;
    
    public MiscSeries() {
        inconsistent = new Achievement("","Inconsistent","Title with a 1 and 10 rating",3);
        inconsistent.setShowable(false);
        masterpiece = new Achievement("","True Masterpiece","Title with ten 10 ratings",1);
        masterpiece.setShowable(false);
        
        mapRatings = new HashMap<>();
        mapCounts = new HashMap<>();
    }
    
    @Override
    public void accumulate(Media item) {
        String title = item.getTitle();
        String category = item.getCategory();
        List<Integer> ratings = item.getReviews().stream()
                .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                .map(r->r.getRating())
                .collect(Collectors.toList());
        
        if (!mapRatings.containsKey(category))
            mapRatings.put(category,new HashMap<>());
        ratings.addAll(
                mapRatings.get(category).getOrDefault(title, new ArrayList<>())
        );
        mapRatings.get(category).put(title,ratings);
        
        if (!mapCounts.containsKey(category))
            mapCounts.put(category,new HashMap<>());
        Integer count = mapCounts.get(category).getOrDefault(title, 0);
        count += 1;
        mapCounts.get(category).put(title,count);
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        boolean found = mapRatings.values().stream()
                .filter(m->m.values().contains(1))
                .anyMatch(m->m.values().contains(10));
        inconsistent.setProgress(found? 100:0);
        
        found = mapRatings.values().stream()
                .filter(m->Collections.frequency(m.values(), 10)>=10)
                .count() > 0;
        masterpiece.setProgress(found? 100:0);
        return super.getShowableAchievements(inconsistent, masterpiece);
    }
    
}

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
    
    // Title -> Creator -> Ratings
    private Map<String,Map<String,List<Integer>>> map;
    
    public MiscSeries() {
        inconsistent = new Achievement("","Inconsistent","Title with a 1 and 10 rating",3);
        inconsistent.setShowable(false);
        masterpiece = new Achievement("","True Masterpiece","Title with ten 10 ratings",2);
        masterpiece.setShowable(false);
        
        map = new HashMap<>();
    }
    
    @Override
    public void accumulate(Media item) {
        String title = item.getTitle();
        String creator = item.getCreator();
        List<Integer> ratings = item.getReviews().stream()
                .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                .map(r->r.getRating().intValueExact())
                .collect(Collectors.toList());
        
        if (!map.containsKey(title))
            map.put(title,new HashMap<>());
        ratings.addAll(
                map.get(title).getOrDefault(creator, new ArrayList<>())
        );
        map.get(title).put(creator,ratings);
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        boolean found = map.values().stream()
                .filter(m->m.values().contains(1))
                .anyMatch(m->m.values().contains(10));
        inconsistent.setProgress(found? 100:0);
        
        found = map.values().stream()
                .filter(m->Collections.frequency(m.values(), 10)>=10)
                .count() > 0;
        masterpiece.setProgress(found? 100:0);
        return super.getShowableAchievements(inconsistent, masterpiece);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class MediaPerDay extends Accumulator<Media>{

    private Achievement spree;
    private Achievement binge;
    private Achievement marathon;
    
    // Day -> Category -> # Reviews
    Map<LocalDate,Map<String,Integer>> categoryMap;
    // Day -> User -> # Reviews
    Map<LocalDate,Map<String,Integer>> userMap;
    
    public MediaPerDay() {
        categoryMap = new HashMap<>();
        userMap = new HashMap<>();
        
        spree = new Achievement("", "Spree", "5 categories in a day", 1);
        spree.setShowable(false);
        binge = new Achievement("", "Binge", "10 media in 1 category in a day", 1);
        binge.setShowable(false);
        marathon = new Achievement("", "Marathon", "5 users review 5 media in a day", 1);
        marathon.setShowable(false);
    }
    
    @Override
    public void accumulate(Media item) {
        String category = item.getCategory();
        for (Review review : item.getReviews()) {
            String user = review.getUser();
            LocalDate date = review.getDate();
            Integer value;
            
            if (!categoryMap.containsKey(date))
                categoryMap.put(date, new HashMap<>());
            value = categoryMap.get(date).getOrDefault(category, 0);
            value++;
            categoryMap.get(date).put(category, value);
            
            if (!userMap.containsKey(date))
                userMap.put(date, new HashMap<>());
            value = userMap.get(date).getOrDefault(user, 0);
            value++;
            userMap.get(date).put(user, value);
        }
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        // Maximum number of categories reviewed in 1 day
        Integer spreeSize = categoryMap.values().stream()
                .mapToInt(m->m.values().size())
                .max()
                .orElse(0);
        // Maximum number of media reviewed 
        // in a single category in a single day
        Integer bingeSize = categoryMap.values().stream()
                .flatMapToInt(m->m.values().stream().mapToInt(x->x))
                .max()
                .orElse(0);
        // Check if a marathon exists
        boolean haveMarathon = userMap.values().stream()
                .anyMatch(m->
                        m.keySet().size()>=5 && 
                        m.values().stream().filter(x-> x>=5).count()>=5
                );
        
        spree.setProgress(spreeSize>=5? 100:0);
        binge.setProgress(bingeSize>=10? 100:0);
        marathon.setProgress(haveMarathon? 100:0);
        
        return super.getShowableAchievements(spree, binge, marathon);
    }
    
}

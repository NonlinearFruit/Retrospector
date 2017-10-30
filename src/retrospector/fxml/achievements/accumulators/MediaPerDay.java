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
    
    // High Scores
    private Achievement topScoreGold;
    private Achievement topScoreSilver;
    private Achievement topScoreBronze;
    
    private Achievement lowScoreGold;
    private Achievement lowScoreSilver;
    private Achievement lowScoreBronze;
    
    // Day -> Category -> # Reviews
    private Map<LocalDate,Map<String,Integer>> categoryMap;
    // Day -> User -> # Reviews
    private Map<LocalDate,Map<String,Integer>> userMap;
    // Category -> Month -> # Reviews
    public Map<String, Map<LocalDate,Integer>> monthMap;
    
    public MediaPerDay() {
        categoryMap = new HashMap<>();
        userMap = new HashMap<>();
        monthMap = new HashMap<>();
        
        spree = new Achievement("", "Spree", "5 categories in a day", 1);
        spree.setShowable(false);
        binge = new Achievement("", "Binge", "10 media in 1 category in a day", 1);
        binge.setShowable(false);
        marathon = new Achievement("", "Marathon", "5 users review 5 media in a day", 1);
        marathon.setShowable(false);
        
        topScoreGold = new Achievement("", "Hardcore Gamer", "High score over 250", 1);
        topScoreGold.setHint("Top the charts");
        topScoreSilver = new Achievement("", "Die-Hard Fan", "High score over 100", 2);
        topScoreSilver.setHint("Top the charts");
        topScoreBronze = new Achievement("", "Peerless", "High score over 50", 3);
        topScoreBronze.setHint("Top the charts");
        
        lowScoreGold = new Achievement("", "Valedictorian", "All high scores over 100", 1);
        lowScoreGold.setHint("Don't accept 2nd place");
        lowScoreSilver = new Achievement("", "Salutatorian", "All high scores over 50", 2);
        lowScoreSilver.setHint("Don't accept 2nd place");
        lowScoreBronze = new Achievement("", "Top Marks", "All high scores over 10", 3);
        lowScoreBronze.setHint("Don't accept 2nd place");
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
            
            if (!monthMap.containsKey(category))
                monthMap.put(category, new HashMap<>());
            value = monthMap.get(category).getOrDefault(date.withDayOfMonth(1), 0);
            value++;
            monthMap.get(category).put(date.withDayOfMonth(1), value);
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
        
        // High score stuff
        Integer maxScore = monthMap.values().stream()
                .mapToInt(m->m.values().stream().mapToInt(x->x).max().orElse(0))
                .max()
                .orElse(0);
        Integer minScore = monthMap.values().stream()
                .mapToInt(m->m.values().stream().mapToInt(x->x).max().orElse(0))
                .min()
                .orElse(0);
        
        System.out.println(maxScore+" max:min "+minScore);
        
        spree.setProgress(spreeSize>=5? 100:0);
        binge.setProgress(bingeSize>=10? 100:0);
        marathon.setProgress(haveMarathon? 100:0);
        
        topScoreGold.setProgress(Achievement.scaleToFit(maxScore, 250));
        topScoreSilver.setProgress(Achievement.scaleToFit(maxScore, 100));
        topScoreBronze.setProgress(Achievement.scaleToFit(maxScore, 50));

        lowScoreGold.setProgress(Achievement.scaleToFit(minScore, 100));
        lowScoreSilver.setProgress(Achievement.scaleToFit(minScore, 50));
        lowScoreBronze.setProgress(Achievement.scaleToFit(minScore, 10));
        
        return super.getShowableAchievements(
                spree, binge, marathon,
                topScoreGold, topScoreSilver, topScoreBronze,
                lowScoreGold, lowScoreSilver, lowScoreBronze);
    }
    
}

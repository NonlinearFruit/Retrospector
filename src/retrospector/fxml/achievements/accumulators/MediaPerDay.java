/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.fxml.achievements.AchievementFactory;
import retrospector.fxml.achievements.tables.MediaStreak;
import retrospector.model.DataManager;
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
    
    // Media Streaks
    private Achievement topStreakGold;
    private Achievement topStreakSilver;
    private Achievement topStreakBronze;
    
    private Achievement lowStreakGold;
    private Achievement lowStreakSilver;
    private Achievement lowStreakBronze;
    
    // Day -> Category -> # Reviews
    public Map<LocalDate,Map<String,Integer>> categoryMap;
    // Day -> User -> # Reviews
    private Map<LocalDate,Map<String,Integer>> userMap;
    // Category -> Month -> # Reviews
    public Map<String, Map<LocalDate,Integer>> monthMap;
    // Category -> Media Streak
    public Map<String, MediaStreak> mediaStreaks;
    
    public MediaPerDay() {
        String hint = "";
        categoryMap = new HashMap<>();
        userMap = new HashMap<>();
        monthMap = new HashMap<>();
        mediaStreaks = new HashMap<>();
        
        spree = new Achievement("", "Spree", "5 categories in a day", 1);
        spree.setShowable(false);
        binge = new Achievement("", "Binge", "10 media in 1 category in a day", 1);
        binge.setShowable(false);
        marathon = new Achievement("", "Marathon", "5 users review 5 media in a day", 1);
        marathon.setShowable(false);
        
        hint = "Top the charts";
        topScoreGold = new Achievement("", "Hardcore Gamer", "High score over 120", 1);
        topScoreGold.setHint(hint);
        topScoreSilver = new Achievement("", "Die-Hard Fan", "High score over 90", 2);
        topScoreSilver.setHint(hint);
        topScoreBronze = new Achievement("", "Peerless", "High score over 60", 3);
        topScoreBronze.setHint(hint);
        
        hint = "Don't accept 2nd place";
        lowScoreGold = new Achievement("", "Valedictorian", "All high scores over 30", 1);
        lowScoreGold.setHint(hint);
        lowScoreSilver = new Achievement("", "Salutatorian", "All high scores over 20", 2);
        lowScoreSilver.setHint(hint);
        lowScoreBronze = new Achievement("", "Top Marks", "All high scores over 10", 3);
        lowScoreBronze.setHint(hint);
        
        hint = "Everyday same old same old";
        topStreakGold = new Achievement("", "Rampage", "Media streak over 28", 1);
        topStreakGold.setHint(hint);
        topStreakSilver = new Achievement("", "Frenzy", "Media streak over 21", 2);
        topStreakSilver.setHint(hint);
        topStreakBronze = new Achievement("", "Spree", "Media streak over 14", 3);
        topStreakBronze.setHint(hint);
        
        hint = "No streak gets left behind!";
        lowStreakGold = new Achievement("", "Fast Forward", "All media streaks over 14", 1);
        lowStreakGold.setHint(hint);
        lowStreakSilver = new Achievement("", "Double Time", "All media streaks over 7", 2);
        lowStreakSilver.setHint(hint);
        lowStreakBronze = new Achievement("", "Triplets", "All media streaks over 3", 3);
        lowStreakBronze.setHint(hint);
        
    }
    
    @Override
    public void accumulate(Media item) {
        String category = item.getCategory();
        for (Review review : item.getReviews()) {
            String user = review.getUser();
            LocalDate date = review.getDate();
            Integer value;
            
            if (user.equals(DataManager.getDefaultUser())) {
                if (!categoryMap.containsKey(date))
                    categoryMap.put(date, new HashMap<>());
                value = categoryMap.get(date).getOrDefault(category, 0);
                value++;
                categoryMap.get(date).put(category, value);

                if (!monthMap.containsKey(category))
                    monthMap.put(category, new HashMap<>());
                value = monthMap.get(category).getOrDefault(date.withDayOfMonth(1), 0);
                value++;
                monthMap.get(category).put(date.withDayOfMonth(1), value);
            }
            
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
        
        // High score stuff
        Integer maxScore = monthMap.values().stream()
                .mapToInt(m->m.values().stream().mapToInt(x->x).max().orElse(0))
                .max()
                .orElse(0);
        Integer minScore = monthMap.values().stream()
                .mapToInt(m->m.values().stream().mapToInt(x->x).max().orElse(0))
                .min()
                .orElse(0);
        
        // Media streak stuff
        Integer minStreak = 14;
        Integer maxStreak = 0;
        for (String category : DataManager.getCategories()) {
            MediaStreak streak = AchievementFactory.getMediaStreak(category, categoryMap);
            mediaStreaks.put(category, streak);
            int size = streak.getDays();
            if (size > maxStreak)
                maxStreak = size;
            if (size < minStreak)
                minStreak = size;
        }
        
        
        spree.setProgress(spreeSize>=5? 100:0);
        binge.setProgress(bingeSize>=10? 100:0);
        marathon.setProgress(haveMarathon? 100:0);
        
        topScoreGold.setProgress(Achievement.scaleToFit(maxScore, 120));
        topScoreSilver.setProgress(Achievement.scaleToFit(maxScore, 90));
        topScoreBronze.setProgress(Achievement.scaleToFit(maxScore, 60));

        lowScoreGold.setProgress(Achievement.scaleToFit(minScore, 30));
        lowScoreSilver.setProgress(Achievement.scaleToFit(minScore, 20));
        lowScoreBronze.setProgress(Achievement.scaleToFit(minScore, 10));
        
        topStreakGold.setProgress(Achievement.scaleToFit(maxStreak, 28));
        topStreakSilver.setProgress(Achievement.scaleToFit(maxStreak, 21));
        topStreakBronze.setProgress(Achievement.scaleToFit(maxStreak, 14));

        lowStreakGold.setProgress(Achievement.scaleToFit(minStreak, 14));
        lowStreakSilver.setProgress(Achievement.scaleToFit(minStreak, 7));
        lowStreakBronze.setProgress(Achievement.scaleToFit(minStreak, 3));
        
        return super.getShowableAchievements(
                spree, binge, marathon,
                topScoreGold, topScoreSilver, topScoreBronze,
                lowScoreGold, lowScoreSilver, lowScoreBronze,
                topStreakGold, topStreakSilver, topStreakBronze,
                lowStreakGold, lowStreakSilver, lowStreakBronze);
    }
    
}

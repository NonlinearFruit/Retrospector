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
import retrospector.model.DataManager;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class ReviewsPerDay extends Accumulator<Review>{

    // 1/day
    private Achievement fanatic;
    private Achievement obsessed;
    private Achievement hooked;
    private Integer smallThreshold = 1;
            
    // 10/day
    private Achievement unemployed;
    private Achievement vacation;
    private Achievement weekend;
    private Integer largeThreshold = 10;
    
    private Map<LocalDate,Integer> reviewsPerDay;
    
    public ReviewsPerDay() {
        String hint = "Keep the doctor away";
        fanatic = new Achievement("","Fanatic","1 review/day for 3 months",1);
        fanatic.setHint(hint);
        obsessed = new Achievement("","Obsessed","1 review/day for 2 months",2);
        obsessed.setHint(hint);
        hooked = new Achievement("","Hooked","1 review/day for a month",3);
        hooked.setHint(hint);
        
        hint = "Can't stop";
        unemployed = new Achievement("","Unemployed","10 review/day for 20 days",1);
        unemployed.setHint(hint);
        vacation = new Achievement("","Vacation","10 review/day for a week",2);
        vacation.setHint(hint);
        weekend = new Achievement("","Weekend","10 review/day for 3 days",3);
        weekend.setHint(hint);
        
        reviewsPerDay = new HashMap<>();
    }
    
    @Override
    public void accumulate(Review item) {
        if (item.getUser().equals(DataManager.getDefaultUser())) {
            Integer value = reviewsPerDay.getOrDefault(item.getDate(), 0);
            value++;
            reviewsPerDay.put(item.getDate(), value);
        }
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        Integer smallLength = AchievementFactory.getLongestConsecutiveDays(smallThreshold, reviewsPerDay);
        Integer smallCurrentLength = AchievementFactory.getLongestConsecutiveFromToday(smallThreshold, reviewsPerDay);
        Integer largeLength = AchievementFactory.getLongestConsecutiveDays(largeThreshold, reviewsPerDay);
        Integer largeCurrentLength = AchievementFactory.getLongestConsecutiveFromToday(largeThreshold, reviewsPerDay);
        
        fanatic.setProgress(Achievement.scaleToFit(smallLength, 90));
        if (!fanatic.isUnlocked())
            fanatic.setProgress(Achievement.scaleToFit(smallCurrentLength, 90));
        obsessed.setProgress(Achievement.scaleToFit(smallLength, 60));
        if (!obsessed.isUnlocked())
            obsessed.setProgress(Achievement.scaleToFit(smallCurrentLength, 60));
        hooked.setProgress(Achievement.scaleToFit(smallLength, 30));
        if (!hooked.isUnlocked())
            hooked.setProgress(Achievement.scaleToFit(smallCurrentLength, 30));
        
        unemployed.setProgress(Achievement.scaleToFit(largeLength, 20));
        if (!unemployed.isUnlocked())
            unemployed.setProgress(Achievement.scaleToFit(largeCurrentLength, 20));
        vacation.setProgress(Achievement.scaleToFit(largeLength, 7));
        if (!vacation.isUnlocked())
            vacation.setProgress(Achievement.scaleToFit(largeCurrentLength, 7));
        weekend.setProgress(Achievement.scaleToFit(largeLength, 3));
        if (!weekend.isUnlocked())
            weekend.setProgress(Achievement.scaleToFit(largeCurrentLength, 3));
        
        return super.getShowableAchievements(fanatic, obsessed, hooked, unemployed, vacation, weekend);
    }
    
}

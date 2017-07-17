/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import retrospector.fxml.achievements.accumulators.UserCounts;
import retrospector.fxml.achievements.accumulators.MediaCounts;
import retrospector.fxml.achievements.accumulators.EarliestReview;
import retrospector.fxml.achievements.accumulators.FactoidCounts;
import retrospector.fxml.achievements.accumulators.FileSystem;
import retrospector.fxml.achievements.accumulators.Github;
import retrospector.fxml.achievements.accumulators.LongReview;
import retrospector.fxml.achievements.accumulators.MediaPerDay;
import retrospector.fxml.achievements.accumulators.MiscMedia;
import retrospector.fxml.achievements.accumulators.MiscSeries;
import retrospector.fxml.achievements.accumulators.MiscTotals;
import retrospector.fxml.achievements.accumulators.MultipleReviews;
import retrospector.fxml.achievements.accumulators.Ratings;
import retrospector.fxml.achievements.accumulators.ReviewsPerDay;
import retrospector.fxml.achievements.accumulators.RockPaperScissors;
import retrospector.model.DataManager;
import retrospector.model.Factoid;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class AchievementFactory {

    public static List<AchievementFX> getAchievements() {
        List<Accumulator<Object>> otherAccumulators = Arrays.asList(
                new Github(),
                new FileSystem()
        );
        List<Accumulator<Media>> mediaAccumulators = Arrays.asList(
                new MiscSeries(),
                new MediaPerDay(),
                new MiscMedia(),
                new RockPaperScissors(),
                new MediaCounts(),
                new MultipleReviews(),
                new MiscTotals()
        );
        List<Accumulator<Review>> reviewAccumulators = Arrays.asList(
                new Ratings(),
                new EarliestReview(),
                new ReviewsPerDay(),
                new LongReview(),
                new UserCounts()
        );
        List<Accumulator<Factoid>> factoidAccumulators = Arrays.asList(
                new FactoidCounts()
        );
        
        for (Media media : DataManager.getMedia()) {
            for (Accumulator<Media> accumulator : mediaAccumulators) {
                accumulator.accumulate(media);
            }
            for (Review review : media.getReviews()) {
                for (Accumulator<Review> accumulator : reviewAccumulators) {
                    accumulator.accumulate(review);
                }
            }
            for (Factoid factoid : media.getFactoids()) {
                for (Accumulator<Factoid> accumulator : factoidAccumulators) {
                    accumulator.accumulate(factoid);
                }
            }
        }
        for (Accumulator accumulator : otherAccumulators) {
            accumulator.accumulate(null);
        }
        List<AchievementFX> achievements = new ArrayList<>();
        achievements.addAll(otherAccumulators.stream().flatMap(a->a.getShowableAchievements().stream()).collect(Collectors.toList()));
        achievements.addAll(mediaAccumulators.stream().flatMap(a->a.getShowableAchievements().stream()).collect(Collectors.toList()));
        achievements.addAll(reviewAccumulators.stream().flatMap(a->a.getShowableAchievements().stream()).collect(Collectors.toList()));
        achievements.addAll(factoidAccumulators.stream().flatMap(a->a.getShowableAchievements().stream()).collect(Collectors.toList()));
        return achievements;
    }
    
    public static Integer getLongestConsecutiveDays(Integer reviewThreshold, Map<LocalDate,Integer> map) {
        
        int maxLen = 0;
        for (LocalDate today : map.keySet()) {
            if (map.get(today)<reviewThreshold)
                continue;
            
            LocalDate yesterday = today.minus(1,ChronoUnit.DAYS);
            if (!map.containsKey(yesterday) || map.get(yesterday)<reviewThreshold)
            {
                // Then check for next elements in the
                // sequence
                int currentLen = 1;
                LocalDate tomorrow = today.plus(1,ChronoUnit.DAYS);
                while (map.getOrDefault(tomorrow, 0)>=reviewThreshold) {
                    tomorrow = tomorrow.plus(1,ChronoUnit.DAYS);
                    currentLen++;
                }
 
                // update  optimal length if this length
                // is more
                if (maxLen<currentLen)
                    maxLen = currentLen;
            }
        }
        return maxLen;
    }
    
    public static Integer getLongestConsecutiveFromToday(Integer reviewThreshold, Map<LocalDate,Integer> map) {
        LocalDate today = LocalDate.now();
        int currentLen = 1;
        LocalDate yesterday = today.minus(1,ChronoUnit.DAYS);
        while (map.getOrDefault(yesterday, 0)>=reviewThreshold) {
            yesterday = yesterday.minus(1,ChronoUnit.DAYS);
            currentLen++;
        }
        return currentLen;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class EarliestReview extends Accumulator<Review> {

    private Achievement apprentice;
    private Achievement journeyman;
    private Achievement master;
    private LocalDate earliest;
    
    public EarliestReview() {
        earliest = LocalDate.now();
        String hint = "Give it time";
        
        apprentice = new Achievement("","Apprentice","Retrospect for 1 year",3);
        apprentice.setHint(hint);
        journeyman = new Achievement("","Journeyman","Retrospect for 5 years",2);
        journeyman.setHint(hint);
        master = new Achievement("","Master","Retrospect for 10 years",1);
        master.setHint(hint);
    }
    
    @Override
    public void accumulate(Review item) {
        if (item.getDate().isBefore(earliest))
            earliest = item.getDate();
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        LocalDate now = LocalDate.now();
        Integer progress = Math.toIntExact(ChronoUnit.DAYS.between(earliest, now));
        
        apprentice.setProgress(Achievement.scaleToFit(progress,365));
        journeyman.setProgress(Achievement.scaleToFit(progress,365*5));
        master.setProgress(Achievement.scaleToFit(progress,365*10));
        
        return super.getShowableAchievements(master, journeyman, apprentice);
    }
    
}

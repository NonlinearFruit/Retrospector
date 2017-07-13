/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.util.List;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.DataManager;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class LongReview extends Accumulator<Review>{

    private Achievement scholarly;
    private Achievement thoughtful;
    private Achievement wordy;
    
    private Integer threshold;
    private Integer count;
    
    public LongReview() {
        count = 0;
        threshold = 1000;
        String hint = "But what do you really think?";
        
        scholarly = new Achievement("","Scholarly","Give 100 looong reviews",1);
        scholarly.setHint(hint);
        thoughtful = new Achievement("","Thoughtful","Give 10 looong reviews",2);
        thoughtful.setHint(hint);
        wordy = new Achievement("","Wordy","Give a looong review",3);
        wordy.setHint(hint);
    }
    
    @Override
    public void accumulate(Review item) {
        if (item.getUser().equals(DataManager.getDefaultUser()) && item.getReview().length()>=threshold)
            count++;
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        scholarly.setProgress(Achievement.scaleToFit(count, 100));
        thoughtful.setProgress(Achievement.scaleToFit(count, 10));
        wordy.setProgress(Achievement.scaleToFit(count, 1));
        
        return super.getShowableAchievements(scholarly, thoughtful, wordy);
    }
    
}

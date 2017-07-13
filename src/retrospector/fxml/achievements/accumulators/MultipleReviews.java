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
import retrospector.model.Media;

/**
 *
 * @author nonfrt
 */
public class MultipleReviews extends Accumulator<Media>{

    private Achievement iconic;
    private Achievement classic;
    private Achievement favorite;
    private Integer maxReviewsByDefaultUser;
    
    public MultipleReviews() {
        String hint = "Double dipping isn't always bad";
        
        iconic = new Achievement("","Iconic","Review 1 media 20 times",1);
        iconic.setHint(hint);
        classic = new Achievement("","Classic","Review 1 media 10 times",2);
        classic.setHint(hint);
        favorite = new Achievement("","Favorite","Review 1 media 5 times",3);
        favorite.setHint(hint);
        
        maxReviewsByDefaultUser = 0;
    }
    
    @Override
    public void accumulate(Media item) {
        Integer count = reviewsByDefaultUser(item);
        if (count>maxReviewsByDefaultUser)
            maxReviewsByDefaultUser = count;
    }
    
    private Integer reviewsByDefaultUser(Media item) {
        return Math.toIntExact(item.getReviews().stream()
                .filter(m->m.getUser().equals(DataManager.getDefaultUser()))
                .count());
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        iconic.setProgress(Achievement.scaleToFit(maxReviewsByDefaultUser, 20));
        classic.setProgress(Achievement.scaleToFit(maxReviewsByDefaultUser, 10));
        favorite.setProgress(Achievement.scaleToFit(maxReviewsByDefaultUser, 5));
        
        return super.getShowableAchievements(iconic, classic, favorite);
    }
}

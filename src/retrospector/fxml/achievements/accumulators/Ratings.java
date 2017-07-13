/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.math.BigDecimal;
import java.util.List;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class Ratings extends Accumulator<Review> {

    private Achievement neverAgain;
    
    private boolean oneStarFound;
    public Ratings() {
        neverAgain = new Achievement("","Never Again","Give a 1 star review",3);
        neverAgain.setShowable(false);
        
        oneStarFound = false;
    }
    
    @Override
    public void accumulate(Review item) {
        if (item.getRating().equals(BigDecimal.ONE))
            oneStarFound = true;
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        neverAgain.setProgress(oneStarFound? 100:0);

        return super.getShowableAchievements(neverAgain);
    }
    
}

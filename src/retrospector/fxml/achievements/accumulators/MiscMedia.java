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
import retrospector.model.Media;

/**
 *
 * @author nonfrt
 */
public class MiscMedia extends Accumulator<Media> {
    
    private Achievement starwars;
    
    private boolean starwarsFound;
    
    public MiscMedia() {
        starwars = new Achievement("","Star Wars","Star Wars in a title",3);
        starwars.setShowable(false);
        
        starwarsFound = false;
    }

    @Override
    public void accumulate(Media item) {
        if (Achievement.isContained(item.getTitle(), "Star Wars"))
            starwarsFound = true;
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        starwars.setProgress(starwarsFound?100:0);
        
        return super.getShowableAchievements(starwars);
    }
    
}

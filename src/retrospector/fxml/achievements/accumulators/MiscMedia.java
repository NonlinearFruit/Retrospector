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
    private Achievement nerd;
    
    private boolean starwarsFound;
    private boolean nerdFound;
    
    public MiscMedia() {
        starwars = new Achievement("","Star Wars","Star Wars in a title",3);
        starwars.setShowable(false);
        nerd = new Achievement("","Nerd","Media with 10 facts");
        nerd.setShowable(false);
        
        starwarsFound = false;
        nerdFound = false;
    }

    @Override
    public void accumulate(Media item) {
        if (Achievement.isContained(item.getTitle(), "Star Wars"))
            starwarsFound = true;
        if (item.getFactoids().size() >= 10)
            nerdFound = true;
            
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        starwars.setProgress(starwarsFound?100:0);
        nerd.setProgress(nerdFound? 100:0);
        
        return super.getShowableAchievements(starwars, nerd);
    }
    
}

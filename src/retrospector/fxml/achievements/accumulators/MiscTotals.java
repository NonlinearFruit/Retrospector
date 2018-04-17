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
public class MiscTotals extends Accumulator<Media> {
    
    private Achievement doubledip;
    private Achievement dejavu;
    private Achievement timeloop;
    
    private Integer totalMedia;
    private Integer totalDefaultMedia;
    private Integer totalReviews;
    private Integer totalDefaultReviews;
    private Integer totalFactoids;
    private Integer totalDefaultFactoids;
    
    public MiscTotals() {
        totalMedia = 0;
        totalDefaultMedia = 0;
        totalReviews = 0;
        totalDefaultReviews = 0;
        totalFactoids = 0;
        totalDefaultFactoids = 0;
        
        String hint = "If at first you don't succeed";
        doubledip = new Achievement("","Double Dip","10 more reviews than media",3);
        doubledip.setHint(hint);
        dejavu = new Achievement("","Deja Vu","100 more reviews than media",2);
        dejavu.setHint(hint);
        timeloop = new Achievement("","Time Loop","1k more reviews than media",1);
        timeloop.setHint(hint);
    }

    @Override
    public void accumulate(Media item) {
        totalMedia++;
        totalReviews += item.getReviews().size();
        totalFactoids += item.getFactoids().size();
        
        Integer numDefaultReviews = Math.toIntExact(
                item.getReviews()
                        .stream()
                        .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                        .count()
        );
        
        if (numDefaultReviews > 0) {
            totalDefaultMedia++;
            totalDefaultReviews += numDefaultReviews;
            totalDefaultFactoids += item.getFactoids().size();
        }
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        Integer progress = totalDefaultReviews - totalDefaultMedia;
        
        doubledip.setProgress(Achievement.scaleToFit(progress, 10));
        dejavu.setProgress(Achievement.scaleToFit(progress, 100));
        timeloop.setProgress(Achievement.scaleToFit(progress, 1000));
        
        return super.getShowableAchievements(timeloop, dejavu, doubledip);
    }
    
}

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
public class RockPaperScissors extends Accumulator<Media> {
    private Achievement rock;
    private Achievement paper;
    private Achievement scissors;
    private Achievement lizard;
    private Achievement spock;
    
    public RockPaperScissors() {
        String hint = "?";
        
        rock = new Achievement("","Rock","Rock (or Earth) in a title",3);
        rock.setShowable(false);
        rock.setHint(hint);
        
        paper = new Achievement("","Paper","Paper (or Origami) in a title",3);
        paper.setShowable(false);
        paper.setHint(hint);
        
        scissors = new Achievement("","Scissors","Scissor (or Blade) in a title",3);
        scissors.setShowable(false);
        scissors.setHint(hint);
        
        lizard = new Achievement("","Lizard","Lizard (or Godzilla) in a title",3);
        lizard.setShowable(false);
        lizard.setHint(hint);
        
        spock = new Achievement("","Spock","Star Trek (or Alien) in a title",3);
        spock.setShowable(false);
        spock.setHint(hint);
        
    }
    
    @Override
    public void accumulate(Media item) {
        String title = item.getTitle();
        if (Achievement.isContained(title, "Rock") || Achievement.isContained(title, "Earth"))
            rock.setProgress(Achievement.MAX_PROGRESS);
        
        if (Achievement.isContained(title, "Paper") || Achievement.isContained(title, "Origami"))
            paper.setProgress(Achievement.MAX_PROGRESS);
        
        if (Achievement.isContained(title, "Scissor") || Achievement.isContained(title, "Blade"))
            scissors.setProgress(Achievement.MAX_PROGRESS);
        
        if (Achievement.isContained(title, "Lizard") || Achievement.isContained(title, "Godzilla"))
            lizard.setProgress(Achievement.MAX_PROGRESS);
        
        if (Achievement.isContained(title, "Star Trek") || Achievement.isContained(title, "Alien"))
            spock.setProgress(Achievement.MAX_PROGRESS);
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        return super.getShowableAchievements(rock,paper,scissors,lizard,spock);
    }
    
    
}

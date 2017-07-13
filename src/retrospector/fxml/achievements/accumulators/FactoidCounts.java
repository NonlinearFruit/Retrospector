/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.Factoid;

/**
 *
 * @author nonfrt
 */
public class FactoidCounts extends Accumulator<Factoid>{
    // Smallest
    private Achievement researcher;
    private Achievement scientist;
    private Achievement objective;
    
    // Largest
    private Achievement enigmatologist;
    private Achievement riddler;
    private Achievement puzzler;
    
    // Total
    private Achievement expert;
    private Achievement pro;
    private Achievement whiz;
    
    // Types
    private Achievement trivial;
    
    private Map<String,Integer> factoidCounts;
    private Integer totalFacts;
    
    public FactoidCounts() {
        String hint = "?";
        researcher = new Achievement("","Researcher","1,000 facts for each factoid",1);
        researcher.setHint(hint);
        scientist = new Achievement("","Scientist","100 facts for each factoid",2);
        scientist.setHint(hint);
        objective = new Achievement("","Objective","10 facts for each factoid",3);
        objective.setHint(hint);
        
        enigmatologist = new Achievement("","Enigmatologist","Factoid with 10,000 facts",1);
        enigmatologist.setHint(hint);
        riddler = new Achievement("","Riddler","Factoid with 1,000 facts",2);
        riddler.setHint(hint);
        puzzler = new Achievement("","Puzzler","Factoid with 100 facts",3);
        puzzler.setHint(hint);
        
        expert = new Achievement("","Trivia Expert","Collect 10,000 factoids",1);
        expert.setHint(hint);
        pro = new Achievement("","Trivia Pro","Collect 1,000 factoids",2);
        pro.setHint(hint);
        whiz = new Achievement("","Trivia Whiz","Collect 100 factoids",3);
        whiz.setHint(hint);
        
        trivial = new Achievement("","Trivial Pursuit","Have 5 factoid types",3);
        trivial.setHint(hint);
        
        factoidCounts = new HashMap<>();
        totalFacts = 0;
    }

    @Override
    public void accumulate(Factoid item) {
        Integer value = factoidCounts.getOrDefault(item.getTitle(), 0);
        value++;
        factoidCounts.put(item.getTitle(), value);
        totalFacts++;
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        Integer largest = Collections.max(factoidCounts.values());
        Integer smallest = Collections.min(factoidCounts.values());
        Integer numFactoids = factoidCounts.keySet().size();
        
        trivial.setProgress(Achievement.scaleToFit(numFactoids, 5));
        if (!trivial.isUnlocked()) {
            largest = 0;
            smallest = 0;
        }
        
        researcher.setProgress(Achievement.scaleToFit(smallest, 1000));
        scientist.setProgress(Achievement.scaleToFit(smallest, 100));
        objective.setProgress(Achievement.scaleToFit(smallest, 10));
        
        enigmatologist.setProgress(Achievement.scaleToFit(largest, 10000));
        riddler.setProgress(Achievement.scaleToFit(largest, 1000));
        puzzler.setProgress(Achievement.scaleToFit(largest, 100));
        
        expert.setProgress(Achievement.scaleToFit(totalFacts, 10000));
        pro.setProgress(Achievement.scaleToFit(totalFacts, 1000));
        whiz.setProgress(Achievement.scaleToFit(totalFacts, 100));
        
        return super.getShowableAchievements(
                researcher, scientist, objective, 
                enigmatologist, riddler, puzzler,
                expert, pro, whiz,
                trivial
        );
    }
}

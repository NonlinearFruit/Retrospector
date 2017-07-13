/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nonfrt
 */
public abstract class Accumulator<T> {
    
    public abstract void accumulate(T item);
    
    public abstract List<AchievementFX> getShowableAchievements();
    
    protected List<AchievementFX> getShowableAchievements(Achievement... achievements) {
        List<AchievementFX> list = new ArrayList<>();
        for (Achievement achievement : achievements)
            if (achievement.isUnlocked() || achievement.isShowable())
                list.add(new AchievementFX(achievement));
        
        return list;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.fxml.achievements.AchievementFX;
import retrospector.model.DataManager;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class UserCounts extends Accumulator<Review>{

    private Achievement bff;
    private Achievement chum;
    private Achievement friend;
    
    private Achievement anthropologist;
    private Achievement famous;
    private Achievement popular;
    
    private Achievement social;
    private Achievement amicable;
    private Achievement gregarious;
    
    private Map<String,Integer> userReviews;
    private Integer totalReviews;
    
    public UserCounts() {
        userReviews = new HashMap<>();
        totalReviews = 0;
        
        String hint = "Those who retrospect together, stay together";
        bff = new Achievement("","BFFs","1,000 reviews from a user",1);
        bff.setHint(hint);
        chum = new Achievement("","Chums","100 reviews from a user",2);
        chum.setHint(hint);
        friend = new Achievement("","Friends","10 reviews from a user",3);
        friend.setHint(hint);
        
        hint = "Ask your friends";
        anthropologist = new Achievement("","Anthropologist","10,000 reviews from users",1);
        anthropologist.setHint(hint);
        famous = new Achievement("","Famous","1,000 reviews from users",2);
        famous.setHint(hint);
        popular = new Achievement("","Popular","100 reviews from users",3);
        popular.setHint(hint);
        
        hint = "Retrospecting is a great way to make friends*";
        social = new Achievement("","Social","Have 3 users",3);
        social.setHint(hint);
        amicable = new Achievement("","Amicable","Have 6 users",2);
        amicable.setShowable(false);
        gregarious = new Achievement("","Gregarious","Have 10 users",1);
        gregarious.setShowable(false);
    }
    
    @Override
    public void accumulate(Review item) {
        String user = item.getUser();
        if (!user.equals(DataManager.getDefaultUser())) {
            Integer value = userReviews.getOrDefault(item.getUser(), 0);
            value++;
            userReviews.put(item.getUser(), value);
            
            totalReviews += 1;
        }
    }

    @Override
    public List<AchievementFX> getShowableAchievements() {
        Collection<Integer> userValues = new ArrayList<>(userReviews.values());
        if ( userValues.size() == 0 )
            userValues.add(0);
        Integer progress = Collections.max(userValues);
        Integer numUsers = userReviews.keySet().size();
        
        bff.setProgress(Achievement.scaleToFit(progress, 1000));
        chum.setProgress(Achievement.scaleToFit(progress, 100));
        friend.setProgress(Achievement.scaleToFit(progress, 10));
        
        anthropologist.setProgress(Achievement.scaleToFit(totalReviews, 10000));
        famous.setProgress(Achievement.scaleToFit(totalReviews, 1000));
        popular.setProgress(Achievement.scaleToFit(totalReviews, 100));
        
        social.setProgress(Achievement.scaleToFit(numUsers, 3));
        amicable.setProgress(Achievement.scaleToFit(numUsers, 6));
        gregarious.setProgress(Achievement.scaleToFit(numUsers, 10));
        
        return super.getShowableAchievements(
                anthropologist, famous, popular,
                bff, chum, friend,
                gregarious, amicable, social
        );
    }
    
}

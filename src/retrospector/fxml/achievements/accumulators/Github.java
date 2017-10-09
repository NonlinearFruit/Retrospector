/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.model.DataManager;
import retrospector.util.SlowAsMolassesInJanuaryException;
import retrospector.util.SpeedRacer;

/**
 *
 * @author nonfrt
 */
public class Github extends Accumulator{
    
    private Achievement stargazer;
    private Achievement stargazerAndroid;
    
    // Vars
    private boolean hasStarred;
    private boolean hasStarredAndroid;
    
    public Github() {
        stargazer = new Achievement("","Star Gazer","Star Retrospector",1);
        stargazer.setHint("Show some love");
        stargazerAndroid = new Achievement("","Droid Dreamer","Star the App",1);
        stargazerAndroid.setHint("Show some love");
        
        hasStarred = false;
        hasStarredAndroid = false;
    }

    @Override
    public void accumulate(Object item) {
        try{
            SpeedRacer.go(()->{
                try (Scanner scanner = new Scanner(new URL("https://api.github.com/repos/NonlinearFruit/Retrospector/stargazers").openStream())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    hasStarred = Arrays.asList(responseBody.split("\n")).stream().anyMatch(s->s.contains("login")&&s.contains(DataManager.getGithubUser())&&!DataManager.getGithubUser().isEmpty());
                } catch(Exception ex) {
                    System.err.println(ex.getMessage());
                }
                return null;
            }, 1000);
        } catch(SlowAsMolassesInJanuaryException ex) {
            System.err.println("api.github.com timed out");
        }
        try{
            SpeedRacer.go(()->{
                try (Scanner scanner = new Scanner(new URL("https://api.github.com/repos/NonlinearFruit/Retrospector-Android/stargazers").openStream())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    hasStarredAndroid = Arrays.asList(responseBody.split("\n")).stream().anyMatch(s->s.contains("login")&&s.contains(DataManager.getGithubUser())&&!DataManager.getGithubUser().isEmpty());
                } catch(Exception ex) {
                    System.err.println(ex.getMessage());
                }
                return null;
            }, 1000);
        } catch(SlowAsMolassesInJanuaryException ex) {
            System.err.println("api.github.com timed out");
        }
    }

    @Override
    public List getShowableAchievements() {
        stargazer.setProgress(hasStarred? 100:0);
        stargazerAndroid.setProgress(hasStarredAndroid? 100:0);
        return super.getShowableAchievements(stargazer,stargazerAndroid);
    }
    
}

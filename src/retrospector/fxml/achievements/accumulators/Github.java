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
    
    public Github() {
        stargazer = new Achievement("","Star Gazer","Star Retrospector",1);
        stargazer.setHint("Show some love");
    }

    @Override
    public void accumulate(Object item) {
        Integer progress = 0;
        try{
            progress = SpeedRacer.go(()->{
                int result = 0;
                try (Scanner scanner = new Scanner(new URL("https://api.github.com/repos/NonlinearFruit/Retrospector/stargazers").openStream())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    result = Arrays.asList(responseBody.split("\n")).stream().anyMatch(s->s.contains("login")&&s.contains(DataManager.getGithubUser())&&!DataManager.getGithubUser().isEmpty())?100:0;
                } catch(Exception ex) {
                    System.err.println(ex.getMessage());
                }
                return result;
            });
        } catch(SlowAsMolassesInJanuaryException ex) {
            progress = 0;
        }
        stargazer.setProgress(progress);
    }

    @Override
    public List getShowableAchievements() {
        return super.getShowableAchievements(stargazer);
    }
    
}

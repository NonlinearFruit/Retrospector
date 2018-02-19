/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import retrospector.fxml.achievements.tables.HighScoreTable;
import retrospector.fxml.achievements.tables.HighScore;
import retrospector.fxml.achievements.tables.TopMedia;
import retrospector.fxml.achievements.tables.TopMediaTable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import retrospector.fxml.achievements.tables.MediaStreak;
import retrospector.fxml.achievements.tables.MediaStreakTable;
import retrospector.fxml.core.CoreController;
import retrospector.model.DataManager;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class AchievementTabController implements Initializable {

    @FXML
    private VBox anchor;
    @FXML
    private FlowPane achievementPane;
    @FXML
    private HBox header;
    
    private String headerImage = "";
    private Double headerSize = 30.0;
    ObjectProperty<CoreController.TAB> currentTab;
    @FXML
    private Button btnRefresh;
    @FXML
    private HBox highscoreBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAchievementTab();
    }    
    
    public void setup(ObjectProperty<CoreController.TAB> tab) {
        currentTab = tab;
    }
    
    public void update() {
        System.out.println("Start Achievements");
        long time = System.currentTimeMillis();
        achievementPane.getChildren().clear();
        header.getChildren().clear();
        List<Node> list = new ArrayList<>();
        
        // Header Vars
        int first = 0;
        int second = 0;
        int third = 0;
        int locked = 0;
        
        // Achievement Pane
        for (AchievementFX achievementFx : AchievementFactory.getAchievements()) {
            list.add(achievementFx.getDisplay());
            
            if( !achievementFx.getAchievement().isUnlocked() ) {
                locked++;
                continue;
            }
            
            switch(achievementFx.getAchievement().getTier()){
                case 3: third++;break;
                case 2: second++;break;
                case 1: first++;break;
                default: locked++;
            }
        }
        
        // Achievement Display
        achievementPane.getChildren().addAll(list);
        
        // Header Display
        header.getChildren().add(new Text(first+" "));
        header.getChildren().add(AchievementFX.trophyize(headerImage, 1, headerSize));
        header.getChildren().add(new Text("\t"+second+" "));
        header.getChildren().add(AchievementFX.trophyize(headerImage, 2, headerSize));
        header.getChildren().add(new Text("\t"+third+" "));
        header.getChildren().add(AchievementFX.trophyize(headerImage, 3, headerSize));
        header.getChildren().add(new Text("\t"+locked+" "));
        header.getChildren().add(AchievementFX.trophyize(AchievementFX.lockedImage, 0, headerSize));
        
        
        highscoreBox.getChildren().clear();
        // Media Streak Display
        List<MediaStreak> streaks = new ArrayList<>();
        for (String category : DataManager.getCategories()) {
            streaks.add(AchievementFactory.getMediaStreak(category));
        }
        highscoreBox.getChildren().add(new MediaStreakTable(streaks));
        
        highscoreBox.getChildren().add(new Separator(Orientation.VERTICAL));
        
        // Highscore Display
        List<HighScore> scores = new ArrayList<>();
        for (String category : DataManager.getCategories()) {
            scores.add(AchievementFactory.getHighScore(category));
        }
        highscoreBox.getChildren().add(new HighScoreTable(scores));
        
        highscoreBox.getChildren().add(new Separator(Orientation.VERTICAL));
        
        // Top Media Display
        List<TopMedia> bests = new ArrayList<>();
        for (String category : DataManager.getCategories()) {
            bests.add(AchievementFactory.getTopMedia(category));
        }
        highscoreBox.getChildren().add(new TopMediaTable(bests));
        
        System.out.println("\t"+(System.currentTimeMillis()-time)+"ms to complete");
    }
    
    public void initAchievementTab() {
        achievementPane.setVgap(50);
        achievementPane.prefWidthProperty().bind(Bindings.add(-70,anchor.widthProperty()));
//        update();
        btnRefresh.setOnAction(e->{
            update();
        });
    }
}

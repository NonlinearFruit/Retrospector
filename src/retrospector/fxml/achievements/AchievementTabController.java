/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import retrospector.fxml.CoreController;

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
        List<Node> list = new ArrayList<>();
        
        // Header Vars
        int first = 0;
        int second = 0;
        int third = 0;
        int locked = 0;
        
        // Achievement Pane
        long start = System.currentTimeMillis();
        
        for (Achievement achievement : AchievementFactory.getAchievements()) {
            list.add(new AchievementManager(achievement).getDisplay());
            
            if( !achievement.isUnlocked() ) {
                locked++;
                continue;
            }
            
            switch(achievement.getTier()){
                case 3: third++;break;
                case 2: second++;break;
                case 1: first++;break;
                default: locked++;
            }
        }
        System.out.println("\tUpdate:  "+(System.currentTimeMillis()-start));
        
        
//        achievementPane.getChildren().clear();
        header.getChildren().clear();
        // Achievement Display
        achievementPane.getChildren().addAll(list);
        
        // Header Display
        header.getChildren().add(new Text(first+" "));
        header.getChildren().add(AchievementManager.trophyize(headerImage, 1, headerSize));
        header.getChildren().add(new Text("\t"+second+" "));
        header.getChildren().add(AchievementManager.trophyize(headerImage, 2, headerSize));
        header.getChildren().add(new Text("\t"+third+" "));
        header.getChildren().add(AchievementManager.trophyize(headerImage, 3, headerSize));
        header.getChildren().add(new Text("\t"+locked+" "));
        header.getChildren().add(AchievementManager.trophyize(AchievementManager.lockedImage, 0, headerSize));
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

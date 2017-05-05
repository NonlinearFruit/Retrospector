/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import retrospector.fxml.CoreController;
import retrospector.model.DataManager;

/**
 *
 * @author nonfrt
 */
public class Retrospector extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Long start;
        Long end;
        start = System.currentTimeMillis();
        primaryStage.setScene(new Scene(new HBox(),1300,800));
        primaryStage.show();
        System.out.println("PRIMARY STAGE:"+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        DataManager.startDB();
        System.out.println("DATABASE:    "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader ldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/Core.fxml"));
            Parent root = ldr.load();
            CoreController core = ldr.getController();
        System.out.println("CORE:        "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader statldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/chart/StatsTab.fxml"));
            statldr.load();
            core.setStatsController(statldr);
        System.out.println("STATS:       "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader searchldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/SearchTab.fxml"));
            searchldr.load();
            core.setSearchController(searchldr);
        System.out.println("SEARCH:      "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader medialdr = new FXMLLoader(getClass().getResource("/retrospector/fxml/MediaTab.fxml"));
            medialdr.load();
            core.setMediaController(medialdr);
        System.out.println("MEDIA:       "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader reviewldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/ReviewTab.fxml"));
            reviewldr.load();
            core.setReviewController(reviewldr);
        System.out.println("REVIEW:      "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader listldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/ListsTab.fxml"));
            listldr.load();
            core.setListController(listldr);
        System.out.println("LIST:        "+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        FXMLLoader achieveldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/achievements/AchievementTab.fxml"));
            achieveldr.load();
            core.setAchieveController(achieveldr);
        System.out.println("ACHIEVEMENTS:"+(System.currentTimeMillis()-start));
        
//        Scene scene = new Scene(root, 1300, 800);
        primaryStage.getScene().setRoot(root);
        primaryStage.setTitle("Retrospector");
//        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-16.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-22.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-24.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-32.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-48.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-64.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-128.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-256.png"));
//        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

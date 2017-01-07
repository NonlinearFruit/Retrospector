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
        
        DataManager.startDB();
        
        FXMLLoader ldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/Core.fxml"));
            Parent root = ldr.load();
            CoreController core = ldr.getController();
        FXMLLoader statldr = new FXMLLoader(getClass().getResource("/retrospector/fxml/StatsTab.fxml"));
            statldr.load();
            core.setStatsTab(statldr);
        
        Scene scene = new Scene(root, 1300, 800);
        
        primaryStage.setTitle("Retrospector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import retrospector.fx.MediaForm;

/**
 *
 * @author nonfrt
 */
public class Retrospector extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox reviewPane = new VBox();
        MediaForm form = new MediaForm(reviewPane);
        BorderPane root = new BorderPane();
        root.setCenter(form);
        root.setRight(reviewPane);
        
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

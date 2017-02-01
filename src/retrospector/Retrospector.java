/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import retrospector.fxml.CoreController;
import retrospector.model.DataManager;

/**
 *
 * @author nonfrt
 */
public class Retrospector extends Application {
    
    Map<String,List<double[]>> clicks = new HashMap<>();
    
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
        
//        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e->{
//            String tab = core.anchorCenter.getSelectionModel().getSelectedItem().getText();
//            if(clicks.containsKey(tab))
//                clicks.get(tab).add(new double[]{e.getScreenX(),e.getScreenY()});
//            else
//                clicks.put(tab, Arrays.<double[]>asList(new double[]{e.getScreenX(),e.getScreenY()}));
//            System.out.println(core.anchorCenter.getSelectionModel().getSelectedItem().getText()+": "+e.getScreenX()+" , "+e.getScreenY());
//                });
        
        primaryStage.setTitle("Retrospector");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-32.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-64.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-128.png"));
        primaryStage.getIcons().add(new Image("file:/retrospector/res/star-half-full-256.png"));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

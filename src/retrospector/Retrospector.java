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

/**
 *
 * @author nonfrt
 */
public class Retrospector extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
//        List<Media> media = new ArrayList<>();
//        SearchPane<Media> search = new SearchPane(UtilityCloset.getMediaHound(),media,"AverageRating","Title","Creator","Season","Episode");
//        VBox reviewPane = new VBox();
//        MediaForm form = new MediaForm(reviewPane);
//        BorderPane root = new BorderPane();
//        MenuItem b = new MenuItem("New Media");
//        b.setOnAction((e)->{
//            Media neo = new Media();
//            media.add(neo);
//            form.setMedia(neo);
//            search.addToSearchSet(neo);
//        });
//        Menu a = new Menu("Data",null,b);
//        MenuBar menu = new MenuBar(a);
//        root.setTop(menu);
//        root.setCenter(form);
//        root.setRight(reviewPane);
//        root.setLeft(search);
        
        Parent root = FXMLLoader.load(getClass().getResource("/retrospector/fxml/Core.fxml"));
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.cheatsheet;

import retrospector.util.PumpkinEater;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author nonfrt
 */
public class Cheatsheet extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        WebView web = new WebView();
//        web.getEngine().load(getClass().getResource("web/materialtable.css").toExternalForm());
        String webContent = new PumpkinEater(
                "Search Cheatsheet",
                new String[]{
                    "Symbol",
                    "Name",
                    "Example",
                    "Explanation"
                },
                new String[]{":","and","lord:rings","contains 'lord' and 'rings'"},
                new String[]{"|","or","Marvel|DC","contains 'marvel' or 'dc'"},
                new String[]{"!","not","!rebecca black","contains no 'rebecca black'"},
                new String[]{"`","cmd","[See Below]","[See Below]"},
                new String[]{"`T","title","`T~civil war","title contains 'civil war'"},
                new String[]{"`C","creator","`C=BBC","creator equals 'bbc'"},
                new String[]{"`S","season","`S=1","season equals '1'"},
                new String[]{"`E","episode","`E~13","episode contains '13'"},
                new String[]{"`A","category","`A~Book","category contains 'book'"},
                new String[]{"Example","-->","batman|superman:`A=Movie:!vs","contains 'batman' or 'superman',<br>category equals 'movie' and<br>contains no 'vs'"},
                new String[]{"Example","-->","sherlock:doyle:baskervilles","contains 'sherlock',<br>contains 'doyle' and<br>contains 'baskervilles'"}
        ).getHtml();
        
        web.getEngine().loadContent(webContent);
        StackPane root = new StackPane(web);
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Search Cheatsheet");
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
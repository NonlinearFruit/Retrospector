/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.cheatsheet;

import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
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
                new String[]{"`U","user","`U~Adam","reviewed by a user 'Adam'"},
                new String[]{"`D","date","`D>10-21-17","reviewed after 21 Oct 2017"},
                new String[]{"`#","rating","`#>7","rated high than a 7"},
                new String[]{"Example","-->","batman|superman:`A=Movie:!vs","contains 'batman' or 'superman',<br>category equals 'movie' and<br>contains no 'vs'"},
                new String[]{"Example","-->","sherlock:doyle:baskervilles","contains 'sherlock',<br>contains 'doyle' and<br>contains 'baskervilles'"}
        ).getHtml();
        
        web.getEngine().loadContent(webContent);
        StackPane root = new StackPane(web);
        root.setPrefHeight(500);
        root.setPrefWidth(800);
        UndecoratorScene.setClassicDecoration();
        UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, (Region) root);
        primaryStage.setScene(undecoratorScene);
        primaryStage.setTitle("Search Cheatsheet");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
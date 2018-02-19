/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.core;

import insidefx.undecorator.UndecoratorScene;
import java.io.IOException;
import retrospector.fxml.media.MediaTabController;
import retrospector.fxml.search.SearchTabController;
import retrospector.fxml.chart.StatsTabController;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import retrospector.fxml.list.ListsTabController;
import retrospector.fxml.quickentry.QuickEntryController;
import retrospector.fxml.achievements.AchievementTabController;
import retrospector.fxml.cheatsheet.Cheatsheet;
import retrospector.fxml.preferences.PreferencesController;
import retrospector.fxml.wishlist.WishlistTabController;
import retrospector.model.*;
import retrospector.util.Toast;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class CoreController implements Initializable {

    @FXML
    private Tab achievementTab;
    @FXML
    private MenuBar menuBar;
    @FXML
    public TabPane anchorCenter;
    @FXML
    private Tab searchTab;
    @FXML
    private Tab mediaTab;
    @FXML
    private Tab chartTab;
    @FXML
    private Tab listTab;
    @FXML
    private Tab wishlistTab;

    public static enum TAB{ SEARCH, MEDIA, CHART, WISHLIST, LIST, ACHIEVEMENT} 
    public static final DecimalFormat ratingFormat =  new DecimalFormat("#.#");
    
    private final ObjectProperty<Media> currentMedia = new SimpleObjectProperty<>();
    private final ObjectProperty<TAB> currentTab = new SimpleObjectProperty<>();

    private StatsTabController statsController;
    private SearchTabController searchController;
    private MediaTabController mediaController;
    private WishlistTabController wishController;
    private ListsTabController listsController;
    private AchievementTabController achieveController;
    
    public void setStatsController(FXMLLoader ldr){ 
        chartTab.setContent(ldr.getRoot());
        statsController = ldr.getController(); 
        statsController.setup(currentTab,currentMedia);
    }
    
    public void setMediaController(FXMLLoader ldr){ 
        mediaTab.setContent(ldr.getRoot());
        mediaController = ldr.getController();
        mediaController.setup(currentTab,currentMedia,this::nextPreviousMedia);
    }
    
    public void setWishlistController(FXMLLoader ldr) {
        wishlistTab.setContent(ldr.getRoot());
        wishController = ldr.getController();
        wishController.setup(currentTab, currentMedia);
    }
    
    public void setListController(FXMLLoader ldr){ 
        listTab.setContent(ldr.getRoot());
        listsController = ldr.getController();
        listsController.setup(currentTab); 
    }
    
    public void setSearchController(FXMLLoader ldr){ 
        searchTab.setContent(ldr.getRoot());
        searchController = ldr.getController();
        searchController.setup(currentTab,currentMedia); 
    }
    
    public void setAchieveController(FXMLLoader ldr){ 
        achievementTab.setContent(ldr.getRoot());
        achieveController = ldr.getController();
        achieveController.setup(currentTab); 
    }
    
    private double xOffset = 0;
    private double yOffset = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Create the menu bar
        menuBar.setOnMousePressed(e-> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
        });
        menuBar.setOnMouseDragged(e-> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        
        // Tabs
        searchTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.SEARCH);
        });
        mediaTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.MEDIA);
        });
        chartTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.CHART);
        });
        wishlistTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.WISHLIST);
        });
        listTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.LIST);
        });
        achievementTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.ACHIEVEMENT);
        });
        currentTab.set(TAB.SEARCH);
        currentTab.addListener((observe,old,neo)->{
            Tab tab;
            switch(neo){
                default:
                case SEARCH:
                    tab = searchTab;
                    break;
                case MEDIA:
                    tab = mediaTab;
                    break;
                case CHART:
                    tab = chartTab;
                    break;
                case WISHLIST:
                    tab = wishlistTab;
                    break;
                case LIST:
                    tab = listTab;
                    break;
                case ACHIEVEMENT:
                    tab = achievementTab;
                    break;
            }
            anchorCenter.getSelectionModel().select(tab);
        });

        anchorCenter.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            if(neo.getText().equals("Search"))
                searchController.update();
            else if(neo.getText().equals("Media"))
                mediaController.update();
            else if(neo.getText().equals("Chart"))
                statsController.update();
            else if(neo.getText().equals("Wishlist"))
                wishController.update();
            else if(neo.getText().equals("List"))
                listsController.update();
            else if(neo.getText().equals("Achievement"))
                achieveController.update();
        });
        
        currentMedia.addListener((observe, old, neo)->{
            if(neo==null){
                mediaTab.setDisable(true);
            } else {
                mediaTab.setDisable(false);
            }
        });
        
        mediaTab.setDisable(true);
    }
    
    @FXML
    public void standardEntry(ActionEvent e) {
        Media neo = new Media();
        neo.setId(DataManager.createDB(neo));
        setMedia(neo);
        setTab(TAB.MEDIA);
    }
    
    @FXML
    public void quickEntry(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/quickentry/QuickEntry.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            QuickEntryController qec = fxmlLoader.getController();
            qec.setup(currentTab);
            stage.setTitle("Quick Entry");
            stage.show();
        } catch (Exception ex) {
            System.out.println("Quick Entry Failed: "+ex.getMessage());
        }
    }
    
    @FXML
    public void preferences(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/preferences/Preferences.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            PreferencesController qec = fxmlLoader.getController();
            stage.setTitle("Preferences");
            stage.show();
        } catch (Exception ex) {
            System.out.println("Preferences Failed: "+ex.getMessage());
        }
    }
    
    @FXML
    public void server(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/server/ServerTab.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            stage.setTitle("Server");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @FXML
    private void performanceTest(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/dumpster/PerformanceTester.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            stage.setTitle("Performance Test");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void about(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/about/About.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            UndecoratorScene.setClassicDecoration();
            UndecoratorScene undecoratorScene = new UndecoratorScene(stage, (Region) root);
            stage.setScene(undecoratorScene);
            stage.setTitle("About");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @FXML
    public void backup(ActionEvent e) { 
        DataManager.makeBackup(); 
        Toast.makeText((Stage) menuBar.getScene().getWindow(), "Backup Complete!", 3000, 500, 500);
    }
    
    @FXML
    public void cheatsheet(ActionEvent e) { new Cheatsheet().start(new Stage()); }
    
    @FXML
    public void exit() { exit(null); }
    
    public void exit(ActionEvent e) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.hide();
    }
    
    public void setTab(TAB aTab){
        currentTab.set(aTab);
    }
    
    public void setMedia(Media m){
        currentMedia.set(m);
    }
    
    protected void nextPreviousMedia(Integer i){
        if(i>0)
            searchController.next();
        else
            searchController.previous();
    }
}

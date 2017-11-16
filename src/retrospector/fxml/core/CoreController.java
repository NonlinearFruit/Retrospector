/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.core;

import java.io.IOException;
import retrospector.fxml.media.MediaTabController;
import retrospector.fxml.search.SearchTabController;
import retrospector.fxml.chart.StatsTabController;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import retrospector.fxml.list.ListsTabController;
import retrospector.fxml.quickentry.QuickEntryController;
import retrospector.fxml.review.ReviewTabController;
import retrospector.fxml.achievements.AchievementTabController;
import retrospector.fxml.cheatsheet.Cheatsheet;
import retrospector.fxml.preferences.PreferencesController;
import retrospector.fxml.scraper.ScraperController;
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
    private Button closeButton;
    @FXML
    private Button minButton;
    @FXML
    private Button maxButton;

    public static enum TAB{ SEARCH, MEDIA, REVIEW, CHART, LIST, ACHIEVEMENT} 
    public static final DecimalFormat ratingFormat =  new DecimalFormat("#.#");
    
    @FXML
    public TabPane anchorCenter;
    @FXML
    private Tab searchTab;
    @FXML
    private Tab mediaTab;
    @FXML
    private Tab reviewTab;
    @FXML
    private Tab chartTab;
    @FXML
    private Tab listTab;
    
    private final ObjectProperty<Media> currentMedia = new SimpleObjectProperty<>();
    private final ObjectProperty<Review> currentReview = new SimpleObjectProperty<>();
    private final ObjectProperty<TAB> currentTab = new SimpleObjectProperty<>();

    private StatsTabController statsController;
    private SearchTabController searchController;
    private MediaTabController mediaController;
    private ReviewTabController reviewController;
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
        mediaController.setup(currentTab,currentMedia,currentReview,this::nextPreviousMedia);
    }
    
    public void setReviewController(FXMLLoader ldr){ 
        reviewTab.setContent(ldr.getRoot());
        reviewController = ldr.getController();
        reviewController.setup(currentTab,currentMedia,currentReview); 
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

        // This is just for test, try not to put it in a really release
//        if(DataManager.getMedia().size()==0)
//            Dumpster.createMedia(1000);

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
        closeButton.setOnAction(e->{
            exit();
        });
        minButton.setOnAction(e->{
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        maxButton.setOnAction(e->{
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setMaximized(!stage.isMaximized());
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
        reviewTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.REVIEW);
        });
        chartTab.selectedProperty().addListener((observe,old,neo)->{
            if(neo)
                setTab(TAB.CHART);
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
                case REVIEW:
                    tab = reviewTab;
                    break;
                case CHART:
                    tab = chartTab;
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
            else if(neo.getText().equals("Review"))
                reviewController.update();
            else if(neo.getText().equals("Chart"))
                statsController.update();
            else if(neo.getText().equals("List"))
                listsController.update();
            else if(neo.getText().equals("Achievement"))
                achieveController.update();
        });
        
        currentMedia.addListener((observe, old, neo)->{
            if(neo==null){
                mediaTab.setDisable(true);
                reviewTab.setDisable(true);
//                chartTab.setDisable(true);
            } else {
                mediaTab.setDisable(false);
//                chartTab.setDisable(false);
            }
        });
        
        currentReview.addListener((observe,old,neo)->{
            if(neo==null){
                reviewTab.setDisable(true);
            } else {
                reviewTab.setDisable(false);
            }
        });
        mediaTab.setDisable(true);
        reviewTab.setDisable(true);
//        chartTab.setDisable(true);
    }
    
    public void standardEntry(ActionEvent e) {
        Media neo = new Media();
        neo.setId(DataManager.createDB(neo));
        setMedia(neo);
        setTab(TAB.MEDIA);
    }
    
    public void quickEntry(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/quickentry/QuickEntry.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            QuickEntryController qec = fxmlLoader.getController();
            qec.setup(currentTab);
            Stage stage = new Stage();
            stage.setTitle("Quick Entry");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception ex) {
            System.out.println("Quick Entry Failed: "+ex.getMessage());
        }
    }
    
    public void preferences(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/preferences/Preferences.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            PreferencesController qec = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Preferences");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception ex) {
            System.out.println("Preferences Failed: "+ex.getMessage());
        }
    }
    
    public void server(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/server/ServerTab.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Server");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void about(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/about/About.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void scrape(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/scraper/Scraper.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ScraperController scrc = fxmlLoader.getController();
            scrc.setup(media -> { 
                int id = DataManager.createDB(media); 
                media.setId(id);
                setMedia( media ); 
                setTab( TAB.MEDIA ); 
            });
            Stage stage = new Stage();
            stage.setTitle("Scraper");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception ex) {
            System.out.println("Scraper Failed: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void backup(ActionEvent e) { 
        DataManager.makeBackup(); 
        Toast.makeText((Stage) closeButton.getScene().getWindow(), "Backup Complete!", 3000, 500, 500);
    }
    
    public void cheatsheet(ActionEvent e) { new Cheatsheet().start(new Stage()); }
    
    public void exit() { exit(null); }
    
    public void exit(ActionEvent e) {
        Platform.exit();
//        Stage stage = (Stage) closeButton.getScene().getWindow();
//        stage.hide();
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

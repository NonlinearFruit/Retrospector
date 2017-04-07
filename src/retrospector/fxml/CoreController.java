/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import retrospector.fxml.chart.StatsTabController;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import retrospector.model.*;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class CoreController implements Initializable {

    public static enum TAB{ SEARCH, MEDIA, REVIEW, CHART, LIST} 
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // This is just for test, try not to put it in a really release
//        if(DataManager.getMedia().size()==0)
//            Dumpster.createMedia(1000);
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
                statsController.update(currentMedia.get());
            else if(neo.getText().equals("List"))
                listsController.update();
        });
        
        currentMedia.addListener((observe, old, neo)->{
            if(neo==null){
                mediaTab.setDisable(true);
                reviewTab.setDisable(true);
                chartTab.setDisable(true);
            } else {
                mediaTab.setDisable(false);
                chartTab.setDisable(false);
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
        chartTab.setDisable(true);
    }
    
    private void setTab(TAB aTab){
        currentTab.set(aTab);
    }
    
    protected void nextPreviousMedia(Integer i){
        if(i>0)
            searchController.next();
        else
            searchController.previous();
    }
}

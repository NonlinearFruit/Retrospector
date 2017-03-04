/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class MediaTabController implements Initializable {

    @FXML
    private TextField mediaTitle;
    @FXML
    private TextField mediaCreator;
    @FXML
    private TextField mediaSeason;
    @FXML
    private TextField mediaEpisode;
    @FXML
    private Rating mediaStars;
    @FXML
    private Text mediaRating;
    @FXML
    private Text mediaMaxRating;
    @FXML
    private ChoiceBox<String> mediaCategory;
    @FXML
    private ChoiceBox<Media.Type> mediaType;
    @FXML
    private TextArea mediaDescription;
    @FXML
    private Button mediaNewReview;
    @FXML
    private Button mediaEditReview;
    @FXML
    private Button mediaDeleteReview;
    @FXML
    private TableView<Review> mediaReviewTable;
    @FXML
    private Button mediaSave;
    @FXML
    private Button mediaDelete;
    @FXML
    private Button mediaCancel;
    @FXML
    private TableColumn<Review, Double> mediaRatingColumn;
    @FXML
    private TableColumn<Review, String> mediaUserColumn;
    @FXML
    private TableColumn<Review, LocalDate> mediaDateColumn;
    @FXML
    private Button mediaNewMedia;
    @FXML
    private Button mediaAddSeason;
    @FXML
    private Button mediaAddEpisode;
    @FXML
    private HBox mediaTitleBox;
    @FXML
    private HBox mediaSeasonBox;
    @FXML
    private HBox mediaEpisodeBox;
    
    private Media currentMedia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public Media getMedia(){
        return currentMedia;
    }
    
    public void updateMedia(Media m){
        setMedia(m);
    }
    
    private void setMedia(Media m){
        currentMedia = m;
    }
    
    public void refresh(){
        updateMediaTab();
    }
    
    public void updateMediaTab(){
        setMedia(DataManager.getMedia(getMedia().getId()));
        mediaTitle.setText(getMedia().getTitle());
        mediaCreator.setText(getMedia().getCreator());
        mediaSeason.setText(getMedia().getSeasonId());
        mediaEpisode.setText(getMedia().getEpisodeId());
        BigDecimal rating = getMedia().getAverageRating();
        mediaStars.setRating(rating.divide(BigDecimal.valueOf(2)).doubleValue());
        mediaRating.setText(CoreController.ratingFormat.format(rating));
        
        mediaCategory.setValue(getMedia().getCategory());
        mediaType.setValue(getMedia().getType());
        mediaDescription.setText(getMedia().getDescription());
        
        mediaReviewTable.setItems(FXCollections.observableArrayList(getMedia().getReviews()));
        mediaReviewTable.refresh();
        if(mediaReviewTable.getItems().size()>0)
            mediaReviewTable.getSelectionModel().select(0);
    }
    
    public void initMediaTab(){
        mediaStars.setPartialRating(true);
        mediaStars.setDisable(true);
        mediaStars.setMax(DataManager.getMaxRating()/2);
        mediaStars.setRating(DataManager.getDefaultRating()/2);
        mediaRating.setText(CoreController.ratingFormat.format(DataManager.getDefaultRating()));
        mediaMaxRating.setText(CoreController.ratingFormat.format(DataManager.getMaxRating()));
        mediaCategory.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        mediaCategory.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            
        });
        mediaType.setItems(FXCollections.observableArrayList(Media.Type.values()));
        mediaType.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            switch(neo){
                case SERIES:
                    mediaSeasonBox.setVisible(true);
                    mediaEpisodeBox.setVisible(true);
                    mediaEpisodeBox.toFront();
                    break;
                case MINISERIES:
                    mediaSeasonBox.setVisible(false);
                    mediaEpisodeBox.setVisible(true);
                    mediaSeasonBox.toFront();
                    break;
                case SINGLE:
                default:
                    mediaSeasonBox.setVisible(false);
                    mediaEpisodeBox.setVisible(false);
            }
        });
        
        mediaReviewTable.getSelectionModel().selectedItemProperty().addListener((observe, old, neo)->{
//            setReview(neo);
            throw new UnsupportedOperationException("Review tab interaction not happening yet");
        });
        mediaRatingColumn.setCellValueFactory(new PropertyValueFactory<Review,Double>("Rating"));
        mediaUserColumn.setCellValueFactory(new PropertyValueFactory<Review,String>("User"));
        mediaDateColumn.setCellValueFactory(new PropertyValueFactory<Review,LocalDate>("Date"));
        
        mediaNewReview.setOnAction(e->{
            Review review = new Review();
            review.setMediaId(getMedia().getId());
            review.setId(DataManager.createDB(review));
            throw new UnsupportedOperationException("Review tab interaction not happening yet");
//            setReview(review);
//            anchorCenter.getSelectionModel().select(reviewTab);
        });
        mediaEditReview.setOnAction(e->{
            throw new UnsupportedOperationException("Review tab interaction not happening yet");
//            anchorCenter.getSelectionModel().select(reviewTab);
        });
        mediaDeleteReview.setOnAction(e->{
            throw new UnsupportedOperationException("Review tab interaction not happening yet");
//            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
//                DataManager.deleteDB(getReview());
//                updateMediaTab();
//            }
        });
        
        mediaSave.setOnAction(e->{
            Media m = new Media();
            m.clone(getMedia());
            System.out.println("Media ID: "+m.getId());
            m.setTitle(mediaTitle.getText());
            m.setCreator(mediaCreator.getText());
            m.setSeasonId(mediaSeason.getText());
            m.setEpisodeId(mediaEpisode.getText());
            m.setCategory(mediaCategory.getValue());
            m.setType(mediaType.getValue());
            m.setDescription(mediaDescription.getText());
            DataManager.updateDB(m);
            setMedia(m);
//            anchor.getSelectionModel().select(searchTab); // <-- This is annoying
        });
        mediaDelete.setOnAction(e->{
            throw new UnsupportedOperationException("Search tab interaction not happening yet");
//            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
//                DataManager.deleteDB(getMedia());
//                anchorCenter.getSelectionModel().select(searchTab);
//            }
        });
        mediaCancel.setOnAction(e->{
            throw new UnsupportedOperationException("Search tab interaction not happening yet");
//            anchorCenter.getSelectionModel().select(searchTab);
        });
        
        mediaNewMedia.setOnAction(e->{
            Media media = new Media();
            media.setId(DataManager.createDB(media));
            if(media.getId()<2)
                System.err.println("Media got a <2 id (mediaNewMedia#setOnAction");
            setMedia(media);
            updateMediaTab();
            mediaTitle.requestFocus();
        });
        
        mediaAddSeason.setOnAction(e->{
            Media media = new Media(
                    getMedia().getTitle(), 
                    getMedia().getCreator(), 
                    getMedia().getCategory(), 
                    getMedia().getType()
            );
            media.setDescription(getMedia().getDescription());
            setMedia(DataManager.getMedia(DataManager.createDB(media)));
            if(getMedia().getId()==-1)
                System.err.println("Media got a -1 id (mediaAddSeason#setOnAction");
            updateMediaTab();
            mediaSeason.requestFocus();
        });
        mediaAddEpisode.setOnAction(e->{
            Media media = new Media(
                    getMedia().getTitle(), 
                    getMedia().getCreator(), 
                    getMedia().getCategory(), 
                    getMedia().getType()
            );
            media.setDescription(getMedia().getDescription());
            media.setSeasonId(getMedia().getSeasonId());
            setMedia(DataManager.getMedia(DataManager.createDB(media)));
            if(getMedia().getId()==-1)
                System.err.println("Media got a -1 id (mediaAddEpisode#setOnAction");
            updateMediaTab();
            mediaEpisode.requestFocus();
        });
    }
}

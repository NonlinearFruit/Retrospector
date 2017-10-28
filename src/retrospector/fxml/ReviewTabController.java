/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;
import retrospector.fxml.CoreController.TAB;
import static retrospector.fxml.CoreController.ratingFormat;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.ControlFxTextFieldModifier;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ReviewTabController implements Initializable {

    @FXML
    private TextField reviewTitle;
    @FXML
    private TextField reviewCreator;
    @FXML
    private TextField reviewSeason;
    @FXML
    private TextField reviewEpisode;
    @FXML
    private Rating reviewStars;
    @FXML
    private Text reviewRating;
    @FXML
    private Text reviewMaxRating;
    @FXML
    private Slider reviewRater;
    @FXML
    private TextField reviewUser;
    @FXML
    private DatePicker reviewDate;
    @FXML
    private TextArea reviewDescription;
    @FXML
    private Button reviewSave;
    @FXML
    private Button reviewDelete;
    @FXML
    private Button reviewCancel;
    
    private ObjectProperty<Media> currentMedia;
    private ObjectProperty<Review> currentReview;
    private ObjectProperty<TAB> currentTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initReviewTab();
    }    
    
    private Media getMedia(){
        return currentMedia.get();
    }
    
    protected void setup(ObjectProperty<TAB> t, ObjectProperty<Media> m, ObjectProperty<Review> r){
        currentTab = t;
        currentMedia = m;
        currentReview = r;
    }
            
    
    protected void update(){
        updateReviewTab();
    }
    
    private void setMedia(Media m){
        currentMedia.set(m);
    }
    
    private void setReview(Review r){
        currentReview.set(r);
    }
    
    private Review getReview(){
        return currentReview.get();
    }
    
    private void setTab(TAB t){
        currentTab.set(t);
    }
    
    private void updateReviewTab(){
        reviewTitle.setText(getMedia().getTitle());
        reviewCreator.setText(getMedia().getCreator());
        reviewSeason.setText(getMedia().getSeason());
        reviewEpisode.setText(getMedia().getEpisode());
        reviewRater.setValue(getReview().getRating().doubleValue());
        reviewDescription.setText(getReview().getReview());
        reviewUser.setText(getReview().getUser());
        reviewDate.setValue(getReview().getDate());
    }
    
    private void initReviewTab(){
        reviewStars.setPartialRating(true);
        reviewStars.setDisable(true);
        reviewStars.setMax(DataManager.getMaxRating()/2);
        reviewStars.ratingProperty().bind(
                Bindings.divide(reviewRater.valueProperty(), 2)
        );
        reviewRating.textProperty().bind(
                Bindings.createStringBinding(()->ratingFormat.format(reviewRater.valueProperty().getValue()), reviewRater.valueProperty())
        );
        
        ControlFxTextFieldModifier.autocompleteMe(reviewUser, DataManager.getUsers());
        
        reviewSave.setOnAction(e->{
            getReview().setDate(reviewDate.getValue());
            getReview().setRating((int)reviewRater.getValue());
            getReview().setUser(reviewUser.getText());
            getReview().setReview(reviewDescription.getText());
            DataManager.updateDB(getReview());
            setReview(DataManager.getReview(getReview().getId()));
            setTab(TAB.MEDIA);
        });
        reviewDelete.setOnAction(e->{
            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getReview());
                setTab(TAB.MEDIA);
            }
        });
        reviewCancel.setOnAction(e->{
            setTab(TAB.MEDIA);
        });
    }
    
}

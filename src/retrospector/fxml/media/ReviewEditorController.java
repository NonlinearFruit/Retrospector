/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.media;

import retrospector.fxml.review.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import retrospector.fxml.core.CoreController.TAB;
import static retrospector.fxml.core.CoreController.ratingFormat;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.ControlFxTextFieldModifier;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ReviewEditorController implements Initializable {

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
    private Runnable showReviewList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            showReviewList();
        });
        reviewDelete.setOnAction(e->{
            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getReview());
                showReviewList();
            }
        });
        reviewCancel.setOnAction(e->{
            showReviewList();
        });
    }    
    
    private Media getMedia(){
        return currentMedia.get();
    }
    
    public void setup(Runnable showReviewList, ObjectProperty<Media> m, ObjectProperty<Review> r){
        this.showReviewList = showReviewList;
        currentMedia = m;
        currentReview = r;
    }
    
    private void showReviewList() {
        showReviewList.run();
    }
    
    private void setMedia(Media m){
        currentMedia.set(m);
    }
    
    private void setReview(Review r){
        currentReview.set(r);
    }
    
    private Review getReview(){
        Review r = currentReview.get();
        return r;
    }
    
    public void update(){
        reviewRater.setValue(getReview().getRating().doubleValue());
        Platform.runLater(()->reviewRater.requestFocus());
        reviewDescription.setText(getReview().getReview());
        reviewUser.setText(getReview().getUser());
        reviewDate.setValue(getReview().getDate());
    }
}

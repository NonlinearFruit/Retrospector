/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.media;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import retrospector.fxml.core.CoreController;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ReviewListController implements Initializable {

    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> userColumn;
    @FXML
    private TableColumn<Review, LocalDate> dateColumn;
    
    private ObjectProperty<Media> currentMedia;
    private ObjectProperty<Review> currentReview;
    private ObjectProperty<CoreController.TAB> currentTab;
    private Runnable showReviewEditor;

    public void setup(Runnable showReviewEditor, ObjectProperty<Media> m, ObjectProperty<Review> r){
        this.showReviewEditor = showReviewEditor;
        currentMedia = m;
        currentReview = r;
    }
    
    private Review getReview() {
        return currentReview.get();
    }
    
    private void setReview(Review r) {
        currentReview.set(r);
    }
    
    private Media getMedia() {
        if (currentMedia == null)
            return new Media();
        return currentMedia.get();
    }
    
    private void showReviewEditor() {
        showReviewEditor.run();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        reviewTable.setPlaceholder(new Text("^ Click 'New' to create a Review"));
        reviewTable.getSelectionModel().selectedItemProperty().addListener((observe, old, neo) -> {
            setReview(neo);
        });
        reviewTable.setRowFactory(tv -> {
            // Display 'X Days Ago'
            TableRow<Review> row = new TableRow<Review>() {
                private Tooltip tooltip = new Tooltip();

                @Override
                public void updateItem(Review review, boolean empty) {
                    super.updateItem(review, empty);
                    if (review == null) {
                        setTooltip(null);
                    } else {
                        long age = ChronoUnit.DAYS.between(review.getDate(), LocalDate.now());
                        tooltip.setText(age + " days ago");
                        setTooltip(tooltip);
                    }
                }
            };
            // Quickly switch to editing a review
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    showReviewEditor();
                }
            });
            return row;
        });
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("User"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }    
    
    public void update() {
        List<Review> reviews = DataManager.getMedia(getMedia().getId()).getReviews();
        reviewTable.setItems(FXCollections.observableArrayList(reviews));
        reviewTable.refresh();
        if (reviewTable.getItems().size() > 0) {
            reviewTable.getSelectionModel().select(0);
        }
    }

    @FXML
    private void createNewReview(ActionEvent event) {
        Review review = new Review();
        review.setMediaId(getMedia().getId());
        review.setId(DataManager.createDB(review));
        setReview(review);
        showReviewEditor();
    }

    @FXML
    private void editCurrentReview(ActionEvent event) {
        showReviewEditor();
    }

    @FXML
    private void deleteCurrentReview(ActionEvent event) {
        if (new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this?", ButtonType.NO, ButtonType.YES).showAndWait().get().equals(ButtonType.YES)) {
            DataManager.deleteDB(getReview());
            update();
        }
    }
    
}

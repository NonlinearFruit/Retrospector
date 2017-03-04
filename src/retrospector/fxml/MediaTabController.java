/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class MediaTabController implements Initializable {

    @FXML
    private HBox mediaTitleBox;
    @FXML
    private TextField mediaTitle;
    @FXML
    private Button mediaNewMedia;
    @FXML
    private TextField mediaCreator;
    @FXML
    private HBox mediaSeasonBox;
    @FXML
    private TextField mediaSeason;
    @FXML
    private Button mediaAddSeason;
    @FXML
    private HBox mediaEpisodeBox;
    @FXML
    private TextField mediaEpisode;
    @FXML
    private Button mediaAddEpisode;
    @FXML
    private Rating mediaStars;
    @FXML
    private Text mediaRating;
    @FXML
    private Text mediaMaxRating;
    @FXML
    private ChoiceBox<?> mediaCategory;
    @FXML
    private ChoiceBox<?> mediaType;
    @FXML
    private TextArea mediaDescription;
    @FXML
    private Button mediaNewReview;
    @FXML
    private Button mediaEditReview;
    @FXML
    private Button mediaDeleteReview;
    @FXML
    private TableView<?> mediaReviewTable;
    @FXML
    private TableColumn<?, ?> mediaRatingColumn;
    @FXML
    private TableColumn<?, ?> mediaUserColumn;
    @FXML
    private TableColumn<?, ?> mediaDateColumn;
    @FXML
    private Button mediaSave;
    @FXML
    private Button mediaDelete;
    @FXML
    private Button mediaCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

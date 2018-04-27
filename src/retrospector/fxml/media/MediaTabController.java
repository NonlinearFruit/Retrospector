/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.media;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
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
import retrospector.fxml.core.CoreController.TAB;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import java.util.function.Consumer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.script.ScriptException;
import org.controlsfx.control.PopOver;
import retrospector.Retrospector;
import retrospector.fxml.core.CoreController;
import retrospector.model.Factoid;
import retrospector.util.ControlFxTextFieldModifier;
import retrospector.util.Toast;

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
    private Button mediaSave;
    @FXML
    private Button mediaDelete;
    @FXML
    private Button mediaCancel;
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
    @FXML
    private Button nextBtn;
    @FXML
    private Button prevBtn;
    @FXML
    private Button mediaNewFactoid;
    @FXML
    private Button mediaEditFactoid;
    @FXML
    private Button mediaDeleteFactoid;
    @FXML
    private TableView<Factoid> mediaFactoidTable;
    @FXML
    private TableColumn<Factoid, String> mediaTitleColumn;
    @FXML
    private TableColumn<Factoid, String> mediaContentColumn;
    @FXML
    private ChoiceBox<String> mediaTitleFactoid;
    @FXML
    private TextField mediaContentFactoid;
    @FXML
    private Button mediaSaveFactoid;
    @FXML
    private VBox reviewBox;
    @FXML
    private Button autofillBtn;
    
    private final ObjectProperty<Review> currentReview = new SimpleObjectProperty<>();
    private ObjectProperty<Media> currentMedia;
    private ObjectProperty<TAB> currentTab;
    private ObjectProperty<Factoid> currentFactoid;
    private Consumer<Integer> nextPreviousFunct;
    
    private ReviewEditorController reviewEditorController;
    private Parent reviewEditor;
    private ReviewListController reviewListController;
    private Parent reviewList;
    private RollOver reviewSwapper;
    private PopOver autofillPopOver;
    private ListView<String> scrapeListView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMediaTab();
    }    
    
    public void setup(ObjectProperty<TAB> t, ObjectProperty<Media> m, Consumer<Integer> np){
        currentTab = t;
        currentMedia = m;
        nextPreviousFunct = np;
        reviewEditorController.setup(()->{reviewListController.update();reviewSwapper.showFront();}, currentMedia, currentReview);
        reviewListController.setup(()->{reviewEditorController.update();reviewSwapper.showBack();}, currentMedia, currentReview);
        currentTab.addListener((observe,old,neo) -> {
//            if (old == TAB.MEDIA && neo != TAB.MEDIA) {
            if (old == TAB.MEDIA) {
                reviewSwapper.showFront();
            }
        });
    }
    
    public void update(){
        updateMediaTab();
    }
    
    private void setTab(TAB t){
        currentTab.set(t);
    }
    
    private Review getReview(){
        return currentReview.get();
    }
    
    private void setReview(Review r){
        currentReview.set(r);
    }

    public Media getMedia(){
        return currentMedia.get();
    }
    
    private void setMedia(Media m){
        currentMedia.setValue(m);
    }
    
    private Media pullMedia() {
        Media m = new Media();
        m.clone(getMedia());
        m.setTitle(mediaTitle.getText());
        m.setCreator(mediaCreator.getText());
        m.setSeason(mediaSeason.getText());
        m.setEpisode(mediaEpisode.getText());
        m.setCategory(mediaCategory.getValue());
        m.setType(mediaType.getValue());
        m.setDescription(mediaDescription.getText());
        return m;
    }
    
    private void setFactoid(Factoid factoid) {
        currentFactoid.set(factoid);
        if (factoid == null) {
            mediaTitleFactoid.getSelectionModel().clearSelection();
            mediaContentFactoid.setText("");
        } else {
            mediaTitleFactoid.getSelectionModel().select(factoid.getTitle());
            mediaContentFactoid.setText(factoid.getContent());
        }
    }
    
    private Factoid getFactoid() {
        if(currentFactoid.get() == null) {
            currentFactoid.set(new Factoid());
            currentFactoid.get().setMediaId(getMedia().getId());
        }
        currentFactoid.get().setTitle(mediaTitleFactoid.getValue());
        currentFactoid.get().setContent(mediaContentFactoid.getText());
        return currentFactoid.get();
    }
    
    private void updateFactoid() {
        Media media = DataManager.getMedia(getMedia().getId());
        updateFactoid(media.getFactoids());
    }
    
    private void updateFactoid(List<Factoid> factoids) {
        setFactoid(null);
        mediaFactoidTable.setItems(FXCollections.observableArrayList(factoids));
        mediaFactoidTable.refresh();
        if (mediaFactoidTable.getItems().size() > 0) {
            mediaFactoidTable.getSelectionModel().select(0);
        }
    }
    
    private void updateMediaTab(){
        // Media Stuff
        setMedia(DataManager.getMedia(getMedia().getId()));
        mediaTitle.setText(getMedia().getTitle());
        mediaCreator.setText(getMedia().getCreator());
        mediaSeason.setText(getMedia().getSeason());
        mediaEpisode.setText(getMedia().getEpisode());
        Double rating = getMedia().getAverageRating();
        mediaStars.setRating(rating/2.0);
        mediaRating.setText(CoreController.ratingFormat.format(rating));
        
        mediaCategory.setValue(getMedia().getCategory());
        mediaType.setValue(getMedia().getType());
        mediaDescription.setText(getMedia().getDescription());
        mediaSave.setDisable(true);
        
        // Factoid Stuff
        updateFactoid(getMedia().getFactoids());
        
        // Review Stuff
//        reviewEditorController.update();
        reviewListController.update();
    }
    
    private void initMediaTab(){
        // Rating Stuff
        mediaStars.setPartialRating(true);
        mediaStars.setDisable(true);
        mediaStars.setMax(DataManager.getMaxRating()/2);
        mediaStars.setRating(DataManager.getDefaultRating()/2);
        mediaRating.setText(CoreController.ratingFormat.format(DataManager.getDefaultRating()));
        mediaMaxRating.setText(CoreController.ratingFormat.format(DataManager.getMaxRating()));
        mediaCategory.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        mediaCategory.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            
        });
        
        // Media Stuff
        mediaTitle.textProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        ControlFxTextFieldModifier.autocompleteMe(mediaTitle, DataManager.getTitles());
        mediaCreator.textProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        ControlFxTextFieldModifier.autocompleteMe(mediaCreator, DataManager.getCreators());
        mediaSeason.textProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        ControlFxTextFieldModifier.autocompleteMe(mediaSeason, DataManager.getSeasons());
        mediaEpisode.textProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        ControlFxTextFieldModifier.autocompleteMe(mediaEpisode, DataManager.getEpisodes());
        ControlFxTextFieldModifier.autocompleteMe(mediaContentFactoid, DataManager.getFactoidContents());
        mediaDescription.textProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        mediaCategory.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->mediaSave.setDisable(false));
        mediaType.setItems(FXCollections.observableArrayList(Media.Type.SINGLE, Media.Type.MINISERIES, Media.Type.SERIES));
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
        
        // Buttons
        mediaSave.setOnAction(e->{
            Media m = pullMedia();
            DataManager.updateDB(m);
            setMedia(m);
            mediaSave.setDisable(true);
        });
        mediaDelete.setOnAction(e->{
            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getMedia());
                setTab(TAB.SEARCH);
            }
        });
        mediaCancel.setOnAction(e->{
            setTab(TAB.SEARCH);
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
            int id = DataManager.createDB(media);
            for (Factoid factoid : getMedia().getFactoids()) {
                Factoid fact = new Factoid(factoid.getTitle(),factoid.getContent());
                fact.setMediaId(id);
                DataManager.createDB(fact);
            }
            setMedia(DataManager.getMedia(id));
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
            media.setSeason(getMedia().getSeason());
            int id = DataManager.createDB(media);
            for (Factoid factoid : getMedia().getFactoids()) {
                Factoid fact = new Factoid(factoid.getTitle(),factoid.getContent());
                fact.setMediaId(id);
                DataManager.createDB(fact);
            }
            setMedia(DataManager.getMedia(id));
            if(getMedia().getId()==-1)
                System.err.println("Media got a -1 id (mediaAddEpisode#setOnAction");
            updateMediaTab();
            mediaEpisode.requestFocus();
        });
        
        nextBtn.setOnAction(e->{
            nextPreviousFunct.accept(1);
            update();
        });
        
        prevBtn.setOnAction(e->{
            nextPreviousFunct.accept(-1);
            update();
        });
        
        // Factoid Stuff
        mediaFactoidTable.setPlaceholder(new Text("^ Click 'New' to add a Factoid"));
        mediaTitleFactoid.setItems(FXCollections.observableArrayList(DataManager.getFactiodTypes()));
        mediaTitleFactoid.setDisable(true);
        mediaContentFactoid.setDisable(true);
        mediaSaveFactoid.setDisable(true);
        mediaEditFactoid.setDisable(true);
        mediaDeleteFactoid.setDisable(true);
        currentFactoid = new SimpleObjectProperty<>();
        currentFactoid.addListener((observe,old,neo)->{
            boolean tf = false;
            if (neo==null)
                tf = true;
            mediaTitleFactoid.setDisable(tf);
            mediaContentFactoid.setDisable(tf);
            mediaSaveFactoid.setDisable(tf);
            mediaEditFactoid.setDisable(tf);
            mediaDeleteFactoid.setDisable(tf);
        });
//        ControlFxTextFieldModifier.autocompleteMe(mediaContentFactoid, DataManager.getFactoids().stream().distinct().map(f->f.getContent()).collect(Collectors.toList()));
//        mediaTitleFactoid.onActionProperty().addListener(e -> {
//            ControlFxTextFieldModifier.autocompleteMe(mediaContentFactoid, DataManager.getFactoidsByType(mediaTitleFactoid.getValue()).stream().map(f->f.getContent()).collect(Collectors.toList()));
//        });
        mediaFactoidTable.getSelectionModel().selectedItemProperty().addListener((observe, old, neo)->{
            boolean tf = false;
            if (neo==null)
                tf = true;
            mediaEditFactoid.setDisable(tf);
            mediaDeleteFactoid.setDisable(tf);
        });
        mediaFactoidTable.setRowFactory(tv -> {
            TableRow<Factoid> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    setFactoid(row.getItem());
                }
            });
            return row;
        });
        mediaTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        mediaContentColumn.setCellValueFactory(new PropertyValueFactory<>("Content"));
        
        mediaNewFactoid.setOnAction(e->{
            Factoid factoid = new Factoid();
            factoid.setMediaId(getMedia().getId());
            factoid.setId(DataManager.createDB(factoid));
            mediaFactoidTable.getItems().add(factoid);
            setFactoid(factoid);
        });
        mediaEditFactoid.setOnAction(e->{
            setFactoid(mediaFactoidTable.getSelectionModel().getSelectedItem());
        });
        mediaDeleteFactoid.setOnAction(e->{
            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(mediaFactoidTable.getSelectionModel().getSelectedItem());
                updateFactoid();
            }
        });
        mediaSaveFactoid.setOnAction(e->{
            Factoid factoid = getFactoid();
            if (factoid.getId() > 0)
                DataManager.updateDB(factoid);
            else
                DataManager.createDB(factoid);
            updateFactoid();
        });
        
        // Review Stuff
        try{
            FXMLLoader rlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/media/ReviewList.fxml"));
            Parent reviewList = (Parent) rlLoader.load();
            reviewListController = (ReviewListController) rlLoader.getController();
            
            FXMLLoader reLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/media/ReviewEditor.fxml"));
            Parent reviewEditor = (Parent) reLoader.load();
            reviewEditorController = (ReviewEditorController) reLoader.getController();
            
            reviewSwapper = new RollOver(reviewList,reviewEditor);
            reviewBox.getChildren().add(reviewSwapper);
            
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        // Autofill Stuff
        Font fa = Font.loadFont(Retrospector.class.getResourceAsStream("res/fontawesome-webfont.ttf"), 15);
        autofillBtn.setText(""); // Download symbol
        autofillBtn.setFont(fa);
        scrapeListView = new ListView();
        scrapeListView.setOnMouseClicked((click)-> {
                if (click.getClickCount() == 2) {
                    String file = scrapeListView.getSelectionModel().getSelectedItem();
                    autofillPopOver.hide();
                    autofill(file);
                }
        });
        autofillPopOver = new PopOver(scrapeListView);
        autofillPopOver.setAutoHide(true);
        autofillPopOver.setAutoFix(true);
        autofillPopOver.setHideOnEscape(true);
        autofillPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
    }
    
    public void reviewEditor() {
        reviewEditorController.update();
    }

    @FXML
    private void autofill(ActionEvent event) {
        scrapeListView.setItems(FXCollections.observableArrayList(JsDataScraper.getJsFiles()));
        autofillPopOver.show(autofillBtn);
    }
    
    private void autofill(String file) {
        Media m = pullMedia();
        try {
            JsDataScraper scraper = JsDataScraper.getScraper(file); 
            m = scraper.autocomplete(m);
            DataManager.updateDB(m);
            setMedia(m);
            updateMediaTab();
        } catch (ScriptException se) {
            Toast.makeText((Stage) reviewBox.getScene().getWindow(), "Autofill failed", 3000, 500, 500);
            se.printStackTrace();
        }
    }
}

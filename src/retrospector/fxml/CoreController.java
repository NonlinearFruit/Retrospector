/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.Rating;
import retrospector.model.*;
import static retrospector.model.Media.Type.SERIES;
import retrospector.util.Dumpster;
import retrospector.util.NaturalOrderComparator;
import retrospector.util.PropertyManager;
import retrospector.util.Stroolean;
import retrospector.util.UtilityCloset;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class CoreController implements Initializable {

    @FXML
    private TabPane anchorCenter;
    @FXML
    private Tab searchTab;
    @FXML
    private TextField searchBox;
    @FXML
    private TableView<Media> searchTable;
    @FXML
    private MenuButton searchNewMedia;
    @FXML
    private Button searchEditMedia;
    @FXML
    private Button searchDeleteMedia;
    @FXML
    private TableColumn<Media, Integer> searchNumberColumn;
    @FXML
    private TableColumn<Media, String> searchTitleColumn;
    @FXML
    private TableColumn<Media, String> searchCreatorColumn;
    @FXML
    private TableColumn<Media, String> searchSeasonColumn;
    @FXML
    private TableColumn<Media, String> searchEpisodeColumn;
    @FXML
    private TableColumn<Media, Integer> searchReviewsColumn;
    @FXML
    private TableColumn<Media, String> searchCategoryColumn;
    @FXML
    private TableColumn<Media, ?> searchRatingColumns;
    @FXML
    private TableColumn<Media, BigDecimal> searchMeanRColumn;
    @FXML
    private TableColumn<Media, BigDecimal> searchCurrentRColumn;
    @FXML
    private MenuItem searchQuickEntry;
    @FXML
    private MenuItem searchStandardEntry;
    @FXML
    private Tab mediaTab;
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
    @FXML
    private Tab reviewTab;
    @FXML
    private Tab chartTab;
    @FXML
    private ListView<Stroolean> listIncludeList;
    @FXML
    private TableView<Media> listTable;
    @FXML
    private ToggleButton listTop10;
    @FXML
    private ToggleButton listTop100;
    @FXML
    private ToggleButton listTop1000;
    @FXML
    private TextField listYear;
    @FXML
    private DatePicker listStartDate;
    @FXML
    private DatePicker listEndDate;
    @FXML
    private TextField listUser;
    @FXML
    private TableColumn<Media, String> listTitleColumn;
    @FXML
    private TableColumn<Media, String> listCreatorColumn;
    @FXML
    private TableColumn<Media, String> listSeasonColumn;
    @FXML
    private TableColumn<Media, String> listEpisodeColumn;
    @FXML
    private TableColumn<Media, String> listCategoryColumn;
    @FXML
    private TableColumn<Media, Integer> listReviewsColumn;
    @FXML
    private TableColumn<Media, BigDecimal> listRatingColumn;
    @FXML
    private TableColumn<Media, Integer> listRankColumn;
    @FXML
    private RadioButton listCustomDateRange;
    @FXML
    private ToggleButton listGroupCreator;
    @FXML
    private ToggleButton listGroupTitle;
    @FXML
    private ToggleButton listGroupSeason;
    @FXML
    private ToggleButton listGroupEpisode;
    @FXML
    private RadioButton listUseAllTime;
    @FXML
    private RadioButton listUseYear;
    @FXML
    private Tab tropeTab;
    @FXML
    private WebView tropeWebView;
    
    public final ObjectProperty<Media> currentMedia = new SimpleObjectProperty<>();
    private ObjectProperty<Review> currentReview = new SimpleObjectProperty<>();
    private DecimalFormat ratingFormat =  new DecimalFormat("#.#");
    private String tropeHome = "http://tvtropes.org";
    private ObservableList<Media> searchTableData;

    private StatsTabController statsTab;
    public void setStatsTab(FXMLLoader ldr){ chartTab.setContent(ldr.getRoot());statsTab = ldr.getController(); }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // This is just for test, try not to put it in a really release
        if(DataManager.getMedia().size()==0)
            Dumpster.createMedia(1000);
        
        initSearchTab();
        initMediaTab();
        initReviewTab();
        
        // List
        initListTab();
        // Tropes
        if(PropertyManager.loadProperties().isEnableTrope())initTropeTab();
        else anchorCenter.getTabs().remove(tropeTab);
        
        
        anchorCenter.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            if(neo.getText().equals("Search"))
                updateSearchTab();
            else if(neo.getText().equals("Media"))
                updateMediaTab();
            else if(neo.getText().equals("Review"))
                updateReviewTab();
            else if(neo.getText().equals("Chart"))
                statsTab.update(currentMedia.get());
//                updateChartTab();
            else if(neo.getText().equals("List"))
                updateListTab();
            else if(neo.getText().equals("Tropes"))
                updateTropes();
        });
        
        currentMedia.addListener((observe, old, neo)->{
            if(neo==null){
                mediaTab.setDisable(true);
                reviewTab.setDisable(true);
                chartTab.setDisable(true);
                searchEditMedia.setDisable(true);
                searchDeleteMedia.setDisable(true);
            } else {
                mediaTab.setDisable(false);
                chartTab.setDisable(false);
                searchEditMedia.setDisable(false);
                searchDeleteMedia.setDisable(false);
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
        searchEditMedia.setDisable(true);
        searchDeleteMedia.setDisable(true);
        try{
            new URL(tropeHome).openConnection();
        }catch(IOException e){tropeTab.setDisable(true);}
        
        updateSearchTab();
    }    
    
    public void updateSearchTab(){
        searchTableData.clear();
        searchTableData.addAll(DataManager.getMedia());
        searchTable.refresh();
        if(searchTable.getItems().contains(getMedia()))
            searchTable.getSelectionModel().select(getMedia());
        else if(searchTable.getItems().size()>0)
            searchTable.getSelectionModel().select(0);
    }
    
    public void initSearchTab(){
        searchTableData = DataManager.getMedia();
        FilteredList<Media> mediaFiltered = new FilteredList(searchTableData,x->true);
        SortedList<Media> mediaSortable = new SortedList<>(mediaFiltered);
        searchTable.setItems(mediaSortable);
        mediaSortable.comparatorProperty().bind(searchTable.comparatorProperty());
        
        // Link to data properties
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Title"));
        searchCreatorColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Creator"));
        searchSeasonColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("SeasonId"));
        searchEpisodeColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("EpisodeId"));
        searchCategoryColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Category"));
        
        // Values for special columns
        searchNumberColumn.setSortable(false);
        searchNumberColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1+searchTable.getItems().indexOf(p.getValue())));
        searchReviewsColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Media,Integer> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getReviews().size());
            }
        });
        searchMeanRColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getAverageRating());
            }
        });
//        searchOriginalRColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
//            @Override
//            public ObservableValue<BigDecimal> call(CellDataFeatures<Media,BigDecimal> p) {
//                return new ReadOnlyObjectWrapper(p.getValue().getOriginalRating());
//            }
//        });
        searchCurrentRColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getCurrentRating());
            }
        });
        
        // Comparors for string columns
        searchTitleColumn.setComparator(new NaturalOrderComparator());
        searchCreatorColumn.setComparator(new NaturalOrderComparator());
        searchSeasonColumn.setComparator(new NaturalOrderComparator());
        searchEpisodeColumn.setComparator(new NaturalOrderComparator());
        searchCategoryColumn.setComparator(new NaturalOrderComparator());
        
        searchTable.getSelectionModel().selectedItemProperty().addListener( (observe, old, neo) -> {
            setMedia(neo);
        });
        
        searchBox.textProperty().addListener((observa,old,neo)->{
            String query = neo.toLowerCase();
            if(query==null || query.equals(""))
                mediaFiltered.setPredicate(x->true);
            else{
                String[] queries = query.split(":");
                mediaFiltered.setPredicate(x->{
                    boolean pass = true;
                    for (String q : queries) {
                        if(     
                                !x.getTitle().toLowerCase().contains(q) &&
                                !x.getCreator().toLowerCase().contains(q) &&
                                !x.getSeasonId().toLowerCase().contains(q) &&
                                !x.getEpisodeId().toLowerCase().contains(q) &&
                                !x.getDescription().toLowerCase().contains(q) &&
                                !x.getCategory().toString().toLowerCase().contains(q) &&
                                !x.getType().toString().toLowerCase().contains(q)
                        )
                            pass = false;
                    }
                    return pass;
                });
            }
        });
        
        searchNewMedia.setOnAction(e->{
            Media neo = new Media();
            neo.setId(DataManager.createDB(neo));
            setMedia(neo);
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        searchQuickEntry.setOnAction(e->{
                  try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QuickEntry.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    QuickEntryController qec = fxmlLoader.getController();
                    qec.setCore(this);
                    Stage stage = new Stage();
//                    stage.initModality(Modality.APPLICATION_MODAL);
//                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setTitle("Quick Entry");
                    stage.setScene(new Scene(root1));  
                    stage.show();
                  } catch(Exception ex) {}
        });
        searchStandardEntry.setOnAction(e->{
            Media neo = new Media();
            neo.setId(DataManager.createDB(neo));
            setMedia(neo);
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        searchEditMedia.setOnAction(e->{
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        searchDeleteMedia.setOnAction(e->{
            if(new Alert(AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getMedia());
                updateSearchTab();
            }
        });
    }
    
    public void setMedia(Media media){
        currentMedia.set(media);
    }
    
    public Media getMedia(){
        return currentMedia.get();
    }
    
    public void updateMediaTab(){
        setMedia(DataManager.getMedia(getMedia().getId()));
        mediaTitle.setText(getMedia().getTitle());
        mediaCreator.setText(getMedia().getCreator());
        mediaSeason.setText(getMedia().getSeasonId());
        mediaEpisode.setText(getMedia().getEpisodeId());
        BigDecimal rating = getMedia().getAverageRating();
        mediaStars.setRating(rating.divide(BigDecimal.valueOf(2)).doubleValue());
        mediaRating.setText(ratingFormat.format(rating));
        
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
        mediaRating.setText(ratingFormat.format(DataManager.getDefaultRating()));
        mediaMaxRating.setText(ratingFormat.format(DataManager.getMaxRating()));
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
            setReview(neo);
        });
        mediaRatingColumn.setCellValueFactory(new PropertyValueFactory<Review,Double>("Rating"));
        mediaUserColumn.setCellValueFactory(new PropertyValueFactory<Review,String>("User"));
        mediaDateColumn.setCellValueFactory(new PropertyValueFactory<Review,LocalDate>("Date"));
        
        mediaNewReview.setOnAction(e->{
            Review review = new Review();
            review.setMediaId(getMedia().getId());
            review.setId(DataManager.createDB(review));
            setReview(review);
            anchorCenter.getSelectionModel().select(reviewTab);
        });
        mediaEditReview.setOnAction(e->{
            anchorCenter.getSelectionModel().select(reviewTab);
        });
        mediaDeleteReview.setOnAction(e->{
            if(new Alert(AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getReview());
                updateMediaTab();
            }
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
            if(new Alert(AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getMedia());
                anchorCenter.getSelectionModel().select(searchTab);
            }
        });
        mediaCancel.setOnAction(e->{
            anchorCenter.getSelectionModel().select(searchTab);
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
    
    public void setReview(Review review){
        currentReview.set(review);
    }
    
    public Review getReview(){
        return currentReview.get();
    }
    
    public void updateReviewTab(){
        reviewTitle.setText(getMedia().getTitle());
        reviewCreator.setText(getMedia().getCreator());
        reviewSeason.setText(getMedia().getSeasonId());
        reviewEpisode.setText(getMedia().getEpisodeId());
        reviewRater.setValue(getReview().getRating().doubleValue());
        reviewDescription.setText(getReview().getReview());
        reviewUser.setText(getReview().getUser());
        reviewDate.setValue(getReview().getDate());
    }
    
    public void initReviewTab(){
        reviewStars.setPartialRating(true);
        reviewStars.setDisable(true);
        reviewStars.setMax(DataManager.getMaxRating()/2);
        reviewStars.ratingProperty().bind(
                Bindings.divide(reviewRater.valueProperty(), 2)
        );
        reviewRating.textProperty().bind(
                Bindings.createStringBinding(()->ratingFormat.format(reviewRater.valueProperty().getValue()), reviewRater.valueProperty())
        );
        
        reviewSave.setOnAction(e->{
            getReview().setDate(reviewDate.getValue());
            getReview().setRating(BigDecimal.valueOf(reviewRater.getValue()).round(new MathContext(2, RoundingMode.HALF_UP)));
            getReview().setUser(reviewUser.getText());
            getReview().setReview(reviewDescription.getText());
            DataManager.updateDB(getReview());
            setReview(DataManager.getReview(getReview().getId()));
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        reviewDelete.setOnAction(e->{
            if(new Alert(AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getReview());
                anchorCenter.getSelectionModel().select(mediaTab);
            }
        });
        reviewCancel.setOnAction(e->{
            anchorCenter.getSelectionModel().select(mediaTab);
        });
    }

// Chart tab outsourced to StatsTabController.java
    
    private ObservableList<Stroolean> strooleans = FXCollections.observableArrayList();
    private ObservableList<Media> listTableData = FXCollections.observableArrayList();
    private ToggleGroup dateToggleGroup = new ToggleGroup();
    
    public void updateListTab(){
        listTableData.clear();
        if(!listGroupCreator.isSelected())
            return;
        
        boolean creator = listGroupCreator.isSelected();
        boolean title = listGroupTitle.isSelected();
        boolean season = listGroupSeason.isSelected();
        boolean episode = listGroupEpisode.isSelected();
        Chartagories chartagory = episode? Chartagories.CURRENT_MEDIA:
                                  season?  Chartagories.SEASON:
                                  title?   Chartagories.TITLE:
                                           Chartagories.CREATOR;
        
        Integer top = listTop10.isSelected()?  10:
                      listTop100.isSelected()? 100:
                                               1000;
        
        LocalDate start = listCustomDateRange.isSelected()? listStartDate.getValue() : LocalDate.of(Integer.parseInt(listYear.getText())-1, 12, 31);
            start = listUseAllTime.isSelected()? LocalDate.MIN : start;
        LocalDate end = listCustomDateRange.isSelected()? listEndDate.getValue() : LocalDate.of(Integer.parseInt(listYear.getText())+1, 1, 1);
            end = listUseAllTime.isSelected()? LocalDate.MAX : end;
        
        String user = listUser.getText();
        
        for (Media media : DataManager.getMedia()) {
            for (Stroolean stroolean : strooleans) {
                if(stroolean.isBoolean() && media.getCategory().equals(stroolean.getString())){
                    boolean homeless = true;
                    for (Media data : listTableData) {
                        Media temp = new Media();
                        temp.clone(media);
                        temp.setCategory(data.getCategory());
                        temp.setType(data.getType());
                        if(UtilityCloset.isSameMedia(chartagory, data, temp)){
                            homeless = false;
                            for (Review review : media.getReviews()) {
                                if(review.getDate().isBefore(end) && 
                                   review.getDate().isAfter(start)&&
                                   review.getUser().equals(user)){
                                        data.getReviews().add(review);
                                }
                            }
                            break;
                        }
                    }
                    if(homeless){
                        Media m = new Media();
                        if(creator)
                            m.setCreator(media.getCreator());
                        if(title)
                            m.setTitle(media.getTitle());
                        if(season)
                            m.setSeasonId(media.getSeasonId());
                        if(episode)
                            m.setEpisodeId(media.getEpisodeId());
                        m.setCategory(media.getCategory());
                        for (Review review : media.getReviews()) {
                            if(review.getUser().equals(user)){
                                if(review.getDate().isBefore(end) && 
                                   review.getDate().isAfter(start)){
                                        m.getReviews().add(review);
                                } else if(review.getDate().isEqual(start) || review.getDate().isEqual(end)){
                                        m.getReviews().add(review);
                                }
                            }
                        }
                        listTableData.add(m);
                    }
                }
            }
        }
        List rankedResults = listTableData.stream()
                    .sorted((x,y)->y.getAverageRating().compareTo(x.getAverageRating()))
                    .limit(top)
                    .collect(Collectors.toList());
        listTable.setItems(FXCollections.observableArrayList(rankedResults));
        listRankColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1+rankedResults.indexOf(p.getValue())));
        listTable.refresh();
    }
    
    public void initListTab(){
        // Include
        for (String category : DataManager.getCategories()) {
            Stroolean c = new Stroolean(category);
            c.booleanProperty().addListener((observe,old,neo)->updateListTab());
            strooleans.add(c);
            listIncludeList.getItems().add(c);
        }
        listIncludeList.setCellFactory(CheckBoxListCell.forListView(Stroolean::booleanProperty));
        
        // Group By
        listGroupCreator.setSelected(true);
        listGroupCreator.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                // Nothing to select, top of the food chain
            } else {
                listGroupTitle.setSelected(false);
                listGroupSeason.setSelected(false);
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupTitle.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
            } else {
                listGroupSeason.setSelected(false);
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupSeason.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
                listGroupTitle.setSelected(true);
            } else {
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupEpisode.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
                listGroupTitle.setSelected(true);
                listGroupSeason.setSelected(true);
            } else {
                // Nothing to deselect, bottom of the food chain
            }
            updateListTab();
        });
        
        
        // Top 10/0/0
        listTop10.setSelected(true);
        listTop10.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop100.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop100.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop1000.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop100.setSelected(false);
                listTop10.setSelected(false);
            }
        });
        
        // Dates
        listYear.setText(String.valueOf(LocalDate.now().getYear()));
        listYear.setOnAction(e->updateListTab());
        listStartDate.setValue(LocalDate.now().withMonth(1).withDayOfMonth(1));
        listStartDate.valueProperty().addListener((observe,old,neo)->updateListTab());
        listEndDate.setValue(LocalDate.now().withMonth(12).withDayOfMonth(31));
        listEndDate.valueProperty().addListener((observe,old,neo)->updateListTab());
        listUseAllTime.selectedProperty().addListener((observe,old,neo)->updateListTab());
        listUseYear.selectedProperty().addListener((observe,old,neo)->updateListTab());
        listCustomDateRange.selectedProperty().addListener((observe,old,neo)->updateListTab());
        //  - Enable/Disble
        dateToggleGroup.getToggles().addAll(listUseYear,listUseAllTime,listCustomDateRange);
        dateToggleGroup.selectToggle(listUseAllTime);
        listYear.disableProperty().bind(Bindings.not(listUseYear.selectedProperty()));
        listStartDate.disableProperty().bind(Bindings.not(listCustomDateRange.selectedProperty()));
        listEndDate.disableProperty().bind(Bindings.not(listCustomDateRange.selectedProperty()));
        
        // User
        listUser.setText(DataManager.getDefaultUser());
        listUser.setOnAction(e->updateListTab());
        
        // Table
        listTable.setItems(listTableData);
        
        // Link to Properties
        listTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        listCreatorColumn.setCellValueFactory(new PropertyValueFactory<>("Creator"));
        listSeasonColumn.setCellValueFactory(new PropertyValueFactory<>("SeasonId"));
        listEpisodeColumn.setCellValueFactory(new PropertyValueFactory<>("EpisodeId"));
        listCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        
        // Special Table Cells
        listReviewsColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getReviews().size()) );
        listRatingColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getAverageRating()) );
        
        // Comparors for string columns
        listTitleColumn.setComparator(new NaturalOrderComparator());
        listCreatorColumn.setComparator(new NaturalOrderComparator());
        listSeasonColumn.setComparator(new NaturalOrderComparator());
        listEpisodeColumn.setComparator(new NaturalOrderComparator());
        listCategoryColumn.setComparator(new NaturalOrderComparator());
    }
    

    private void updateTropes() {
        // Do nothing
    }

    private void initTropeTab() {
        tropeWebView.getEngine().load(tropeHome);
    }
    
    public static enum Chartagories{
        CURRENT_MEDIA,SEASON,TITLE,CREATOR,CATEGORY
    }
}

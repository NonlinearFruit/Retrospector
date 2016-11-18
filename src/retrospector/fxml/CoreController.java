/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.Rating;
import retrospector.model.*;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class CoreController implements Initializable {

    @FXML
    private Tab searchTab;
    @FXML
    private TextField searchBox;
    @FXML
    private TableView<Media> searchTable;
    @FXML
    private Button searchNewMedia;
    @FXML
    private Button searchEditMedia;
    @FXML
    private Button searchDeleteMedia;
    @FXML
    private TableColumn<Media, String> searchTitleColumn;
    @FXML
    private TableColumn<Media, String> searchCreatorColumn;
    @FXML
    private TableColumn<Media, String> searchSeasonColumn;
    @FXML
    private TableColumn<Media, String> searchEpisodeColumn;
    @FXML
    private TableColumn<Media, String> searchRatingColumn;
    @FXML
    private TableColumn<Media, Integer> searchReviewsColumn;
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
    private ChoiceBox<Media.Category> mediaCategory;
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
    private ToggleButton chartRatings;
    @FXML
    private ToggleButton chartReviews;
    @FXML
    private ToggleButton chartTime;
    @FXML
    private ToggleButton chartUsers;
    @FXML
    private Button chartShow;
    @FXML
    private VBox chartVBox;
    @FXML
    private Text chartTotalMedia;
    @FXML
    private Text chartTotalReviews;
    @FXML
    private Text chartTotalUsers;
    @FXML
    private Text chartTotalRuntime;
    
    private Media currentMedia;
    private Review currentReview;
    private DecimalFormat ratingFormat =  new DecimalFormat("#.#");
    @FXML
    private TabPane anchor;
    @FXML
    private Tab reviewTab;
    @FXML
    private Tab chartTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initSearchTab();
        initMediaTab();
        initReviewTab();
        initChartTab();
    }    
    
    public void updateSearchTab(){
        searchTable.refresh();
        updateChartTab();
    }
    
    public void initSearchTab(){
        FilteredList<Media> media = new FilteredList(DataManager.getMedia(),x->true);
        searchTable.setItems(media);
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Title"));
        searchSeasonColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("SeasonId"));
        searchEpisodeColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("EpisodeId"));
        searchCreatorColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Creator"));
        searchReviewsColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Media,Integer> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getReviews().size());
            }
        });
        searchRatingColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Media,String> p) {
                return new ReadOnlyObjectWrapper(ratingFormat.format(p.getValue().getAverageRating()));
            }
        });
        searchTable.getSelectionModel().selectedItemProperty().addListener( (observe, old, neo) -> {
            setMedia(neo);
        });
        
        searchBox.setOnKeyReleased(e->{
            String query = searchBox.getText();
            if(query==null || query.equals(""))
                media.setPredicate(x->true);
            else{
                String[] queries = query.split(":");
                media.setPredicate(x->{
                    boolean pass = true;
                    for (String q : queries) {
                        if(     
                                !x.getTitle().contains(q) &&
                                !x.getCreator().contains(q) &&
                                !x.getSeasonId().contains(q) &&
                                !x.getEpisodeId().contains(q) &&
                                !x.getDescription().contains(q) &&
                                !x.getCategory().toString().contains(q) &&
                                !x.getType().toString().contains(q)
                        )
                            pass = false;
                    }
                    return pass;
                });
            }
        });
        
        searchNewMedia.setOnAction(e->{
            Media neo = new Media();
            DataManager.getMedia().add(neo);
            setMedia(neo);
            anchor.getSelectionModel().select(mediaTab);
        });
        searchEditMedia.setOnAction(e->{
            anchor.getSelectionModel().select(mediaTab);
        });
        searchDeleteMedia.setOnAction(e->{
            DataManager.getMedia().remove(currentMedia);
            updateSearchTab();
        });
    }
    
    public void setMedia(Media media){
        currentMedia = media;
        updateMediaTab();
    }
    
    public void updateMediaTab(){
        mediaTitle.setText(currentMedia.getTitle());
        mediaCreator.setText(currentMedia.getCreator());
        mediaSeason.setText(currentMedia.getSeasonId());
        mediaEpisode.setText(currentMedia.getEpisodeId());
        double rating = currentMedia.getAverageRating();
        mediaStars.setRating(rating/2);
        mediaRating.setText(ratingFormat.format(rating));
        
        mediaCategory.setValue(currentMedia.getCategory());
        mediaType.setValue(currentMedia.getType());
        mediaDescription.setText(currentMedia.getDescription());
        
        mediaReviewTable.setItems(FXCollections.observableArrayList(currentMedia.getReviews()));
        mediaReviewTable.refresh();
        updateSearchTab();
    }
    
    public void initMediaTab(){
        mediaStars.setPartialRating(true);
        mediaStars.setDisable(true);
        mediaStars.setMax(DataManager.getMaxRating()/2);
        mediaStars.setRating(DataManager.getDefaultRating());
        mediaRating.setText(ratingFormat.format(DataManager.getDefaultRating()));
        mediaMaxRating.setText(ratingFormat.format(DataManager.getMaxRating()));
        mediaCategory.setItems(FXCollections.observableArrayList(Media.Category.values()));
        mediaType.setItems(FXCollections.observableArrayList(Media.Type.values()));
        
        mediaReviewTable.getSelectionModel().selectedItemProperty().addListener((observe, old, neo)->{
            setReview(neo);
        });
        mediaRatingColumn.setCellValueFactory(new PropertyValueFactory<Review,Double>("Rating"));
        mediaUserColumn.setCellValueFactory(new PropertyValueFactory<Review,String>("User"));
        mediaDateColumn.setCellValueFactory(new PropertyValueFactory<Review,LocalDate>("Date"));
        
        mediaNewReview.setOnAction(e->{
            Review review = new Review();
            currentMedia.getReviews().add(review);
            updateMediaTab();
            setReview(review);
            anchor.getSelectionModel().select(reviewTab);
        });
        mediaEditReview.setOnAction(e->{
            anchor.getSelectionModel().select(reviewTab);
        });
        mediaDeleteReview.setOnAction(e->{
            currentMedia.getReviews().remove(currentReview);
            updateMediaTab();
        });
        
        mediaSave.setOnAction(e->{
            currentMedia.setTitle(mediaTitle.getText());
            currentMedia.setCreator(mediaCreator.getText());
            currentMedia.setSeasonId(mediaSeason.getText());
            currentMedia.setEpisodeId(mediaEpisode.getText());
            currentMedia.setCategory(mediaCategory.getValue());
            currentMedia.setType(mediaType.getValue());
            currentMedia.setDescription(mediaDescription.getText());
            updateMediaTab();
            anchor.getSelectionModel().select(searchTab);
        });
        mediaDelete.setOnAction(e->{
            DataManager.getReviews().remove(currentMedia);
            updateSearchTab();
            anchor.getSelectionModel().select(searchTab);
        });
        mediaCancel.setOnAction(e->{
            anchor.getSelectionModel().select(searchTab);
        });
    }
    
    public void setReview(Review review){
        currentReview = review;
        updateReviewTab();
    }
    
    public void updateReviewTab(){
        reviewTitle.setText(currentMedia.getTitle());
        reviewCreator.setText(currentMedia.getCreator());
        reviewSeason.setText(currentMedia.getSeasonId());
        reviewEpisode.setText(currentMedia.getEpisodeId());
        reviewRater.setValue(currentReview.getRating());
        reviewDescription.setText(currentReview.getReview());
        reviewUser.setText(currentReview.getUser());
        reviewDate.setValue(currentReview.getDate());
        updateMediaTab();
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
            currentReview.setDate(reviewDate.getValue());
            currentReview.setRating(reviewRater.getValue());
            currentReview.setUser(reviewUser.getText());
            currentReview.setReview(reviewDescription.getText());
            updateMediaTab();
            anchor.getSelectionModel().select(mediaTab);
        });
        reviewDelete.setOnAction(e->{
            currentMedia.getReviews().remove(currentReview);
            updateMediaTab();
            anchor.getSelectionModel().select(mediaTab);
        });
        reviewCancel.setOnAction(e->{
            anchor.getSelectionModel().select(mediaTab);
        });
    }
    
    public void updateChartTab(){
        
        // Chart
        // chartUsers, chartReviews, chartRating, chartTime
        boolean users = chartUsers.isSelected(),
                reviews = chartReviews.isSelected(),
                ratings = chartRatings.isSelected(),
                time = chartTime.isSelected();
        
        chartVBox.getChildren().clear();
        if(!users && reviews && ratings && !time){
            double one=0, two=0, three=0, four=0, five=0;
            for (Review review : currentMedia.getReviews()) {
                double rating = review.getRating();
                if(rating<2)
                    one+=1;
                else if(rating<4)
                    two+=1;
                else if(rating<6)
                    three+=1;
                else if(rating<8)
                    four+=1;
                else
                    five+=1;
            }
            Series series = new Series();
            series.getData().addAll(
                    new Data(one,"One Star"),
                    new Data(two,"Two Star"),
                    new Data(three,"Three Star"),
                    new Data(four,"Four Star"),
                    new Data(five,"Five Star")
            );
            BarChart<String,Integer> graph = new BarChart(new NumberAxis(),new CategoryAxis(FXCollections.observableArrayList("One Star","Two Star","Three Star","Four Star","Five Star")));
            graph.getData().add(series);
            chartVBox.getChildren().add(graph);
        }
        
        // Stats at Bottom
        chartTotalMedia.setText(String.valueOf(DataManager.getReviews().size()));
        chartTotalReviews.setText(String.valueOf(DataManager.getReviews().size()));
        chartTotalUsers.setText(String.valueOf(DataManager.getUsers().size()));
        
        LocalDate oldest = DataManager.getReviews().get(0).getDate();
        for (Review review : DataManager.getReviews()) {
            if(review.getDate().isBefore(oldest))
                oldest = review.getDate();
        }
        
        long days = ChronoUnit.DAYS.between(oldest, LocalDate.now())+1;
        chartTotalRuntime.setText(days+" days");
    }
    
    public void initChartTab(){
        chartShow.setOnAction(e->{
            updateChartTab();
        });
        updateChartTab();
    }
    
}

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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.Rating;
import retrospector.model.*;
import static retrospector.model.Media.Type.SERIES;
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
    private TableColumn<Media, Integer> searchReviewsColumn;
    @FXML
    private TableColumn<Media, String> searchCategoryColumn;
    @FXML
    private TableColumn<Media, ?> searchRatingColumns;
    @FXML
    private TableColumn<Media, BigDecimal> searchOriginalRColumn;
    @FXML
    private TableColumn<Media, BigDecimal> searchMeanRColumn;
    @FXML
    private TableColumn<Media, BigDecimal> searchCurrentRColumn;
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
    private ToggleButton chartRatings;
    @FXML
    private ToggleButton chartReviews;
    @FXML
    private ToggleButton chartTime;
    @FXML
    private ToggleButton chartUsers;
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
    @FXML
    private Tab chartTab;
    @FXML
    private ChoiceBox<Chartagories> chartChoiceBox;
    @FXML
    private Tab tropeTab;
    @FXML
    private WebView tropeWebView;
    
    private ObjectProperty<Media> currentMedia = new SimpleObjectProperty<>();
    private ObjectProperty<Review> currentReview = new SimpleObjectProperty<>();
    private DecimalFormat ratingFormat =  new DecimalFormat("#.#");
    private String tropeHome = "http://tvtropes.org";
    private ObservableList<Media> media;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initSearchTab();
        initMediaTab();
        initReviewTab();
        initChartTab();
        initTropeTab();
        anchorCenter.getSelectionModel().selectedItemProperty().addListener((observe,old,neo)->{
            if(neo.getText().equals("Search"))
                updateSearchTab();
            else if(neo.getText().equals("Media"))
                updateMediaTab();
            else if(neo.getText().equals("Review"))
                updateReviewTab();
            else if(neo.getText().equals("Chart"))
                updateChartTab();
            else
                updateTropes();
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
        
        try{
            new URL(tropeHome).openConnection();
        }catch(IOException e){tropeTab.setDisable(true);}
        
        updateSearchTab();
    }    
    
    public void updateSearchTab(){
        media.clear();
        media.addAll(DataManager.getMedia());
        searchTable.refresh();
        if(searchTable.getItems().contains(getMedia()))
            searchTable.getSelectionModel().select(getMedia());
        else if(searchTable.getItems().size()>0)
            searchTable.getSelectionModel().select(0);
    }
    
    public void initSearchTab(){
        media = DataManager.getMedia();
        FilteredList<Media> mediaFiltered = new FilteredList(media,x->true);
        SortedList<Media> mediaSortable = new SortedList<>(mediaFiltered);
        searchTable.setItems(mediaSortable);
        mediaSortable.comparatorProperty().bind(searchTable.comparatorProperty());
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Title"));
        searchSeasonColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("SeasonId"));
        searchEpisodeColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("EpisodeId"));
        searchCreatorColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Creator"));
        searchCategoryColumn.setCellValueFactory(new PropertyValueFactory<Media,String>("Category"));
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
        searchOriginalRColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getOriginalRating());
            }
        });
        searchCurrentRColumn.setCellValueFactory(new Callback<CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getCurrentRating());
            }
        });
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
        searchEditMedia.setOnAction(e->{
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        searchDeleteMedia.setOnAction(e->{
            DataManager.deleteDB(getMedia());
            updateSearchTab();
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
            DataManager.deleteDB(getReview());
            updateMediaTab();
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
            DataManager.deleteDB(getMedia());
            anchorCenter.getSelectionModel().select(searchTab);
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
            DataManager.deleteDB(getReview());
            anchorCenter.getSelectionModel().select(mediaTab);
        });
        reviewCancel.setOnAction(e->{
            anchorCenter.getSelectionModel().select(mediaTab);
        });
    }

    private void updateTropes() {
        // Do nothing
    }

    private void initTropeTab() {
        tropeWebView.getEngine().load(tropeHome);
    }
    
    public static enum Chartagories{
        CURRENT_MEDIA,MINISERIES,SEASON,SERIES,CREATOR,CATEGORY
    }
    
    public void updateChartTab(){
        
        // Chart
        // chartUsers, chartReviews, chartRating, chartTime
        boolean users = chartUsers.isSelected(),
                reviews = chartReviews.isSelected(),
                ratings = chartRatings.isSelected(),
                time = chartTime.isSelected();
        
        chartVBox.getChildren().clear();
        
        ObservableList<Review> setOfReviews = FXCollections.observableArrayList();

        Chartagories category = chartChoiceBox.getSelectionModel().getSelectedItem();
        if(category.equals(Chartagories.CURRENT_MEDIA))
            setOfReviews.addAll(getMedia().getReviews());
        else
            for (Media media : DataManager.getMedia()) {
                if(UtilityCloset.isSameMedia(category, getMedia(), media))
                    setOfReviews.addAll(media.getReviews());
            }
        
        // User :: Review :: Rating :: Time
        
        // 0    :: 0      :: 0      :: 0
        if(!users && !reviews && !ratings && !time){
            PieChart chart = new PieChart();
            chart.setTitle("Media per Category");
            chart.setData(
                    FXCollections.observableArrayList(
                        Arrays.asList(
                                DataManager.getCategories()).stream()
                                .map(c->
                                        new PieChart.Data(
                                                c, 
                                                DataManager.getMedia().stream()
                                                    .filter(m->c.equals(m.getCategory()))
                                                    .count()
                                            )
                                    )
                                .collect(Collectors.toList()
                            )
                        )
            );
            chartVBox.getChildren().add(chart);
        }
        
        // 0    :: 0      :: 0      :: 1
        if(!users && !reviews && !ratings && time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 0    :: 0      :: 1      :: 0
        if(!users && !reviews && ratings && !time){
            chartVBox.getChildren().add(new Text("Average Rating: "+
                    setOfReviews.stream()
                    .collect(Collectors.averagingInt(r->r.getRating().intValueExact()))
                    ));
        }
        
        // 0    :: 0      :: 1      :: 1 see 0111
        
        // 0    :: 1      :: 0      :: 0
        if(!users && reviews && !ratings && !time){
            chartVBox.getChildren().add(new Text("Number of Reviews: "+setOfReviews.size()));
        }
        
        // 0    :: 1      :: 0      :: 1
        if(!users && reviews && !ratings && time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 0    :: 1      :: 1      :: 0
        else if(!users && reviews && ratings && !time){
            int one=0, two=0, three=0, four=0, five=0;
            for (Review review : setOfReviews) {
                BigDecimal rating = review.getRating();
                if(rating.compareTo(BigDecimal.valueOf(2))<0)
                    one+=1;
                else if(rating.compareTo(BigDecimal.valueOf(4))<0)
                    two+=1;
                else if(rating.compareTo(BigDecimal.valueOf(6))<0)
                    three+=1;
                else if(rating.compareTo(BigDecimal.valueOf(8))<0)
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
            graph.setTitle("Number of Reviews per Rating");
            graph.getData().add(series);
            chartVBox.getChildren().add(graph);
        }
        
        // 0    :: 1      :: 1      :: 1  && 0    :: 0      :: 1      :: 1  
        else if(!users /*&& reviews*/ && ratings && time){
            NumberAxis xAxis = new NumberAxis();
            Axis yAxis = new NumberAxis(0.0,5.0,1.0);
            yAxis.setLabel("Rating");
            xAxis.setAutoRanging(true);
            xAxis.setForceZeroInRange(false);
            xAxis.setLabel("Time");
            xAxis.setTickLabelFormatter(new StringConverter<Number>() {
                @Override
                public String toString(Number day) {
                    return LocalDate.ofEpochDay(day.longValue()).format(DateTimeFormatter.ISO_LOCAL_DATE);
                }

                @Override
                public Number fromString(String date) {
                    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).toEpochDay();
                }
            });
            LineChart graph = new LineChart(xAxis,yAxis);
            Series series = new Series();
            int one=0, two=0, three=0, four=0, five=0;
            for (Review review : setOfReviews) {
                BigDecimal rating = review.getRating();
                series.getData().add(new Data(review.getDate().toEpochDay(),rating.divide(new BigDecimal(2),2,RoundingMode.HALF_UP)));
            }
            graph.setTitle("Ratings over Time");
            graph.getData().add(series);
            chartVBox.getChildren().add(graph);
        }
        
        // 1    :: 0      :: 0      :: 0
        if(users && !reviews && !ratings && !time){
            chartVBox.getChildren().add(new Text("Number of users: "+
                    setOfReviews.stream()
                    .map(r->r.getUser())
                    .distinct()
                    .toArray()
                    .length
            ));
        }
        
        // 1    :: 0      :: 0      :: 1
        if(users && !reviews && !ratings && time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 1    :: 0      :: 1      :: 0
        if(users && !reviews && ratings && !time){
            NumberAxis xAxis = new NumberAxis(0,5,1);
            BarChart<BigDecimal,String> graph = new BarChart(xAxis,new CategoryAxis());
            graph.setTitle("Average Rating per User");
            Series<BigDecimal,String> series = new Series<>();
            for (String user : DataManager.getUsers()) {
                BigDecimal mean = BigDecimal.ZERO;
                BigDecimal count = BigDecimal.ONE;
                for (Review review : setOfReviews) {
                    if(user.equals(review.getUser())){
                        count = count.add(BigDecimal.ONE);
                        mean = mean.add(review.getRating());
                    }
                }
                mean = mean.divide(count,2,RoundingMode.HALF_UP);
                series.getData().add(new Data(mean,user));
            }
            graph.getData().add(series);
            chartVBox.getChildren().add(graph);
        }
        
        // 1    :: 0      :: 1      :: 1
        if(users && !reviews && ratings && time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 1    :: 1      :: 0      :: 0
        if(users && reviews && !ratings && !time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 1    :: 1      :: 0      :: 1
        if(users && reviews && !ratings && time){
            chartVBox.getChildren().add(new Text("Not implemented yet"));
        }
        
        // 1    :: 1      :: 1      :: 0
        else if(users && reviews && ratings && !time){
            BarChart<String,Integer> graph = new BarChart(new NumberAxis(),new CategoryAxis(FXCollections.observableArrayList("One Star","Two Star","Three Star","Four Star","Five Star")));
            for (String user : DataManager.getUsers()) {
                int one=0, two=0, three=0, four=0, five=0;
                for (Review review : setOfReviews) {
                    BigDecimal rating = review.getRating();
                    if(user.equals(review.getUser())){
                        if(rating.compareTo(BigDecimal.valueOf(2))<0)
                            one+=1;
                        else if(rating.compareTo(BigDecimal.valueOf(4))<0)
                            two+=1;
                        else if(rating.compareTo(BigDecimal.valueOf(6))<0)
                            three+=1;
                        else if(rating.compareTo(BigDecimal.valueOf(8))<0)
                            four+=1;
                        else
                            five+=1;
                    }
                }
                Series series = new Series();
                series.setName(user);
                series.getData().addAll(
                        new Data(one,"One Star"),
                        new Data(two,"Two Star"),
                        new Data(three,"Three Star"),
                        new Data(four,"Four Star"),
                        new Data(five,"Five Star")
                );
                graph.getData().add(series);
            }
            chartVBox.getChildren().add(graph);
        }
        
        // 1    :: 1      :: 1      :: 1
        else if(users && reviews && ratings && time){
            NumberAxis xAxis = new NumberAxis();
            Axis yAxis = new NumberAxis(0.0,5.0,1.0);
            yAxis.setLabel("Rating");
            xAxis.setAutoRanging(true);
            xAxis.setForceZeroInRange(false);
            xAxis.setLabel("Time");
            xAxis.setTickLabelFormatter(new StringConverter<Number>() {
                @Override
                public String toString(Number day) {
                    return LocalDate.ofEpochDay(day.longValue()).format(DateTimeFormatter.ISO_LOCAL_DATE);
                }

                @Override
                public Number fromString(String date) {
                    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).toEpochDay();
                }
            });
            LineChart graph = new LineChart(xAxis,yAxis);
            graph.setTitle("Number of Reviews per Rating per User");
            for (String user : DataManager.getUsers()) {
                Series series = new Series();
                series.setName(user);
                int one=0, two=0, three=0, four=0, five=0;
                for (Review review : setOfReviews) {
                    BigDecimal rating = review.getRating();
                    if(user.equals(review.getUser())){
                        series.getData().add(new Data(review.getDate().toEpochDay(),rating.divide(new BigDecimal(2),2,RoundingMode.HALF_UP)));
                    }
                }
                graph.getData().add(series);
            }
            chartVBox.getChildren().add(graph);
        }
        // Stats at Bottom
        chartTotalMedia.setText(String.valueOf(DataManager.getMedia().size()));
        chartTotalReviews.setText(String.valueOf(DataManager.getReviews().size()));
        chartTotalUsers.setText(String.valueOf(DataManager.getUsers().size()));
        
        if(DataManager.getReviews().size()<1){
            chartTotalRuntime.setText("0 days");
            return;
        }
        LocalDate oldest = DataManager.getReviews().get(0).getDate();
        for (Review review : DataManager.getReviews()) {
            if(review.getDate().isBefore(oldest))
                oldest = review.getDate();
        }
        
        long days = ChronoUnit.DAYS.between(oldest, LocalDate.now())+1;
        chartTotalRuntime.setText(days+" days");
    }
    
    public void initChartTab(){
        chartChoiceBox.setItems(FXCollections.observableArrayList(Chartagories.values()));
        chartChoiceBox.getSelectionModel().select(1);
        chartRatings.setOnAction(e->{
            updateChartTab();
        });
        chartReviews.setOnAction(e->{
            updateChartTab();
        });
        chartUsers.setOnAction(e->{
            updateChartTab();
        });
        chartTime.setOnAction(e->{
            updateChartTab();
        });
        chartChoiceBox.setOnAction(e->{
            updateChartTab();
        });
    }
    
}

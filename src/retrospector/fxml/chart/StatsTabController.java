/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import retrospector.fxml.CoreController.TAB;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.NaturalOrderComparator;
import retrospector.util.Stroolean;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class StatsTabController implements Initializable {

    private ObservableList<Stroolean> strooleans = FXCollections.observableArrayList();
    private ObjectProperty<Media> currentMedia;
    private ObjectProperty<TAB> currentTab;
    private ObservableList<Media> allMedia = FXCollections.observableArrayList();
    private FilteredList<Media> mediaTableFilter = new FilteredList(allMedia);
    private String[] colors = new String[]{"red","orange","yellowgreen","seagreen","lightseagreen","skyblue","royalblue","grey","mediumpurple","palevioletred","firebrick"};
    
    @FXML
    private LineChart<Number, Number> chartRatingOverTime;
    @FXML
    private PieChart chartMediaPerCategory;
    @FXML
    private ChoiceBox<String> categorySelector;
    @FXML
    private CheckBox checkTitle;
    @FXML
    private CheckBox checkCreator;
    @FXML
    private CheckBox checkSeason;
    @FXML
    private CheckBox checkEpisode;
    @FXML
    private CheckBox checkCategory;
    @FXML
    private Text overallMedia;
    @FXML
    private Text overallReview;
    @FXML
    private Text overallUser;
    @FXML
    private Text overallTime;
    @FXML
    private Text overallPerMonth;
    @FXML
    private Text categoryMedia;
    @FXML
    private Text categoryUser;
    @FXML
    private Text categoryTime;
    @FXML
    private Text categorySingle;
    @FXML
    private Text categoryMiniseries;
    @FXML
    private Text categorySeries;
    @FXML
    private Text categoryPerMonth;
    @FXML
    private TableView<Media> mediaTable;
    @FXML
    private TableColumn<Media, Integer> mediaColumnRowNumber;
    @FXML
    private TableColumn<Media, String> mediaColumnTitle;
    @FXML
    private TableColumn<Media, String> mediaColumnCreator;
    @FXML
    private TableColumn<Media, String> mediaColumnSeason;
    @FXML
    private TableColumn<Media, String> mediaColumnEpisode;
    @FXML
    private TableColumn<Media, String> mediaColumnCategory;
    @FXML
    private Text mediaMedia;
    @FXML
    private Text mediaReview;
    @FXML
    private Text mediaUser;
    @FXML
    private Text mediaTime;
    @FXML
    private Text mediaCurrentRating;
    @FXML
    private Text mediaAllRating;
    @FXML
    private Text overallSingle;
    @FXML
    private Text overallMiniseries;
    @FXML
    private Text overallSeries;
    @FXML
    private Text overallCurrentRating;
    @FXML
    private Text overallAllRating;
    @FXML
    private Text categoryCurrentRating;
    @FXML
    private Text categoryAllRating;
    @FXML
    private Text mediaSeries;
    @FXML
    private Text mediaMiniseries;
    @FXML
    private Text mediaPerMonth;
    @FXML
    private Text categoryReview;
    @FXML
    private Text mediaSingle;
    @FXML
    private LineChart<Number, Number> chartReviewsPerYear;
    @FXML
    private Text overallTitle;
    @FXML
    private Text overallCreator;
    @FXML
    private Text categoryTitle;
    @FXML
    private Text categoryCreator;
    @FXML
    private Text mediaTitle;
    @FXML
    private Text mediaCreator;
    @FXML
    private StackedBarChart<String, Number> chartReviewsPerDay;
    @FXML
    private BarChart<String, Number> chartReviewsPerRating;
    @FXML
    private NumberAxis chartRotY;
    @FXML
    private NumberAxis chartRotX;
    @FXML
    private NumberAxis chartRpdY;
    @FXML
    private CategoryAxis chartRpdX;
    @FXML
    private NumberAxis chartRprY;
    @FXML
    private CategoryAxis chartRprX;
    @FXML
    private NumberAxis chartRpyY;
    @FXML
    private CategoryAxis chartRpyX;
    @FXML
    private ListView<Stroolean> overallUserList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Collections.rotate(Arrays.asList(colors), -5);
        // Bug Work Around
        chartReviewsPerYear.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        chartReviewsPerDay.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        chartReviewsPerRating.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        // Overall
        for (String user : DataManager.getUsers()) {
            Stroolean c = new Stroolean(user,true);
            c.booleanProperty().addListener((observe,old,neo)->update());
            strooleans.add(c);
            overallUserList.getItems().add(c);
        }
        overallUserList.setCellFactory(CheckBoxListCell.forListView(Stroolean::booleanProperty));
        chartMediaPerCategory.setLegendVisible(true);
        chartRpdX.setLabel("Days");
        chartRpdY.setLabel("Reviews");
        // Category
        categorySelector.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        categorySelector.setValue(DataManager.getCategories()[0]);
        categorySelector.valueProperty().addListener((observe,old,neo)->updateCategory());
        chartReviewsPerRating.setLegendVisible(false);
        chartRprX.setLabel("Ratings");
        chartRprY.setLabel("Reviews");
        chartRpyX.setLabel("Months");
        chartRpyY.setLabel("Reviews");
        // Media
        checkTitle.setSelected(true);
        checkCreator.setSelected(true);
        checkSeason.setSelected(true);
        checkEpisode.setSelected(true);
        checkCategory.setSelected(true);
        checkTitle.selectedProperty().addListener((observe,old,neo)->updateMedia());
        checkCreator.selectedProperty().addListener((observe,old,neo)->updateMedia());
        checkSeason.selectedProperty().addListener((observe,old,neo)->updateMedia());
        checkEpisode.selectedProperty().addListener((observe,old,neo)->updateMedia());
        checkCategory.selectedProperty().addListener((observe,old,neo)->updateMedia());
        mediaTableFilter = new FilteredList(allMedia);
        SortedList<Media> mediaSortable = new SortedList<>(mediaTableFilter);
        mediaTable.setItems(mediaSortable);
        mediaSortable.comparatorProperty().bind(mediaTable.comparatorProperty());
        mediaColumnRowNumber.setSortable(false);
        mediaColumnRowNumber.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1+mediaTable.getItems().indexOf(p.getValue())));
        mediaColumnTitle.setComparator(new NaturalOrderComparator());
        mediaColumnCreator.setComparator(new NaturalOrderComparator());
        mediaColumnSeason.setComparator(new NaturalOrderComparator());
        mediaColumnEpisode.setComparator(new NaturalOrderComparator());
        mediaColumnCategory.setComparator(new NaturalOrderComparator());
        mediaColumnTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        mediaColumnCreator.setCellValueFactory(new PropertyValueFactory<>("Creator"));
        mediaColumnSeason.setCellValueFactory(new PropertyValueFactory<>("SeasonId"));
        mediaColumnEpisode.setCellValueFactory(new PropertyValueFactory<>("EpisodeId"));
        mediaColumnCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        chartRotY.setLabel("Reviews");
        chartRotY.setAutoRanging(false);
        chartRotY.setLowerBound(0);
        chartRotY.setUpperBound(10);
        chartRotY.setTickUnit(2);
        chartRotY.setMinorTickCount(2);
        chartRotX.setLabel("Time");
        chartRotX.setAutoRanging(false);
        chartRotX.setTickUnit(1);
        chartRotX.setMinorTickCount(4);
        chartRotX.setTickLabelFormatter(new StringConverter<Number>(){
            @Override
            public String toString(Number number) {
                double x = number.doubleValue();
                double decimal = x%1;
                double year = x - decimal;
                double days = decimal*365.25;
                if(days>365 || days<1)
                    return ((int)year)+"";
                LocalDate date = LocalDate.ofYearDay((int)year, (int)days);
                return date.format(DateTimeFormatter.ofPattern("MMM uuuu"));
            }

            @Override
            public Number fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void setup(ObjectProperty<TAB> aTab, ObjectProperty<Media> aMedia){
        currentMedia = aMedia;
        currentTab = aTab;
    }
    
    private Media getMedia(){
        return currentMedia.get();
    }
    
       
    public void update(Media media){
        currentMedia.set(media);
        checkTitle.setText("Title: "+media.getTitle());
        checkCreator.setText("Creator: "+media.getCreator());
        checkSeason.setText("Season: "+media.getSeasonId());
        checkEpisode.setText("Episode: "+media.getEpisodeId());
        checkCategory.setText("Category: "+media.getCategory());
        categorySelector.setValue(media.getCategory());
        update();
    }
    public void update(){
        allMedia.clear();
        allMedia.addAll(DataManager.getMedia());
        Platform.runLater(()->updateOverall());
        Platform.runLater(()->updateCategory());
        Platform.runLater(()->updateMedia());
    }
    
    private void updateOverall(){
        
        // Constants
        int last__days = 20;
        
        // Graph Title
        chartReviewsPerDay.setTitle("Past "+last__days+" Days");
        
        // Data Mining - Vars
        Set<String> titleSet = new HashSet<>();
        Set<String> creatorSet = new HashSet<>();
        Set<String> userSet = new HashSet<>();
        Map<String, Integer> categories = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> last30Days = new HashMap<>();
        int media = 0;
        int reviews = 0;
        int users = 0;
        int titles = 0;
        int creators = 0;
        double aveAll = 0;
        double aveCurrent = 0;
        LocalDate earliest = LocalDate.now();
        int singles = 0;
        int minis = 0;
        int series = 0;
        long days = 0;
        double perMonth = 0;
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            boolean used = false;
            for (Review r : m.getReviews()) {
                if (strooleans.filtered(x->x.getString().equalsIgnoreCase(r.getUser())).get(0).isBoolean()) {
                    if(r.getDate().isBefore(earliest))
                        earliest = r.getDate();
                    if(ChronoUnit.DAYS.between(r.getDate(), LocalDate.now())<=last__days){
                        Map<String,Integer> cat2Num = last30Days.getOrDefault(r.getDate(), new HashMap<>());
                        Integer num = cat2Num.getOrDefault(m.getCategory(), 1);
                        cat2Num.put(m.getCategory(), num+1);
                        last30Days.put(r.getDate(), cat2Num);
                    }
                    userSet.add(r.getUser());
                    aveAll += r.getRating().intValue();
                    reviews++;
                    used = true;
                }
            }
            if (used) {
                switch(m.getType()){
                    case SINGLE:singles++;break;
                    case MINISERIES:minis++;break;
                    case SERIES:series++;break;
                }
                aveCurrent += m.getCurrentRating().intValue();
                titleSet.add(m.getTitle()+m.getCreator());
                creatorSet.add(m.getCreator());
                categories.put(m.getCategory(), categories.getOrDefault(m.getCategory(), 0)+1);
                media++;
            }
        }
        users = userSet.size();
        titles = titleSet.size();
        creators = creatorSet.size();
        aveAll = reviews==0? 0 : aveAll/reviews;
        aveCurrent = media==0? 0 : aveCurrent/media;
        days = ChronoUnit.DAYS.between(earliest, LocalDate.now())+1;
        perMonth = days<2? 0 : (media+0.0)/days*30;
        
        // Stats
        overallMedia.setText(media+" Media");
        overallReview.setText(reviews+" Review(s)");
        overallUser.setText(users+" User(s)");  
        overallTime.setText(days+" Days");
        overallTitle.setText(titles + " Titles");
        overallCreator.setText(creators + " Creators");
        overallSingle.setText(singles+" Single(s)");
        overallMiniseries.setText(minis+" Mini(s)");
        overallSeries.setText(series+" Serie(s)");
        overallPerMonth.setText(String.format("%.2f", perMonth)+" / Month");
        overallCurrentRating.setText(String.format("%.2f", aveCurrent)+" Current");
        overallAllRating.setText(String.format("%.2f", aveAll)+" All");
        
        // Chart # Media / Category
        chartMediaPerCategory.setData(
                FXCollections.observableArrayList(
                    Arrays.asList(DataManager.getCategories())
                        .stream()
                        .map(c -> {
                                int count = categories.getOrDefault(c, 0);
                                PieChart.Data data = new PieChart.Data(c + " - " + count, count);
                                return data;
                            }
                        )
                        .collect(Collectors.toList())
                )
        );
        for (PieChart.Data data : chartMediaPerCategory.getData()) {
            String category = data.getName().substring(0,data.getName().indexOf(" - "));
            int i = Arrays.asList(DataManager.getCategories()).indexOf(category);
            data.getNode().setStyle("-fx-pie-color: " + colors[ (i>0?i:0) % colors.length] + ";");
        }
        for (Node node : chartMediaPerCategory.lookupAll("Label.chart-legend-item")) {
            Shape symbol = new Circle(5);
            Label label = (Label) node;
            String category = label.getText().substring(0,label.getText().indexOf(" - "));
            int i = Arrays.asList(DataManager.getCategories()).indexOf(category);
            symbol.setStyle("-fx-fill: " + colors[ (i>0?i:0) % colors.length]);
            label.setGraphic(symbol);
        }
        
        // Chart # Reviews / Day
        ObservableList<XYChart.Series<String,Number>> list = FXCollections.observableArrayList();
        LocalDate now = LocalDate.now();
        for (String category : DataManager.getCategories()) {
            XYChart.Series data = new XYChart.Series();
            data.setName(category);
            for (int i = last__days; i > -1; i--) {
                LocalDate target = now.minusDays(i);
                int count = last30Days.getOrDefault(target, new HashMap<>()).getOrDefault(category, 0);
                String key = target.getDayOfMonth()+"";
                data.getData().add(new XYChart.Data(key,count));
            }
            list.add(data);
        }
        chartReviewsPerDay.setData(list);
        for (Node node : chartReviewsPerDay.lookupAll("Label.chart-legend-item")) {
            Shape symbol = new Rectangle(7,7);
            Label label = (Label) node;
            String category = label.getText();
            int i = Arrays.asList(DataManager.getCategories()).indexOf(category);
            symbol.setStyle("-fx-fill: " + colors[ (i>0?i:0) % colors.length]);
            label.setGraphic(symbol);
        }
        
        for (XYChart.Series<String,Number> serie : chartReviewsPerDay.getData()) {
            String category = serie.getName();
            int i = Arrays.asList(DataManager.getCategories()).indexOf(category);
            for (XYChart.Data<String,Number> data : serie.getData()) {
                data.getNode().setStyle("-fx-background-color: " + colors[ (i>0?i:0) % colors.length] + ";");
            }
        }
    }
    
    private void updateCategory(){
        
        // Category Chooser
        String category = categorySelector.getValue();
        
        // Colors
        int index = Arrays.asList(DataManager.getCategories()).indexOf(category);
        chartReviewsPerYear.setStyle("CHART_COLOR_1: "+colors[ (index>0?index:0) % colors.length]+";");
        chartReviewsPerRating.setStyle("CHART_COLOR_1: "+colors[ (index>0?index:0) % colors.length]+";");
            
        // Data Mining - Vars
        Map<String, Integer> reviewMap = new HashMap<>();
        List<String> userSet = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();
        Set<String> creatorSet = new HashSet<>();
        int[] reviewsPerRating = new int[DataManager.getMaxRating()+1];
        int media = 0;
        int reviews = 0;
        long users = 0;
        int titles = 0;
        int creators = 0;
        double aveAll = 0;
        double aveCurrent = 0;
        LocalDate earliest = LocalDate.now();
        int singles = 0;
        int minis = 0;
        int series = 0;
        long days = 0;
        double perMonth = 0;
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            if(category.equals(m.getCategory())){
                boolean used = false;
                for (Review r : m.getReviews()) {
                    if (strooleans.filtered(x->x.getString().equalsIgnoreCase(r.getUser())).get(0).isBoolean()) {
                        if(r.getDate().isBefore(earliest))
                            earliest = r.getDate();
                        reviewsPerRating[r.getRating().intValue()] += 1;
                        String key = r.getDate().getMonthValue()+"-"+r.getDate().getYear();
                        reviewMap.put(key, reviewMap.getOrDefault(key, 0)+1);
                        aveAll += r.getRating().intValue();
                        userSet.add(r.getUser());
                        reviews++;
                        used = true;
                    }
                }
                if (used) {
                    switch(m.getType()){
                        case SINGLE:singles++;break;
                        case MINISERIES:minis++;break;
                        case SERIES:series++;break;
                    }
                    aveCurrent += m.getCurrentRating().intValue();
                    titleSet.add(m.getTitle()+m.getCreator());
                    creatorSet.add(m.getCreator());
                    media++;
                }
            }
        }
        users = userSet.stream().distinct().count();
        titles = titleSet.size();
        creators = creatorSet.size();
        aveAll = reviews==0? 0 : aveAll/reviews;
        aveCurrent = media==0? 0 : aveCurrent/media;
        days = ChronoUnit.DAYS.between(earliest, LocalDate.now())+1;
        perMonth = days<2? 0 : (media+0.0)/days*30;
        
        
        // Stats
        categoryMedia.setText(media+" Media");
        categoryReview.setText(reviews+" Review(s)");
        categoryUser.setText(users+" User(s)");
        categoryTime.setText(days+" Days");
        categoryTitle.setText(titles + " Titles");
        categoryCreator.setText(creators + " Creators");
        categorySingle.setText(singles+" Single(s)");
        categoryMiniseries.setText(minis+" Mini(s)");
        categorySeries.setText(series+" Serie(s)");
        categoryPerMonth.setText(String.format("%.2f", perMonth)+" / Month");
        categoryCurrentRating.setText(String.format("%.2f", aveCurrent)+" Current");
        categoryAllRating.setText(String.format("%.2f", aveAll)+" All");
        
        // Chart - # Reviewed / Year
        chartReviewsPerYear.setLegendVisible(false);
        chartReviewsPerYear.getData().clear();
        
        XYChart.Series data = new XYChart.Series();
        int year = earliest.getYear();
        int month = earliest.getMonthValue();
        for (int i=0; i <= ChronoUnit.MONTHS.between(earliest, LocalDate.now())+1; i++) {
            String key = month+"-"+year;
            data.getData().add(new XYChart.Data(key, reviewMap.getOrDefault(key, 0)));
            ++month;
            if(month>12){
                month = 1;
                ++year;
            }
            if(year>=LocalDate.now().getYear() && month>LocalDate.now().getMonthValue())
                break;
        }
        
        chartReviewsPerYear.getData().addAll(data);
        
        // Chart - # Reviews / Rating
        data = new XYChart.Series();
        for (int i = 1; i < reviewsPerRating.length; i++) {
            data.getData().add(new XYChart.Data(i+"",reviewsPerRating[i]));
        }
        chartReviewsPerRating.getData().clear();
        chartReviewsPerRating.getData().add(data);
    }
    
    private void updateMedia(){
        
        // Colors
        int index = Arrays.asList(DataManager.getCategories()).indexOf(getMedia().getCategory());
        chartRatingOverTime.setStyle("CHART_COLOR_1: "+colors[ (index>0?index:0) % colors.length]+";");
        
        // Media Filtering
        Boolean title = checkTitle.isSelected();
        Boolean creator = checkCreator.isSelected();
        Boolean season = checkSeason.isSelected();
        Boolean episode = checkEpisode.isSelected();
        Boolean category = checkCategory.isSelected();
        
        // Filter Table
        mediaTableFilter.setPredicate(m->
                ( ( checkTitle.isSelected() && getMedia().getTitle().equals(((Media)m).getTitle()) ) || !checkTitle.isSelected() ) &&
                ( ( checkCreator.isSelected() && getMedia().getCreator().equals(((Media)m).getCreator()) ) || !checkCreator.isSelected() ) &&
                ( ( checkSeason.isSelected() && getMedia().getSeasonId().equals(((Media)m).getSeasonId()) ) || !checkSeason.isSelected() ) &&
                ( ( checkEpisode.isSelected() && getMedia().getEpisodeId().equals(((Media)m).getEpisodeId()) ) || !checkEpisode.isSelected() ) &&
                ( ( checkCategory.isSelected() && getMedia().getCategory().equals(((Media)m).getCategory()) ) || !checkCategory.isSelected() )
        );

        // Data Mining - Vars
        List<String> userSet = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();
        Set<String> creatorSet = new HashSet<>();
        XYChart.Series data = new XYChart.Series();
        int media = 0;
        int reviews = 0;
        long users = 0;
        int titles = 0;
        int creators = 0;
        double aveAll = 0;
        double aveCurrent = 0;
        LocalDate earliest = LocalDate.now();
        int singles = 0;
        int minis = 0;
        int series = 0;
        long days = 0;
        double perMonth = 0;
        
        // Data Mining - Calcs
        for (Media m : mediaTable.getItems()) {
            boolean used = false;
            for (Review r : m.getReviews()) {
                if (strooleans.filtered(x->x.getString().equalsIgnoreCase(r.getUser())).get(0).isBoolean()) {
                    if (r.getDate().isBefore(earliest)) {
                        earliest = r.getDate();
                    }
                    aveAll += r.getRating().intValue();
                    userSet.add(r.getUser());
                    reviews++;
                    data.getData().add(new XYChart.Data(dateToDouble(r.getDate()), r.getRating().intValue()));
                    used = true;
                }
            }
            if (used) {
                switch (m.getType()) {
                    case SINGLE:
                        singles++;
                        break;
                    case MINISERIES:
                        minis++;
                        break;
                    case SERIES:
                        series++;
                        break;
                }
                aveCurrent += m.getCurrentRating().intValue();
                titleSet.add(m.getTitle());
                creatorSet.add(m.getCreator());
                media++;
            }
        }
        users = userSet.stream().distinct().count();
        titles = titleSet.size();
        creators = creatorSet.size();
        aveAll = reviews == 0 ? 0 : aveAll / reviews;
        aveCurrent = media == 0 ? 0 : aveCurrent / media;
        days = ChronoUnit.DAYS.between(earliest, LocalDate.now()) + 1;
        perMonth = days<2 ? 0 : (media + 0.0) / days * 30;

        // Stats
        mediaMedia.setText(media + " Media");
        mediaReview.setText(reviews + " Review(s)");
        mediaUser.setText(users + " User(s)");
        mediaTime.setText(days + " Days");
        mediaTitle.setText(titles + " Titles");
        mediaCreator.setText(creators + " Creators");
        mediaSingle.setText(singles + " Single(s)");
        mediaMiniseries.setText(minis + " Mini(s)");
        mediaSeries.setText(series + " Serie(s)");
        mediaPerMonth.setText(String.format("%.2f", perMonth) + " / Month");
        mediaCurrentRating.setText(String.format("%.2f", aveCurrent) + " Current");
        mediaAllRating.setText(String.format("%.2f", aveAll) + " All");
        
        // Chart
        chartRatingOverTime.getData().clear();
        if(data.getData().size()<1500)
            chartRatingOverTime.getData().add(data);
        chartRotX.setLowerBound(dateToDouble(earliest)-1.0/12);
        chartRotX.setUpperBound(dateToDouble(LocalDate.now())+1.0/12);
    }
    
    // Takes a date and returns the year with decimal value for the
    // percentage of the year that is complete.
    private double dateToDouble(LocalDate date){
        return date.getYear()+(date.getDayOfYear()+0.0)/365.25;
    }
}

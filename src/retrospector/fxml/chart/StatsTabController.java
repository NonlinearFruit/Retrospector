/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import retrospector.fxml.core.CoreController.TAB;
import retrospector.model.DataManager;
import retrospector.model.Factoid;
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
    private String universalCategory = "All";
    private ObjectProperty<TAB> currentTab;
    private ObjectProperty<Media> currentMedia;
    private ObservableList<Media> allMedia = FXCollections.observableArrayList();
    public static final String[] colors = new String[]{"red","orange","yellowgreen","seagreen","lightseagreen","skyblue","royalblue","grey","mediumpurple","palevioletred","firebrick"};
    
    @FXML
    private PieChart chartMediaPerCategory;
    @FXML
    private ChoiceBox<String> categorySelector;
    @FXML
    private LineChart<Number, Number> chartReviewsPerYear;
    @FXML
    private StackedBarChart<String, Number> chartReviewsPerDay;
    @FXML
    private BarChart<String, Number> chartReviewsPerRating;
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
    @FXML
    private HBox overallContainer;
    @FXML
    private HBox categoryContainer;
    @FXML
    private HBox mediaContainer;
    @FXML
    private ChoiceBox<String> factoidSelector;
    @FXML
    private BarChart<String, Number> chartAverageFactRating;
    @FXML
    private BarChart<String, Number> chartNumOfFacts;
    
    private ChartPopupController mediaPerCategorySettings;
    private PopOver mediaPerCategoryPopOver;
    private ChartPopupController reviewPerYearSettings;
    private PopOver reviewPerYearPopOver;
    private FactoidChartPopupController numOfFactsSettings;
    private PopOver numOfFactsPopOver;

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
        chartAverageFactRating.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        chartNumOfFacts.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        // Overall
        setupOverall();
        // Category
        setupCategory();
        // Factoid
        setupFactoid();
    }
    
    public void setupFactoid() {
        factoidSelector.setItems(FXCollections.observableArrayList(DataManager.getFactiodTypes()));
        factoidSelector.setValue(DataManager.getFactiodTypes()[0]);
        factoidSelector.valueProperty().addListener((observe,old,neo)->updateFactoid());
        categorySelector.valueProperty().addListener((observe,old,neo)->updateFactoid());
        chartNumOfFacts.setLegendVisible(false);
        chartAverageFactRating.setLegendVisible(false);
        
        // Pop Overs
        numOfFactsPopOver = new PopOver();
        numOfFactsPopOver.setAutoHide(true);
//        reviewPerYearPopOver.setAutoFix(true);
        numOfFactsPopOver.setHideOnEscape(true);
        numOfFactsPopOver.setDetachable(false);
        numOfFactsPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        chartNumOfFacts.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                numOfFactsPopOver.show(chartNumOfFacts);
            }
        });

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/chart/FactoidChartPopup.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            numOfFactsSettings = fxmlLoader.getController();
            numOfFactsSettings.setup(this::updateFactoid);
            numOfFactsPopOver.setContentNode(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void setupCategory() {
        ObservableList<String> categories = FXCollections.observableArrayList(DataManager.getCategories());
        categories.add(0,universalCategory);
        categorySelector.setItems(categories);
        categorySelector.setValue(DataManager.getCategories()[0]);
        categorySelector.valueProperty().addListener((observe,old,neo)->updateCategory());
        chartReviewsPerRating.setLegendVisible(false);
        chartRprX.setLabel("Rating");
        chartRprY.setLabel("Reviews");
        chartRpyX.setLabel("Month");
        chartRpyY.setLabel("Reviews");
        
        // Pop Overs
        reviewPerYearPopOver = new PopOver();
        reviewPerYearPopOver.setAutoHide(true);
//        reviewPerYearPopOver.setAutoFix(true);
        reviewPerYearPopOver.setHideOnEscape(true);
        reviewPerYearPopOver.setDetachable(false);
        reviewPerYearPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        chartReviewsPerYear.setOnMouseClicked(e->{
            if (e.getClickCount() == 2)
                reviewPerYearPopOver.show(chartReviewsPerYear);
        });
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/chart/ChartPopup.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            reviewPerYearSettings = fxmlLoader.getController();
            reviewPerYearSettings.setup(this::updateCategory);
            reviewPerYearPopOver.setContentNode(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void setupOverall() {
        for (String user : DataManager.getUsers()) {
            addUserToOverallUserList(user);
        }
        overallUserList.setCellFactory(CheckBoxListCell.forListView(Stroolean::booleanProperty));
        overallUserList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Stroolean me = overallUserList.getSelectionModel().getSelectedItem();
                for (Stroolean stroolean : strooleans)
                    stroolean.setBoolean(false);
                me.setBoolean(true);
            }
        });
        chartRpdX.setLabel("Day");
        chartRpdY.setLabel("Reviews");
        chartMediaPerCategory.setLegendVisible(true);
        
        // Pop Overs
        mediaPerCategoryPopOver = new PopOver();
        mediaPerCategoryPopOver.setAutoHide(true);
//        mediaPerCategoryPopOver.setAutoFix(true);
        mediaPerCategoryPopOver.setHideOnEscape(true);
        mediaPerCategoryPopOver.setDetachable(false);
        mediaPerCategoryPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        chartMediaPerCategory.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                mediaPerCategoryPopOver.show(chartMediaPerCategory);
        });
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/retrospector/fxml/chart/ChartPopup.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            mediaPerCategorySettings = fxmlLoader.getController();
            mediaPerCategorySettings.setup(this::updateOverall);
            mediaPerCategoryPopOver.setContentNode(root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void setup(ObjectProperty<TAB> aTab, ObjectProperty<Media> aMedia){
        currentTab = aTab;
        currentMedia = aMedia;
    }
    
    private Media getMedia() {
        return currentMedia.get();
    }
    
    private String getCategory() {
        if (getMedia()!=null)
            return getMedia().getCategory();
        return DataManager.getCategories()[0];
    }

    public void update(){
        allMedia.clear();
        allMedia.addAll(DataManager.getMedia());
        Platform.runLater(()->updateOverall());
        Platform.runLater(()->updateCategory());
        Platform.runLater(()->updateFactoid());
    }
    
    public void addUserToOverallUserList(String user) {
        Stroolean c = new Stroolean(user,false);
        c.booleanProperty().addListener((observe,old,neo)->update());
        strooleans.add(c);
        if ( user.equals(DataManager.getDefaultUser()) ) {
            c.setBoolean(true);
            overallUserList.getItems().add(0,c);
        } else {
            overallUserList.getItems().add(c);
        }
    }
    
    private void updateOverall(){
        
        // Update User List
        for (String user : DataManager.getUsers()) {
            Boolean found = false;
            for (Stroolean stroolean : strooleans)
                if (stroolean.getString().equals(user))
                    found = true;
            if (!found)
                addUserToOverallUserList(user);
        }
        
        // Constants
        int last__days = DataManager.getPastDays();
        
        // Graph Title
        chartReviewsPerDay.setTitle("Past "+last__days+" Days");
        
        // Hide Pop Over
        mediaPerCategoryPopOver.hide();
        
        // Data Mining - Vars
        Map<String, Integer> categories = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> last30Days = new HashMap<>();
        InfoBlipAccumulator info = new InfoBlipAccumulator();
        LocalDate cutoff = mediaPerCategorySettings.getTimeFrame(); // The current cut off date for Media/Category
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            boolean used = false;
            boolean beatCutOff = false;
            for (Review r : m.getReviews()) {
                if (strooleans.stream().anyMatch(x->x.getString().equalsIgnoreCase(r.getUser()) && x.isBoolean())) {
                    if(ChronoUnit.DAYS.between(r.getDate(), LocalDate.now())<=last__days){
                        Map<String,Integer> cat2Num = last30Days.getOrDefault(r.getDate(), new HashMap<>());
                        Integer num = cat2Num.getOrDefault(m.getCategory(), 0);
                        cat2Num.put(m.getCategory(), num+1);
                        last30Days.put(r.getDate(), cat2Num);
                    }
                    if(r.getDate().isAfter(cutoff))
                        beatCutOff = true;
                    info.accumulate(r);
                    used = true;
                }
            }
            if (used) {
                info.accumulate(m);
                if (beatCutOff)
                    categories.put(m.getCategory(), categories.getOrDefault(m.getCategory(), 0)+1);
                for (Factoid f : m.getFactoids())
                    info.accumulate(f);
//                media++;
            }
        }
        if (overallContainer.getChildren().size()>3)
            overallContainer.getChildren().remove(2);
        overallContainer.getChildren().add(2,info.getInfo());
        
        // Chart # Media / Category
        chartMediaPerCategory.setData(
                FXCollections.observableArrayList(
                    Arrays.asList(DataManager.getCategories())
                        .stream()
                        .map(c -> {
                                int count = categories.getOrDefault(c, 0);
                                PieChart.Data data = new PieChart.Data(c + " - " + count, count);
//                                Tooltip.install(data.getNode(), new Tooltip(String.format("%.0f%%",count*100.0/info.getMedia())));
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
            int count = categories.getOrDefault(category, 0);
            label.setText(category+": "+String.format("%.0f%%",count*100.0/info.getMedia()));
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
        
        // Pop Overs
        reviewPerYearPopOver.hide();
        
        // Colors
        int index = Arrays.asList(DataManager.getCategories()).indexOf(category);
        if (category.equals(universalCategory))
            index = colors.length-1;
        String color = colors[ (index>0?index:0) % colors.length];
        chartReviewsPerYear.setStyle("CHART_COLOR_1: "+color+";");
        chartReviewsPerRating.setStyle("CHART_COLOR_1: "+color+";");
            
        // Data Mining - Vars
        Map<LocalDate, Integer> reviewMap = new TreeMap<>();
        int[] reviewsPerRating = new int[DataManager.getMaxRating()+1];
        InfoBlipAccumulator info = new InfoBlipAccumulator();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM'-'yyyy");
        LocalDate cutoff = reviewPerYearSettings.getTimeFrame(); // The current cut off date for Review/Year
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            if(category.equals(m.getCategory()) || category.equals(universalCategory)){
                boolean used = false;
                for (Review r : m.getReviews()) {
                    if (strooleans.stream().anyMatch(x->x.getString().equalsIgnoreCase(r.getUser()) && x.isBoolean())) {
                        if (r.getDate().isAfter(cutoff)) {
                            LocalDate key = r.getDate().withDayOfMonth(1);
                            reviewMap.put(key, reviewMap.getOrDefault(key, 0)+1);
                        }
                        reviewsPerRating[r.getRating()] += 1;
                        info.accumulate(r);
                        used = true;
                    }
                }
                if (used) {
                    info.accumulate(m);
                    for (Factoid f : m.getFactoids()) {
                        info.accumulate(f);
                    }
                }
            }
        }
        if (categoryContainer.getChildren().size() > 3)
            categoryContainer.getChildren().remove(2);
        categoryContainer.getChildren().add(2,info.getInfo());
        
        // Chart - # Reviewed / Year
        chartReviewsPerYear.setLegendVisible(false);
        chartReviewsPerYear.getData().clear();
        
        XYChart.Series data = new XYChart.Series();
        LocalDate earliest = info.getEarliest().isAfter(cutoff) ? info.getEarliest() : cutoff; // Use the earliest date in the data if it is after cutoff date, else use cutoff date
        earliest = earliest.withDayOfMonth(1);
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
        for (LocalDate month : reviewMap.keySet()) {
            if(month.isAfter(thisMonth))
                break;
            String key = month.format(formatter);
            Integer value = reviewMap.getOrDefault(month, 0);
            XYChart.Data datapoint = new XYChart.Data(key, value);
            datapoint.setNode(new HoveredLabelNode(color,value));
            data.getData().add(datapoint);
        }
        if (data.getData().size()<1)
            data.getData().add(new XYChart.Data<>("", 0));
        chartReviewsPerYear.getData().addAll(data);
        
        // Chart - # Reviews / Rating
        data = new XYChart.Series();
        for (int i = 1; i < reviewsPerRating.length; i++) {
            data.getData().add(new XYChart.Data(i+"",reviewsPerRating[i]));
        }
        if (data.getData().size()<1)
            data.getData().add(new XYChart.Data<>("", 0));
        chartReviewsPerRating.getData().clear();
        chartReviewsPerRating.getData().add(data);
    }
    
    private void updateFactoid() {
        // Get Vars
        String factoidType = factoidSelector.getValue();
        String category = categorySelector.getValue();
        Integer minCount = numOfFactsSettings.getMin();
        Integer maxCount = numOfFactsSettings.getMax();
        
        // Colors
        int index = Arrays.asList(DataManager.getFactiodTypes()).indexOf(factoidType);
        chartAverageFactRating.setStyle("CHART_COLOR_1: "+colors[ (index>=0?colors.length-index:0) % colors.length]+";");
        chartNumOfFacts.setStyle("CHART_COLOR_1: "+colors[ (index>=0?colors.length-index:0) % colors.length]+";");
            
        // Data Mining - Vars
        final Integer threshold = 5;
        final Integer maxLabelLen = 13;
        Map<String, Integer> ratingFactsMap = new HashMap<>();
        Map<String, Integer> countReviewFactsMap = new HashMap<>();
        Map<String, Integer> numberFactsMap = new HashMap<>();
        InfoBlipAccumulator info = new InfoBlipAccumulator();
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            List<Factoid> factoids = new ArrayList<>(m.getFactoids());
            factoids.add(new Factoid("Title",m.getTitle()));
            factoids.add(new Factoid("Creator",m.getCreator()));
            factoids.add(new Factoid("Season",m.getSeason()));
            factoids.add(new Factoid("Episode",m.getEpisode()));
            factoids.add(new Factoid("Category",m.getCategory()));
            if (category.equals(m.getCategory()) || category.equals(universalCategory)){
                boolean user = false;
                boolean fact = false;
                
                for (Review r : m.getReviews())
                    if (strooleans.stream().anyMatch(x->x.getString().equalsIgnoreCase(r.getUser()) && x.isBoolean()))
                        user = true;
                
                for (Factoid factiod : factoids)
                    if (factiod.getTitle().equals(factoidType))
                        fact = true;
                
                if (user && fact) {
                    Set<String> contentTypes = new HashSet<>();
                    info.accumulate(m);
                    SimpleIntegerProperty rating = new SimpleIntegerProperty(0);
                    SimpleIntegerProperty count = new SimpleIntegerProperty(0);
                    for (Review r : m.getReviews())
                        if (strooleans.stream().anyMatch(x->x.getString().equalsIgnoreCase(r.getUser()) && x.isBoolean())) {
                            rating.set(rating.get()+r.getRating());
                            count.set(count.get()+1);
                            info.accumulate(r);
                        }
                    for (Factoid factoid : factoids)
                        if (factoid.getTitle().equals(factoidType)) {
                            String f = factoid.getContent();
                            if (f.length()>maxLabelLen)
                                f = f.substring(0,maxLabelLen);
                            contentTypes.add(f);
                            numberFactsMap.put(f,numberFactsMap.getOrDefault(f, 0)+1);
                            info.accumulate(factoid);
                        }
                    contentTypes.stream()
                            .forEach(content->{
                                ratingFactsMap.put(content,ratingFactsMap.getOrDefault(content, 0)+rating.get());
                                countReviewFactsMap.put(content,countReviewFactsMap.getOrDefault(content, 0)+count.get());
                            });
                }
            }
        }
        if (mediaContainer.getChildren().size() > 3)
            mediaContainer.getChildren().remove(2);
        mediaContainer.getChildren().add(2,info.getInfo());
        
        // Chart - # of Facts
        chartNumOfFacts.getData().clear();
        XYChart.Series<String,Number> dataNum = new XYChart.Series<>();
        dataNum.setName(factoidType);
        numberFactsMap.keySet().stream()
                .sorted(new NaturalOrderComparator())
                .forEachOrdered(factoid-> {
                    Integer count = numberFactsMap.get(factoid);
                    if (count >= minCount && count <= maxCount)
                        dataNum.getData().add(new XYChart.Data<>(factoid, count));
                });
        if (dataNum.getData().size()<1)
            dataNum.getData().add(new XYChart.Data<>("", 0));
        chartNumOfFacts.getData().add(dataNum);
        
        // Chart - Average Fact Rating
        chartAverageFactRating.getData().clear();
        XYChart.Series<String,Number> dataAve = new XYChart.Series<>();
        dataAve.setName(factoidType);
        ratingFactsMap.keySet().stream()
                .sorted(new NaturalOrderComparator())
                .forEachOrdered(factoid->{
                    Integer count = countReviewFactsMap.get(factoid);
                    if (count>=minCount && count<=maxCount)
                        dataAve.getData().add(new XYChart.Data<>(factoid, ratingFactsMap.get(factoid)*1.0/count));
                });
        if (dataAve.getData().size()<1)
            dataAve.getData().add(new XYChart.Data<>("", 0));
        chartAverageFactRating.getData().add(dataAve);
    }
    
    // Takes a date and returns the year with decimal value for the
    // percentage of the year that is complete.
    private double dateToDouble(LocalDate date){
        return date.getYear()+(date.getDayOfYear()+0.0)/365.25;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class CategorySectionController implements Initializable {

    @FXML
    private ChoiceBox<String> categorySelector;
    @FXML
    private BarChart<?, ?> chartReviewsPerRating;
    @FXML
    private NumberAxis chartRprY;
    @FXML
    private CategoryAxis chartRprX;
    @FXML
    private LineChart<?, ?> chartReviewsPerYear;
    @FXML
    private NumberAxis chartRpyY;
    @FXML
    private CategoryAxis chartRpyX;
    @FXML
    private VBox statsBox;
    
    private List<Media> allMedia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartReviewsPerYear.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        chartReviewsPerRating.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        categorySelector.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        categorySelector.setValue(DataManager.getCategories()[0]);
        categorySelector.valueProperty().addListener((observe,old,neo)->updateCategory());
        chartReviewsPerRating.setLegendVisible(false);
        chartRprX.setLabel("Ratings");
        chartRprY.setLabel("Reviews");
        chartRpyX.setLabel("Months");
        chartRpyY.setLabel("Reviews");
    }    
    
    public void update(List<Media> aList){
        allMedia.clear();
        allMedia.addAll(aList);
        updateCategory();
    }
    
    private void updateCategory(){
        // Category Chooser
        String category = categorySelector.getValue();

        // Data Mining - Vars
        Map<String, Integer> reviewMap = new HashMap<>();
        int[] reviewsPerRating = new int[DataManager.getMaxRating() + 1];
        InfoBlipAccumulator info = new InfoBlipAccumulator();

        // Data Mining - Calcs
        for (Media m : allMedia) {
            if (category.equals(m.getCategory())) {
                info.accumulate(m);
                for (Review r : m.getReviews()) {
                    reviewsPerRating[r.getRating().intValue()] += 1;
                    String key = r.getDate().getMonthValue() + "-" + r.getDate().getYear();
                    reviewMap.put(key, reviewMap.getOrDefault(key, 0) + 1);
                    info.accumulate(r);
                }
            }
        }

        // Stats
        statsBox.getChildren().clear();
        statsBox.getChildren().add(info.getInfo());

        // Chart - # Reviewed / Year
        chartReviewsPerYear.setLegendVisible(false);
        chartReviewsPerYear.getData().clear();

        XYChart.Series data = new XYChart.Series();
        int year = info.getEarliest().getYear();
        int month = info.getEarliest().getMonthValue();
        for (int i = 0; i <= ChronoUnit.MONTHS.between(info.getEarliest(), LocalDate.now()) + 1; i++) {
            String key = month + "-" + year;
            data.getData().add(new XYChart.Data(key, reviewMap.getOrDefault(key, 0)));
            ++month;
            if (month > 12) {
                month = 1;
                ++year;
            }
            if (year >= LocalDate.now().getYear() && month > LocalDate.now().getMonthValue()) {
                break;
            }
        }

        chartReviewsPerYear.getData().addAll(data);

        // Chart - # Reviews / Rating
        data = new XYChart.Series();
        for (int i = 1; i < reviewsPerRating.length; i++) {
            data.getData().add(new XYChart.Data(i + "", reviewsPerRating[i]));
        }
        chartReviewsPerRating.getData().clear();
        chartReviewsPerRating.getData().add(data);
    }
    
}

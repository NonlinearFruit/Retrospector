/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
class OverallSectionController implements Initializable {

    @FXML
    private StackedBarChart<?, ?> chartReviewsPerDay;
    @FXML
    private NumberAxis chartRpdY;
    @FXML
    private CategoryAxis chartRpdX;
    @FXML
    private PieChart chartMediaPerCategory;
    @FXML
    private VBox statsBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartReviewsPerDay.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        chartMediaPerCategory.setLegendVisible(true);
        chartRpdX.setLabel("Days");
        chartRpdY.setLabel("Reviews");
    }    
    
    protected void update(List<Media> allMedia){
        
        // Constants
        int last__days = 20;
        
        // Data Mining - Vars
        Map<String, Integer> categories = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> last30Days = new HashMap<>();
        InfoBlipAccumulator info = new InfoBlipAccumulator();
        
        // Data Mining - Calcs
        for (Media m : allMedia) {
            categories.put(m.getCategory(), categories.getOrDefault(m.getCategory(), 0)+1);
            info.accumulate(m);
            for (Review r : m.getReviews()) {
                if(ChronoUnit.DAYS.between(r.getDate(), LocalDate.now())<=last__days){
                    Map<String,Integer> cat2Num = last30Days.getOrDefault(r.getDate(), new HashMap<>());
                    Integer num = cat2Num.getOrDefault(m.getCategory(), 1);
                    cat2Num.put(m.getCategory(), num+1);
                    last30Days.put(r.getDate(), cat2Num);
                }
                info.accumulate(r);
            }
        }
        
        // Stats
        statsBox.getChildren().clear();
        statsBox.getChildren().add(info.getInfo());
        
        // Chart # Media / Category
        chartMediaPerCategory.setData(
                FXCollections.observableArrayList(
                    Arrays.asList(DataManager.getCategories())
                        .stream()
                        .map(c -> {
                                int count = categories.getOrDefault(c, 0);
                                return new PieChart.Data(c + " - " + count, count);
                            }
                        )
                        .collect(Collectors.toList())
                )
        );
        
        // Chart # Reviews / Day
        ObservableList list = FXCollections.observableArrayList();
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
    }
    
}

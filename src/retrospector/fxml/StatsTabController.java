/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.Stroolean;
import retrospector.util.UtilityCloset;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class StatsTabController implements Initializable {

    CoreController core;
    
    @FXML
    private PieChart chartMediaPerCategory;
    @FXML
    private ListView<Stroolean> userList;
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
    private TableView<Media> mediaTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartMediaPerCategory.setData(
                FXCollections.observableArrayList(
                        Arrays.asList(
                                DataManager.getCategories()).stream()
                        .map(c -> {
                            long count = DataManager.getMedia().stream()
                                    .filter(m -> c.equals(m.getCategory()))
                                    .count();
                            return new PieChart.Data(c + " - " + count, count);
                        }
                        )
                        .collect(Collectors.toList()
                        )
                )
        );
    }    
    
    public void update(CoreController core){
        this.core = core;
    }
    
       
    public void update(){

    }
}

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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.NaturalOrderComparator;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class MediaSectionController implements Initializable {

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
    @FXML
    private TableColumn<Media, Number> mediaColumnRowNumber;
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
    private LineChart<?, ?> chartRatingOverTime;
    @FXML
    private NumberAxis chartRotY;
    @FXML
    private NumberAxis chartRotX;
    @FXML
    private VBox statsBox;
    
    private Media currentMedia;
    private ObservableList<Media> allMedia = FXCollections.observableArrayList();
    private FilteredList<Media> mediaTableFilter = new FilteredList(allMedia);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartRatingOverTime.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data(0,0))));
        checkTitle.setSelected(true);
        checkCreator.setSelected(true);
        checkSeason.setSelected(true);
        checkEpisode.setSelected(true);
        checkCategory.setSelected(true);
        checkTitle.selectedProperty().addListener((observe, old, neo) -> updateMedia());
        checkCreator.selectedProperty().addListener((observe, old, neo) -> updateMedia());
        checkSeason.selectedProperty().addListener((observe, old, neo) -> updateMedia());
        checkEpisode.selectedProperty().addListener((observe, old, neo) -> updateMedia());
        checkCategory.selectedProperty().addListener((observe, old, neo) -> updateMedia());
        mediaTableFilter = new FilteredList(allMedia);
        SortedList<Media> mediaSortable = new SortedList<>(mediaTableFilter);
        mediaTable.setItems(mediaSortable);
        mediaSortable.comparatorProperty().bind(mediaTable.comparatorProperty());
        mediaColumnRowNumber.setSortable(false);
        mediaColumnRowNumber.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1 + mediaTable.getItems().indexOf(p.getValue())));
        mediaColumnTitle.setComparator(new NaturalOrderComparator());
        mediaColumnCreator.setComparator(new NaturalOrderComparator());
        mediaColumnSeason.setComparator(new NaturalOrderComparator());
        mediaColumnEpisode.setComparator(new NaturalOrderComparator());
        mediaColumnCategory.setComparator(new NaturalOrderComparator());
        mediaColumnTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        mediaColumnCreator.setCellValueFactory(new PropertyValueFactory<>("Creator"));
        mediaColumnSeason.setCellValueFactory(new PropertyValueFactory<>("Season"));
        mediaColumnEpisode.setCellValueFactory(new PropertyValueFactory<>("Episode"));
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
        chartRotX.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                double x = number.doubleValue();
                double decimal = x % 1;
                double year = x - decimal;
                double days = decimal * 365.25;
                if (days > 365 || days < 1) {
                    return ((int) year) + "";
                }
                LocalDate date = LocalDate.ofYearDay((int) year, (int) days);
                return date.format(DateTimeFormatter.ofPattern("MMM uuuu"));
            }

            @Override
            public Number fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }    
    
    private void updateMedia(){
        
        // Media Filtering
        Boolean title = checkTitle.isSelected();
        Boolean creator = checkCreator.isSelected();
        Boolean season = checkSeason.isSelected();
        Boolean episode = checkEpisode.isSelected();
        Boolean category = checkCategory.isSelected();
        
        // Filter Table
        mediaTableFilter.setPredicate(m->
                ( ( checkTitle.isSelected() && currentMedia.getTitle().equals(((Media)m).getTitle()) ) || !checkTitle.isSelected() ) &&
                ( ( checkCreator.isSelected() && currentMedia.getCreator().equals(((Media)m).getCreator()) ) || !checkCreator.isSelected() ) &&
                ( ( checkSeason.isSelected() && currentMedia.getSeason().equals(((Media)m).getSeason()) ) || !checkSeason.isSelected() ) &&
                ( ( checkEpisode.isSelected() && currentMedia.getEpisode().equals(((Media)m).getEpisode()) ) || !checkEpisode.isSelected() ) &&
                ( ( checkCategory.isSelected() && currentMedia.getCategory().equals(((Media)m).getCategory()) ) || !checkCategory.isSelected() )
        );

        // Data Mining - Vars
        XYChart.Series data = new XYChart.Series();
        InfoBlipAccumulator info = new InfoBlipAccumulator();
        
        // Data Mining - Calcs
        for (Media m : mediaTable.getItems()) {
            info.accumulate(m);
            for (Review r : m.getReviews()) {
                data.getData().add(new XYChart.Data(dateToDouble(r.getDate()), r.getRating().intValue()));
                info.accumulate(r);
            }
        }

        // Stats
        statsBox.getChildren().clear();
        statsBox.getChildren().add(info.getInfo());
        
        // Chart
        chartRatingOverTime.getData().clear();
        if(data.getData().size()<1500)
            chartRatingOverTime.getData().add(data);
        chartRotX.setLowerBound(dateToDouble(info.getEarliest())-1.0/12);
        chartRotX.setUpperBound(dateToDouble(LocalDate.now())+1.0/12);
    }
    
    // Takes a date and returns the year with decimal value for the
    // percentage of the year that is complete.
    private double dateToDouble(LocalDate date){
        return date.getYear()+(date.getDayOfYear()+0.0)/365.25;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.util.PropertyManager;
import retrospector.util.Stroolean;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class StatsTabController implements Initializable {

    private List<Stroolean> strooleans = new ArrayList<>();
    private Media currentMedia;
    
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
        chartMediaPerCategory.setLegendVisible(true);
        categorySelector.setItems(FXCollections.observableArrayList(PropertyManager.loadProperties().getCategories()));
    }
    
       
    public void update(Media media){
        currentMedia = media;
        checkTitle.setText("Title: "+media.getTitle());
        checkCreator.setText("Creator: "+media.getCreator());
        checkSeason.setText("Season: "+media.getSeasonId());
        checkEpisode.setText("Episode: "+media.getEpisodeId());
        checkCategory.setText("Category: "+media.getCategory());
        update();
    }
    public void update(){
        
        
        
        // Overall
        
        userList.getItems().clear();
        strooleans.clear();
        for (String category : DataManager.getUsers()) {
            Stroolean c = new Stroolean(category,true);
            c.booleanProperty().addListener((observe,old,neo)->update());
            strooleans.add(c);
            userList.getItems().add(c);
        }
        userList.setCellFactory(CheckBoxListCell.forListView(Stroolean::booleanProperty));
        
        chartMediaPerCategory.setData(
                FXCollections.observableArrayList(
                    Arrays.asList(DataManager.getCategories())
                        .stream()
                        .map(c -> {
                                long count = DataManager.getMedia().stream()
                                        .filter(m -> c.equals(m.getCategory()))
                                        .count();
                                return new PieChart.Data(c + " - " + count, count);
                            }
                        )
                        .collect(Collectors.toList())
                )
        );
    }
}

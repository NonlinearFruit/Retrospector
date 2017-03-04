/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ListsTabController implements Initializable {

    @FXML
    private ListView<?> listIncludeList;
    @FXML
    private ToggleButton listGroupCreator;
    @FXML
    private ToggleButton listGroupTitle;
    @FXML
    private ToggleButton listGroupSeason;
    @FXML
    private ToggleButton listGroupEpisode;
    @FXML
    private TableView<?> listTable;
    @FXML
    private TableColumn<?, ?> listRankColumn;
    @FXML
    private TableColumn<?, ?> listTitleColumn;
    @FXML
    private TableColumn<?, ?> listCreatorColumn;
    @FXML
    private TableColumn<?, ?> listSeasonColumn;
    @FXML
    private TableColumn<?, ?> listEpisodeColumn;
    @FXML
    private TableColumn<?, ?> listCategoryColumn;
    @FXML
    private TableColumn<?, ?> listReviewsColumn;
    @FXML
    private TableColumn<?, ?> listRatingColumn;
    @FXML
    private ToggleButton listTop10;
    @FXML
    private ToggleButton listTop100;
    @FXML
    private ToggleButton listTop1000;
    @FXML
    private RadioButton listUseAllTime;
    @FXML
    private RadioButton listUseYear;
    @FXML
    private TextField listYear;
    @FXML
    private RadioButton listCustomDateRange;
    @FXML
    private DatePicker listStartDate;
    @FXML
    private DatePicker listEndDate;
    @FXML
    private TextField listUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import retrospector.fxml.CoreController.TAB;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.ControlFxTextFieldModifier;
import retrospector.util.MediaComparator;
import retrospector.util.NaturalOrderComparator;
import retrospector.util.Stroolean;
import retrospector.util.UtilityCloset;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ListsTabController implements Initializable {

    @FXML
    private ListView<Stroolean> listIncludeList;
    @FXML
    private TableView<Media> listTable;
    @FXML
    private ToggleButton listTop10;
    @FXML
    private ToggleButton listTop25;
    @FXML
    private ToggleButton listTop50;
    @FXML
    private ToggleButton listTop100;
    @FXML
    private ToggleButton listTop500;
    @FXML
    private ToggleButton listTop1000;
    @FXML
    private TextField listYear;
    @FXML
    private DatePicker listStartDate;
    @FXML
    private DatePicker listEndDate;
    @FXML
    private TextField listUser;
    @FXML
    private TableColumn<Media, String> listTitleColumn;
    @FXML
    private TableColumn<Media, String> listCreatorColumn;
    @FXML
    private TableColumn<Media, String> listSeasonColumn;
    @FXML
    private TableColumn<Media, String> listEpisodeColumn;
    @FXML
    private TableColumn<Media, String> listCategoryColumn;
    @FXML
    private TableColumn<Media, Integer> listReviewsColumn;
    @FXML
    private TableColumn<Media, BigDecimal> listRatingColumn;
    @FXML
    private TableColumn<Media, Integer> listRankColumn;
    @FXML
    private RadioButton listCustomDateRange;
    @FXML
    private ToggleButton listGroupCreator;
    @FXML
    private ToggleButton listGroupTitle;
    @FXML
    private ToggleButton listGroupSeason;
    @FXML
    private ToggleButton listGroupEpisode;
    @FXML
    private RadioButton listUseAllTime;
    @FXML
    private RadioButton listUseYear;

    private ObservableList<Stroolean> strooleans = FXCollections.observableArrayList();
    private ObservableList<Media> listTableData = FXCollections.observableArrayList();
    private ObjectProperty<TAB> currentTab;
    private ToggleGroup dateToggleGroup = new ToggleGroup();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initListTab();
    }
    
    protected void setup(ObjectProperty<TAB> t){
        currentTab = t;
    }
    
    protected void update(){
        updateListTab();
    }
    
    
    private void updateListTab(){
        listTableData.clear();
        if(!listGroupCreator.isSelected())
            return;
        
        boolean creator = listGroupCreator.isSelected();
        boolean title = listGroupTitle.isSelected();
        boolean season = listGroupSeason.isSelected();
        boolean episode = listGroupEpisode.isSelected();
        Chartagories chartagory = episode? Chartagories.CURRENT_MEDIA:
                                  season?  Chartagories.SEASON:
                                  title?   Chartagories.TITLE:
                                           Chartagories.CREATOR;
        
        Integer top = listTop10.isSelected()?   10:
                      listTop25.isSelected()?   25:
                      listTop50.isSelected()?   50:
                      listTop100.isSelected()? 100:
                      listTop500.isSelected()? 500:
                                               1000;
        
        LocalDate start = listCustomDateRange.isSelected()? listStartDate.getValue().minus(1,ChronoUnit.DAYS) : LocalDate.of(Integer.parseInt(listYear.getText())-1, 12, 31);
            start = listUseAllTime.isSelected()? LocalDate.MIN : start;
        LocalDate end = listCustomDateRange.isSelected()? listEndDate.getValue().plus(1,ChronoUnit.DAYS) : LocalDate.of(Integer.parseInt(listYear.getText())+1, 1, 1);
            end = listUseAllTime.isSelected()? LocalDate.MAX : end;
        
        String user = listUser.getText();
        
        for (Media media : DataManager.getMedia()) {
            for (Stroolean stroolean : strooleans) {
                if(stroolean.isBoolean() && media.getCategory().equals(stroolean.getString())){
                    boolean homeless = true;
                    for (Media data : listTableData) {
                        Media temp = new Media();
                        temp.clone(media);
                        temp.setCategory(data.getCategory());
                        temp.setType(data.getType());
                        if(UtilityCloset.isSameMedia(chartagory, data, temp)){
                            homeless = false;
                            for (Review review : media.getReviews()) {
                                if(review.getDate().isBefore(end) && 
                                   review.getDate().isAfter(start)&&
                                   review.getUser().equals(user)){
                                        data.getReviews().add(review);
                                }
                            }
                            break;
                        }
                    }
                    if(homeless){
                        Media m = new Media();
                        if(creator)
                            m.setCreator(media.getCreator());
                        if(title)
                            m.setTitle(media.getTitle());
                        if(season)
                            m.setSeason(media.getSeason());
                        if(episode)
                            m.setEpisode(media.getEpisode());
                        m.setCategory(media.getCategory());
                        for (Review review : media.getReviews()) {
                            if(review.getUser().equals(user)){
                                if(review.getDate().isBefore(end) && 
                                   review.getDate().isAfter(start)){
                                        m.getReviews().add(review);
                                }
                            }
                        }
                        listTableData.add(m);
                    }
                }
            }
        }
        List rankedResults = listTableData.stream()
                    .sorted(new MediaComparator())
                    .limit(top)
                    .collect(Collectors.toList());
        listTable.setItems(FXCollections.observableArrayList(rankedResults));
        listRankColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1+rankedResults.indexOf(p.getValue())));
        listTable.refresh();
    }
    
    private void initListTab(){
        // Include
        for (String category : DataManager.getCategories()) {
            Stroolean c = new Stroolean(category,true);
            c.booleanProperty().addListener((observe,old,neo)->updateListTab());
            strooleans.add(c);
            listIncludeList.getItems().add(c);
        }
        listIncludeList.setCellFactory(CheckBoxListCell.forListView(Stroolean::booleanProperty));
        listIncludeList.setOnMouseClicked(e->{
            if (e.getClickCount() == 2){
                Stroolean me = listIncludeList.getSelectionModel().getSelectedItem();
                for (Stroolean stroolean : strooleans) {
                    stroolean.setBoolean(false);
                }
                me.setBoolean(true);
            }
        });
        
        // Group By
        listGroupCreator.setSelected(true);
        listGroupCreator.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                // Nothing to select, top of the food chain
            } else {
                listGroupTitle.setSelected(false);
                listGroupSeason.setSelected(false);
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupTitle.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
            } else {
                listGroupSeason.setSelected(false);
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupSeason.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
                listGroupTitle.setSelected(true);
            } else {
                listGroupEpisode.setSelected(false);
            }
            updateListTab();
        });
        listGroupEpisode.selectedProperty().addListener((observe,old,neo)->{
            if(neo){
                listGroupCreator.setSelected(true);
                listGroupTitle.setSelected(true);
                listGroupSeason.setSelected(true);
            } else {
                // Nothing to deselect, bottom of the food chain
            }
            updateListTab();
        });
        
        
        // Top 10/0/0
        listTop10.setSelected(true);
        listTop10.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop25.setSelected(false);
                listTop50.setSelected(false);
                listTop100.setSelected(false);
                listTop500.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop25.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop50.setSelected(false);
                listTop100.setSelected(false);
                listTop500.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop50.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop25.setSelected(false);
                listTop100.setSelected(false);
                listTop500.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop100.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop25.setSelected(false);
                listTop50.setSelected(false);
                listTop500.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop500.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop25.setSelected(false);
                listTop50.setSelected(false);
                listTop100.setSelected(false);
                listTop1000.setSelected(false);
            }
        });
        listTop1000.selectedProperty().addListener((observe,old,neo)->{
            updateListTab();
            if(neo){
                listTop10.setSelected(false);
                listTop25.setSelected(false);
                listTop50.setSelected(false);
                listTop100.setSelected(false);
                listTop500.setSelected(false);
            }
        });
        
        // Dates
        listYear.setText(String.valueOf(LocalDate.now().getYear()));
        listYear.setOnAction(e->updateListTab());
        listStartDate.setValue(LocalDate.now().withMonth(1).withDayOfMonth(1));
        listStartDate.valueProperty().addListener((observe,old,neo)->updateListTab());
        listEndDate.setValue(LocalDate.now().withMonth(12).withDayOfMonth(31));
        listEndDate.valueProperty().addListener((observe,old,neo)->updateListTab());
        listUseAllTime.selectedProperty().addListener((observe,old,neo)->updateListTab());
        listUseYear.selectedProperty().addListener((observe,old,neo)->updateListTab());
        listCustomDateRange.selectedProperty().addListener((observe,old,neo)->updateListTab());
        //  - Enable/Disble
        dateToggleGroup.getToggles().addAll(listUseYear,listUseAllTime,listCustomDateRange);
        dateToggleGroup.selectToggle(listUseAllTime);
        listYear.disableProperty().bind(Bindings.not(listUseYear.selectedProperty()));
        listStartDate.disableProperty().bind(Bindings.not(listCustomDateRange.selectedProperty()));
        listEndDate.disableProperty().bind(Bindings.not(listCustomDateRange.selectedProperty()));
        
        // User
        listUser.setText(DataManager.getDefaultUser());
        listUser.setOnAction(e->updateListTab());
        ControlFxTextFieldModifier.autocompleteMe(listUser, DataManager.getUsers());
        
        // Table
        listTable.setItems(listTableData);
        listTable.setPlaceholder(new Text("Adjust the settings above to view a Top Ten list"));
        
        // Link to Properties
        listTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        listCreatorColumn.setCellValueFactory(new PropertyValueFactory<>("Creator"));
        listSeasonColumn.setCellValueFactory(new PropertyValueFactory<>("Season"));
        listEpisodeColumn.setCellValueFactory(new PropertyValueFactory<>("Episode"));
        listCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        
        // Special Table Cells
        listReviewsColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getReviews().size()) );
        listRatingColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getAverageRating()) );
        
        // Comparors for string columns
        listTitleColumn.setComparator(new NaturalOrderComparator());
        listCreatorColumn.setComparator(new NaturalOrderComparator());
        listSeasonColumn.setComparator(new NaturalOrderComparator());
        listEpisodeColumn.setComparator(new NaturalOrderComparator());
        listCategoryColumn.setComparator(new NaturalOrderComparator());
    }
    
    public static enum Chartagories{
        CURRENT_MEDIA,SEASON,TITLE,CREATOR,CATEGORY
    }
    
}

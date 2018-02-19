/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.wishlist;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import retrospector.fxml.core.CoreController;
import retrospector.fxml.search.QueryProcessor;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.util.NaturalOrderComparator;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class WishlistTabController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField creatorField;
    @FXML
    private TextField seasonField;
    @FXML
    private TextField episodeField;
    @FXML
    private ChoiceBox<String> categoryField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Media> table;
    @FXML
    private TableColumn<Media, String> titleColm;
    @FXML
    private TableColumn<Media, String> creatorColm;
    @FXML
    private TableColumn<Media, String> seasonColm;
    @FXML
    private TableColumn<Media, String> episodeColm;
    @FXML
    private TableColumn<Media, String> categoryColm;
    @FXML
    private TableColumn<Media, String> descriptionColm;
    @FXML
    private VBox root;
    @FXML
    private Button newBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button mediaBtn;

    
    private ObservableList<Media> wishMedia;
    private int currentWishId;
    private ObjectProperty<CoreController.TAB> currentTab;
    private ObjectProperty<Media> currentMedia;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Display Details
        table.setPlaceholder(new Text("Click 'New' to make your 1st wishlist item!"));
        
        // Items
        wishMedia = FXCollections.observableArrayList(DataManager.getWishlist());
        FilteredList<Media> filteredMedia = new FilteredList<>(wishMedia);
        SortedList<Media> sortedMedia = new SortedList<>(filteredMedia);
        sortedMedia.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedMedia);
        
        // Columns
        titleColm.setCellValueFactory(new PropertyValueFactory("Title"));
        creatorColm.setCellValueFactory(new PropertyValueFactory("Creator"));
        seasonColm.setCellValueFactory(new PropertyValueFactory("Season"));
        episodeColm.setCellValueFactory(new PropertyValueFactory("Episode"));
        categoryColm.setCellValueFactory(new PropertyValueFactory("Category"));
        descriptionColm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Media,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Media,String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDescription().replaceAll("\n", " "));
            }
        });
        
        // Comparators
        titleColm.setComparator(new NaturalOrderComparator());
        creatorColm.setComparator(new NaturalOrderComparator());
        seasonColm.setComparator(new NaturalOrderComparator());
        episodeColm.setComparator(new NaturalOrderComparator());
        categoryColm.setComparator(new NaturalOrderComparator());
        descriptionColm.setComparator(new NaturalOrderComparator());
        
        // Searching
        searchField.textProperty().addListener((observa,old,neo)->{
            String query = neo;
            if(query==null || query.equals(""))
                filteredMedia.setPredicate(x->true);
            else{
                String[] queries = query.split(":");
                filteredMedia.setPredicate(x->QueryProcessor.isMatchForMedia(query,x));
            }
        });
        
        // Double-clicking a row
        table.setRowFactory( tv -> {
            TableRow<Media> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Media rowData = row.getItem();
                    setMedia(rowData);
                }
            });
            return row ;
        });
        
        //====================== Editing Portion  ======================//
        
        // Init
        enableEditting(false);
        
        // Categories
        categoryField.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        categoryField.setValue(DataManager.getCategories()[0]);
    }    
    
    private void enableEditting(boolean tf) {
        titleField.setDisable(!tf);
        creatorField.setDisable(!tf);
        seasonField.setDisable(!tf);
        episodeField.setDisable(!tf);
        categoryField.setDisable(!tf);
        descriptionField.setDisable(!tf);
        
        saveBtn.setDisable(!tf);
        deleteBtn.setDisable(!tf);
        mediaBtn.setDisable(!tf);
    }
    
    private void clear() {
        currentWishId = 0;
        titleField.setText("");
        creatorField.setText("");
        seasonField.setText("");
        episodeField.setText("");
        categoryField.setValue(DataManager.getCategories()[0]);
        descriptionField.setText("");
        enableEditting(false);
    }
    
    public void setMedia(Media media) {
        enableEditting(true);
        currentWishId = media.getId();
        titleField.setText(media.getTitle());
        creatorField.setText(media.getCreator());
        seasonField.setText(media.getSeason());
        episodeField.setText(media.getEpisode());
        categoryField.setValue(media.getCategory());
        descriptionField.setText(media.getDescription());
    }
    
    public Media getMedia() {
        Media media = new Media();
        media.setId(currentWishId);
        media.setType(Media.Type.WISHLIST);
        media.setTitle(titleField.getText());
        media.setCreator(creatorField.getText());
        media.setSeason(seasonField.getText());
        media.setEpisode(episodeField.getText());
        media.setCategory(categoryField.getValue());
        media.setDescription(descriptionField.getText());
        return media;
    }
    
    public void setup(ObjectProperty<CoreController.TAB> t, ObjectProperty<Media> m) {
        currentTab = t;
        currentMedia = m;
    }
    
    private void setTab(CoreController.TAB tab) {
        currentTab.setValue(tab);
    }
    
    public void update() {
        int index = table.getSelectionModel().getFocusedIndex();
        wishMedia.clear();
        wishMedia.addAll(DataManager.getWishlist());
        Collections.reverse(wishMedia); // Place newest stuff first
        table.refresh();
        if (table.getItems().contains(getMedia())) {
            table.getSelectionModel().select(getMedia());
        } else if (table.getItems().size() > index) {
            table.getSelectionModel().select(index);
        } else if (table.getItems().size() > 0) {
            table.getSelectionModel().select(0);
        }
    }

    @FXML
    private void saveWish(ActionEvent event) {
        Media media = getMedia();
        DataManager.updateDB(media);
        clear();
        update();
    }

//    @FXML
    private void cancelWish(ActionEvent event) {
        clear();
    }

    @FXML
    private void deleteWish(ActionEvent event) {
        Media media = getMedia();
        if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete "+media.getTitle()+"?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(media);
                clear();
                update();
        }
    }

    @FXML
    private void sendToMedia(ActionEvent event) {
        Media media = getMedia();
        if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to promote "+media.getTitle()+"?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                Media.Type type = media.getEpisode().isEmpty()? Media.Type.SINGLE :
                                  media.getSeason().isEmpty()?  Media.Type.MINISERIES :
                                                                Media.Type.SERIES;
                media.setType(type);
                DataManager.updateDB(media);
                clear();
                currentMedia.setValue(media);
                setTab(CoreController.TAB.MEDIA);
        }
    }

    @FXML
    private void newWish(ActionEvent event) {
        Media media = new Media();
        media.setType(Media.Type.WISHLIST);
        media.setId(DataManager.createDB(media));
        if (media.getId() < 2) {
            System.err.println("Media got a <2 id (WishlistTabController#newWish");
        }
        setMedia(media);
        titleField.requestFocus();
    }
}

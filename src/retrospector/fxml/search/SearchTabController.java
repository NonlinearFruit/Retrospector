/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.search;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import retrospector.fxml.CoreController.TAB;
import retrospector.fxml.QuickEntryController;
import retrospector.model.DataManager;
import retrospector.model.Factoid;
import retrospector.model.Media;
import retrospector.model.Review;
import retrospector.util.NaturalOrderComparator;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class SearchTabController implements Initializable {

    @FXML
    private TextField searchBox;
    @FXML
    private TableView<Media> searchTable;
    @FXML
    private Button searchNewMedia;
    @FXML
    private Button searchEditMedia;
    @FXML
    private Button searchDeleteMedia;
    @FXML
    private TableColumn<Media, Integer> searchNumberColumn;
    @FXML
    private TableColumn<Media, String> searchTitleColumn;
    @FXML
    private TableColumn<Media, String> searchCreatorColumn;
    @FXML
    private TableColumn<Media, String> searchSeasonColumn;
    @FXML
    private TableColumn<Media, String> searchEpisodeColumn;
    @FXML
    private TableColumn<Media, Integer> searchReviewsColumn;
    @FXML
    private TableColumn<Media, String> searchCategoryColumn;
    @FXML
    private TableColumn<Media, ?> searchRatingColumns;
    @FXML
    private TableColumn<Media, BigDecimal> searchMeanRColumn;
    @FXML
    private TableColumn<Media, BigDecimal> searchCurrentRColumn;
    @FXML
    private MenuItem searchQuickEntry;
    @FXML
    private MenuItem searchStandardEntry;
    @FXML
    private Text searchMeanAverage;
    @FXML
    private Text searchCurrentAverage;
    @FXML
    private Text searchResults;
    @FXML
    private MenuItem searchBackup;
    @FXML
    private MenuItem searchCheatsheet;
    
    private ObservableList<Media> searchTableData;
    private ObjectProperty<Media> currentMedia;
    private ObjectProperty<TAB> currentTab;
    @FXML
    private MenuItem searchServer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initSearchTab();
//        printMedia(); // Used to get fake data for android
    }   
    
    private void updateSearchTab(){
        int index = searchTable.getSelectionModel().getFocusedIndex();
        searchTableData.clear();
        searchTableData.addAll(DataManager.getMedia());
        Collections.reverse(searchTableData); // Place newest stuff first
        searchTable.refresh();
        if(searchTable.getItems().contains(getMedia()))
            searchTable.getSelectionModel().select(getMedia());
        else if(searchTable.getItems().size()>index)
            searchTable.getSelectionModel().select(index);
        else if(searchTable.getItems().size()>0)
            searchTable.getSelectionModel().select(0);
    }
    
    private Media getMedia(){
        return currentMedia.get();
    }
    
    public void next(){
        int size = searchTable.getItems().size();
        int index = searchTable.getSelectionModel().getFocusedIndex()+1;
        searchTable.getSelectionModel().select(index%size);
//        setMedia(searchTable.getSelectionModel().getSelectedItem());
    }
    
    public void previous(){
        int size = searchTable.getItems().size();
        int index = searchTable.getSelectionModel().getFocusedIndex()-1;
        searchTable.getSelectionModel().select( (index+size)%size );
//        setMedia(searchTable.getSelectionModel().getSelectedItem());
    }
    
    public void setup(ObjectProperty<TAB> t,ObjectProperty<Media> m){
        currentTab = t;
        currentMedia = m;
        
        currentMedia.addListener((observe,old,neo)->{
            if(neo==null){
                searchEditMedia.setDisable(true);
                searchDeleteMedia.setDisable(true);
            } else {
                searchEditMedia.setDisable(false);
                searchDeleteMedia.setDisable(false);
            }
        });
    }
    
    private void setMedia(Media m){
        currentMedia.set(m);
    }
    
    private void setTab(TAB t){
        currentTab.set(t);
    }
    
    public void update(){
        updateSearchTab();
    }
    
    private void updateStats(){
        int totalNumberReviews = 0;
        int totalNumberMedia = 0;
        int totalReviewRating = 0;
        int totalCurrentRating = 0;

        totalNumberMedia = searchTable.getItems().size();
        for (Media media : searchTable.getItems()) {
            totalNumberReviews += media.getReviews().size();
            for (Review review : media.getReviews()) {
                totalReviewRating += review.getRating().intValue();
            }
            totalCurrentRating += media.getCurrentRating().intValue();
        }

        searchResults.setText(totalNumberMedia+"");
        searchMeanAverage.setText(String.format("%.2f", totalReviewRating * 1.0 / totalNumberReviews));
        searchCurrentAverage.setText(String.format("%.2f", totalCurrentRating * 1.0 / totalNumberMedia));
    }
    
    private boolean isMatchForMedia(String query, Media media){
        boolean pass = true;
        if(query.endsWith("|") && !query.endsWith("||"))
            query = query.substring(0, query.length()-1);
        String[] queries = query.toLowerCase().split(":");
        List<String> searchables = new ArrayList<>();
        searchables.addAll(Arrays.asList(
                media.getTitle().toLowerCase(),
                media.getCreator().toLowerCase(),
                media.getSeason().toLowerCase(),
                media.getEpisode().toLowerCase(),
                media.getCategory().toLowerCase()
        ));
        for (Factoid fact : media.getFactoids()) {
            searchables.add(fact.getContent().toLowerCase());
        }
        for (String q : queries) {
            String[] optns = q.split("\\|\\|");
            boolean minorPass = false;
            for (String optn : optns) {
                boolean negator = optn.length()>1 && optn.startsWith("!");
                if ( !negator && searchables.stream().anyMatch(s -> s.contains(optn)) )
                    minorPass = true;
                if ( negator && !searchables.stream().anyMatch(s -> s.contains(optn.substring(1))) )
                    minorPass = true;
            }
            if (!minorPass) {
                pass = false;
            }
        }
        return pass;
    }
    
    private void initSearchTab(){
        searchEditMedia.setDisable(true);
        searchDeleteMedia.setDisable(true);
        
        // Table Double Click
        searchTable.setRowFactory(tv -> {
            TableRow<Media> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    setMedia(row.getItem());
                    setTab(TAB.MEDIA);
                }
            });
            return row;
        });
        
        // Table data setup
        searchTable.setPlaceholder(new Text("Create your first Media by clicking the 'New' button!"));
        searchTableData = DataManager.getMedia();
        Collections.reverse(searchTableData); // Place newest stuff first
        FilteredList<Media> mediaFiltered = new FilteredList(searchTableData,x->true);
        SortedList<Media> mediaSortable = new SortedList<>(mediaFiltered);
        searchTable.setItems(mediaSortable);
        mediaSortable.comparatorProperty().bind(searchTable.comparatorProperty());
        
        // Link to data properties
        searchTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        searchCreatorColumn.setCellValueFactory(new PropertyValueFactory<>("Creator"));
        searchSeasonColumn.setCellValueFactory(new PropertyValueFactory<>("Season"));
        searchEpisodeColumn.setCellValueFactory(new PropertyValueFactory<>("Episode"));
        searchCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        
        // Values for special columns
        searchNumberColumn.setSortable(false);
        searchNumberColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(1+searchTable.getItems().indexOf(p.getValue())));
        searchReviewsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Media,Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Media,Integer> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getReviews().size());
            }
        });
        searchMeanRColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getAverageRating());
            }
        });
        searchCurrentRColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Media,BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<Media,BigDecimal> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getCurrentRating());
            }
        });
        
        // Comparators for string columns
        searchTitleColumn.setComparator(new NaturalOrderComparator());
        searchCreatorColumn.setComparator(new NaturalOrderComparator());
        searchSeasonColumn.setComparator(new NaturalOrderComparator());
        searchEpisodeColumn.setComparator(new NaturalOrderComparator());
        searchCategoryColumn.setComparator(new NaturalOrderComparator());
        
        searchTable.getSelectionModel().selectedItemProperty().addListener( (observe, old, neo) -> {
            setMedia(neo);
        });
        
        searchBox.textProperty().addListener((observa,old,neo)->{
            String query = neo;
            if(query==null || query.equals(""))
                mediaFiltered.setPredicate(x->true);
            else{
                String[] queries = query.split(":");
                mediaFiltered.setPredicate(x->QueryProcessor.isMatchForMedia(query,x));
            }
            updateStats();
        });
        
        // Buttons
        searchNewMedia.setOnAction(e->{
            Media neo = new Media();
            neo.setId(DataManager.createDB(neo));
            setMedia(neo);
            setTab(TAB.MEDIA);
        });
        searchEditMedia.setOnAction(e->{
            setTab(TAB.MEDIA);
        });
        searchDeleteMedia.setOnAction(e->{
            if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete this?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)){
                DataManager.deleteDB(getMedia());
                updateSearchTab();
            }
        });
        
        // Init stuff
        updateStats();
    }
    
    private void printMedia() {
        for (Media media : DataManager.getMedia()) {
            if (media.getReviews().size()>1 && media.getAverageRating()>=8) {
                System.out.println("m = new Media(\""+media.getTitle()+"\",\""+media.getCreator()+"\",\""+media.getCategory()+"\");");
                System.out.println("m.setId("+media.getId()+");");
                System.out.println("m.setSeason(\""+media.getSeason()+"\");");
                System.out.println("m.setEpisode(\""+media.getEpisode()+"\");");
                for (Review review : media.getReviews()) {
                    System.out.println("r = new Review("+review.getRating()+", new Date("+
                            review.getDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()+
                            "L),\""+review.getUser()+"\");");
                    System.out.println("r.setId("+review.getId()+");");
                    System.out.println("r.setMediaId("+review.getMediaId()+");");
                    System.out.println("r.setReview(\""+review.getReview().replaceAll("\n", "\\\\n").replaceAll("\"", "'")+"\");");
                    System.out.println("m.getReviews().add(r);\n");
                }
                System.out.println("media.add(m);");
            }
        }
    }
}

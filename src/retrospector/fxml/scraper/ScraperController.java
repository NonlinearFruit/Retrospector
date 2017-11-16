/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.scraper;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.util.PropertyManager;
import static retrospector.util.PropertyManager.retroFolder;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ScraperController implements Initializable {

    @FXML
    private TextField searchBox;
    @FXML
    private ChoiceBox<String> scrapeSelector;
    @FXML
    private ListView<ScrapeResult> listView;
    
    private JsDataScraper scraper;
    private ScriptEngineManager mgr = new ScriptEngineManager();
    private ScriptEngine engine = mgr.getEngineByName("JavaScript");
    private Consumer<Media> callback;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrapeSelector.setItems(FXCollections.observableArrayList(getJsDataScrapers()));
        scrapeSelector.setOnAction( e -> {
            try{
                scraper = (JsDataScraper) engine.eval("load('"+PropertyManager.pluginPath+"/"+scrapeSelector.getValue()+"');");
                search();
            } catch( ScriptException se ) {
                se.printStackTrace();
            }
        });
        
        searchBox.setOnAction( e -> search() );
    }
    
    public void setup(Consumer<Media> callback) {
        this.callback = callback;
    }

    @FXML
    private void openMedia(ActionEvent event) {
        ScrapeResult scrape = listView.getSelectionModel().getSelectedItem();
        if (scrape.isEpisode() || scrape.isMedia()) {
            Media media = scraper.getMedia(scrape);
            callback.accept(media);
            cancel(event);
        } else {
            List<ScrapeResult> results = scraper.getGroup(scrape);
            listView.setItems(FXCollections.observableArrayList(results));
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.hide();
    }
    
    private String[] getJsDataScrapers() {
        File directory = new File( PropertyManager.pluginPath );
        if (!directory.exists())
            directory.mkdir();
        
        return Arrays.stream( directory.listFiles() )
                .map( file -> file.getName() )
                .toArray( String[] :: new );
    }
    
    private void search() {
        search(searchBox.getText());
    }
    
    private void search(String query) {
        if ( scraper == null )
            return;
        
        List<ScrapeResult> results = scraper.search(query);
        listView.setItems(FXCollections.observableArrayList(results));
    }
}

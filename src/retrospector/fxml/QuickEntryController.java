/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class QuickEntryController implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField creator;
    @FXML
    private ChoiceBox<String> category;
    @FXML
    private TextField season;
    @FXML
    private TextField episode;
    @FXML
    private Text ratingTitle;
    @FXML
    private TextField rating;
    @FXML
    private TextField date;
    @FXML
    private SplitMenuButton brandNew;
    @FXML
    private MenuItem newKeepT;
    @FXML
    private MenuItem newKeepCr;
    @FXML
    private MenuItem newKeepCa;
    @FXML
    private MenuItem newKeepTCr;
    @FXML
    private MenuItem newKeepTCrCa;
    @FXML
    private MenuItem newKeepTCrCaS;
    @FXML
    private Button discard;
    
    private String defaultCat;
    private CoreController core;
    @FXML
    private Button saveClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultCat = DataManager.getCategories()[0];
        category.setItems(FXCollections.observableArrayList(DataManager.getCategories()));
        category.setValue(defaultCat);
        ratingTitle.setText(DataManager.getDefaultUser()+"'s Rating");
        rating.textProperty().addListener( (obs,old,neo)->{
            if(!neo.equals("")){
                try{
                    Integer r = Integer.parseInt(neo);
                    if(r>DataManager.getMaxRating() || r<1)
                        rating.setText(old);
                } catch(Exception ex){
                    System.out.println("Bad int: "+neo);
                    rating.setText(old);
                }
            }
        });
        date.textProperty().addListener( (obs,old,neo)->{
            if(!neo.equals("")){
                try{
                    Integer d = Integer.parseInt(neo);
                    if(d>99 || d<0)
                        date.setText(old);
                } catch(Exception ex){
                    System.out.println("Bad int: "+neo);
                    date.setText(old);
                }
            }
        });
        saveClose.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            saveClose.getScene().getWindow().hide();
        });
        brandNew.setOnAction(e->{
            DataManager.createDB(getMedia());
            core.updateSearchTab();
            clear();
            title.requestFocus();
        });
        newKeepT.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            title.setText(m.getTitle());
            creator.requestFocus();
        });
        newKeepCr.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            creator.setText(m.getCreator());
            title.requestFocus();
        });
        newKeepCa.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            category.setValue(m.getCategory());
            title.requestFocus();
        });
        newKeepTCr.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            title.setText(m.getTitle());
            creator.setText(m.getCreator());
            category.requestFocus();
        });
        newKeepTCrCa.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            title.setText(m.getTitle());
            creator.setText(m.getCreator());
            category.setValue(m.getCategory());
            season.requestFocus();
        });
        newKeepTCrCaS.setOnAction(e->{
            Media m = getMedia();
            DataManager.createDB(m);
            core.updateSearchTab();
            clear();
            title.setText(m.getTitle());
            creator.setText(m.getCreator());
            category.setValue(m.getCategory());
            season.setText(m.getSeasonId());
            episode.requestFocus();
        });
        discard.setOnAction(e->{
            clear();
            title.requestFocus();
        });
        clear();
    }
    
    public void clear(){
        title.setText("");
        creator.setText("");
        category.setValue(defaultCat);
        season.setText("");
        episode.setText("");
        rating.setText("");
        date.setText("");
    }
    
    public void setCore(CoreController c){
        core = c;
    }
    
    public Media getMedia(){
        Media m = new Media();
        m.setTitle(title.getText());
        m.setCreator(creator.getText());
        m.setCategory(category.getValue());
        m.setSeasonId(season.getText());
        m.setEpisodeId(episode.getText());
        // Rating
        if(!rating.getText().equals("")){
            Review r = new Review(BigDecimal.valueOf(Integer.parseInt(rating.getText())));
            int yy = Integer.parseInt(date.getText());
            if(yy>(LocalDate.now().getYear()%100)+1)
                r.setDate(LocalDate.of(1900+yy,1,1));
            else
                r.setDate(LocalDate.of(2000+yy,1,1));
            m.getReviews().add(r);
        }
        // Type
        if(m.getSeasonId().equals("") && m.getEpisodeId().equals(""))
            m.setType(Media.Type.SINGLE);
        else if(m.getSeasonId().equals(""))
            m.setType(Media.Type.MINISERIES);
        else
            m.setType(Media.Type.SERIES);
        return m;
    }
    
}

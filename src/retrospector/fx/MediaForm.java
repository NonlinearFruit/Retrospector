/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fx;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import retrospector.model.Media;
import retrospector.model.Media.Category;
import retrospector.model.Media.Type;
import retrospector.model.Review;
import retrospector.util.PropertyManager;
import retrospector.util.UtilityCloset;

/**
 *
 * @author nonfrt
 */
public class MediaForm extends VBox{
    
    private TextField title;
    private Rating aveRating;
    private TextField description;
    private TextField creator;
    private TextField season;
    private TextField episode;
    private Pane reviewPane;
    private ListView<Review> reviews;
    private List reviewList;
    private ChoiceBox<Type> type;
    private ChoiceBox<Category> category;
    private Button neo;
    
    public MediaForm(){
        super();
        
        title = new TextField();
        aveRating = new Rating(PropertyManager.loadProperties().getMaxRating(),0);
        description = new TextField();
        creator = new TextField();
        season = new TextField();
        episode = new TextField();
        reviewPane = new VBox();
        reviews = new ListView();
        reviewList = new ArrayList<>();
        type = new ChoiceBox(FXCollections.observableArrayList(Media.Type.values()));
        category = new ChoiceBox(FXCollections.observableArrayList(Media.Category.values()));
        neo = new Button("New Review!");
        
        aveRating.setUpdateOnHover(false);
        aveRating.setDisable(true);
        aveRating.setPartialRating(true);
        type.setOnAction((x)->{
            boolean s =  true;
            boolean e = true;
            if(type.getValue().equals(Type.MINISERIES) || type.getValue().equals(Type.SERIES))
                s = false;
            if(type.getValue().equals(Type.SERIES))
                e = false;
            hideSeason(s);
            hideEpisode(e);
        });
        type.setValue(Type.SINGLE);
        category.setValue(Category.MOVIE);
//        reviews.
        neo.setOnAction((x)->{
            Review r = new Review();
            reviewList.add(r);
            reviews.setItems(FXCollections.observableArrayList(reviewList));
            editReview(r);
        });
        reviews.getSelectionModel().selectedItemProperty().addListener((obs,old,neo)->{
            editReview(neo);
        });
       
        this.setAlignment(Pos.CENTER);
        this.getChildren().clear();
        this.getChildren().addAll(
                UtilityCloset.labelMeImpressed(title,"Title"),
//                UtilityCloset.labelMeImpressed(aveRating, "Rating"),
                aveRating,
                UtilityCloset.labelMeImpressed(creator,"Creator"),
                UtilityCloset.labelMeImpressed(description,"Description"),
                UtilityCloset.labelMeImpressed(category,"Category"),
                UtilityCloset.labelMeImpressed(type,"Type"),
                UtilityCloset.labelMeImpressed(season,"Season"),
                UtilityCloset.labelMeImpressed(episode,"Episode"),
                UtilityCloset.labelMeImpressed(reviews,"Reviews",ContentDisplay.BOTTOM),
                        neo
        );
    }
    
    public MediaForm(Pane pane){
        this();
        reviewPane = pane;
    }
    
    public MediaForm(Pane p, Media m){
        this(p);
        title.setText(m.getTitle());
        aveRating.setRating(m.getAverageRating());
        description.setText(m.getDescription());
        creator.setText(m.getCreator());
        season.setText(m.getSeasonId());
        episode.setText(m.getEpisodeId());
        reviews.setItems(FXCollections.observableArrayList(m.getReviews()));
        reviewList.clear();
        reviewList.addAll(m.getReviews());
        type.setValue(m.getType());
        category.setValue(m.getCategory());
    }
    
    private void hideSeason(Boolean tf){
        season.setVisible(!tf);
    }
    
    private void hideEpisode(Boolean tf){
        episode.setVisible(!tf);
    }
   
    public void setMedia(Media m){
        title.setText(m.getTitle());
        creator.setText(m.getCreator());
        description.setText(m.getDescription());
        category.setValue(m.getCategory());
        type.setValue(m.getType());
        season.setText(m.getSeasonId());
        episode.setText(m.getEpisodeId());
        reviews.setItems(FXCollections.observableArrayList(m.getReviews()));
        reviewList = m.getReviews();
    }
    
    public Media getMedia(){
        Media m = new Media();
        m.setTitle(title.getText());
        m.setCreator(creator.getText());
        m.setDescription(description.getText());
        m.setCategory(category.getValue());
        m.setType(type.getValue());
        m.setSeasonId(season.getText());
        m.setEpisodeId(episode.getText());
//        m.setReviews(reviewList);
        return m;
    }
    
    public Pane getReviewPane(){
        return reviewPane;
    }
    
    public void refresh(){
        reviews.refresh();
        aveRating.setRating(getMedia().getAverageRating());
    }
    
    private void editReview(Review r){
        
        ReviewForm form = new ReviewForm(r);
        Button save = new Button("Save");
        save.setOnAction((x)->{
            r.morphInto(form.getReview());
            refresh();
            reviewPane.getChildren().clear();
        });
        Button cancel = new Button("Remove");
        cancel.setOnAction((x)->{
            reviews.getItems().remove(r);
            reviewList.remove(r);
            reviewPane.getChildren().clear();
        });
        
        reviewPane.getChildren().clear();
        reviewPane.getChildren().addAll(
                form,
                new HBox(save,cancel)
        );
    }
}

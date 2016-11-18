/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fx;

import java.time.LocalDate;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;
import retrospector.model.DataManager;
import retrospector.model.Review;
import retrospector.util.UtilityCloset;

/**
 *
 * @author nonfrt
 */
public class ReviewForm extends VBox{
    
    private Rating rate;
    private DatePicker date;
    private TextField user;
    private TextArea review;
    
    public ReviewForm(){ 
        super(); 
        
        rate = new Rating(DataManager.getMaxRating(), DataManager.getDefaultRating());
        date = new DatePicker(LocalDate.now());
        user = new TextField(DataManager.getDefaultUser());
        review = new TextArea();
        
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(
                new Text("Review"),
                rate,
                UtilityCloset.labelMeImpressed(user,"User"),
                UtilityCloset.labelMeImpressed(date,"Date"),
                UtilityCloset.labelMeImpressed(review,"Review",ContentDisplay.BOTTOM)
        );
    }
    
    public ReviewForm(Review r){
        this();
        setReview(r);
    }
    
    public void setReview(Review r){
        if(r.getRating()!=null)rate.setRating(r.getRating());
        if(r.getDate()!=null)date.setValue(r.getDate());
        if(r.getUser()!=null)user.setText(r.getUser());
        if(r.getReview()!=null)review.setText(r.getReview());
    }
    
    public Review getReview(){
        Review r = new Review();
        r.setRating(rate.getRating());
        r.setDate(date.getValue());
        r.setUser(user.getText());
        r.setReview(review.getText());
        return r;
    }
}

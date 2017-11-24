/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.media;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author nonfrt
 */
public class RollOver extends StackPane{
    
    private Integer msDuration = 1500;
    private ScaleTransition stHideFront;
    private ScaleTransition stHideBack;
    
    public RollOver(Node front, Node back) {
        super(front, back);

        // Front
        ScaleTransition stShowFront = new ScaleTransition(Duration.millis(msDuration), front);
        stShowFront.setFromX(0);
        stShowFront.setToX(1);
        stHideFront = new ScaleTransition(Duration.millis(msDuration), front);
        stHideFront.setFromX(1);
        stHideFront.setToX(0);

        // Back
        back.setScaleX(0);
        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(msDuration), back);
        stShowBack.setFromX(0);
        stShowBack.setToX(1);
        stHideBack = new ScaleTransition(Duration.millis(msDuration), back);
        stHideBack.setFromX(1);
        stHideBack.setToX(0);
        
        // Continuity
        stHideFront.setOnFinished( e -> stShowBack.play() );
        stHideBack.setOnFinished( e -> stShowFront.play() );
    }
    
    public void showFront() {
        stHideBack.play();
    }
    
    public void showBack() {
        stHideFront.play();
    }
}

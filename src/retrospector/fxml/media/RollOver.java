
package retrospector.fxml.media;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * https://stackoverflow.com/a/22541421/4769802
 * @author ggrec
 * Modified by nonfrt
 */
public class RollOver extends StackPane {

    // ==================== 1. Static Fields ========================

    /*
     * Mmm... pie.
     */
    private static final Double PIE = Math.PI;

    private static final Double HALF_PIE = Math.PI / 2;

    private static final double ANIMATION_DURATION = 10000;

    private static final double ANIMATION_RATE = 10;

    // ====================== 2. Instance Fields =============================
    private Timeline animation;

    private SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);

    private PerspectiveTransform transform = new PerspectiveTransform();

    private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);
    
    public RollOver(Node front, Node back) {
        super(front, back);
        
        front.setEffect(transform);
        front.visibleProperty().bind(flippedProperty);
        
        back.setEffect(transform);
        back.visibleProperty().bind(flippedProperty.not());
        
        angle = createAngleProperty();

        this.setPadding(new Insets(30));

        Double height = Math.max(front.getLayoutX(), back.getLayoutX());
        Double width = Math.max(front.getLayoutY(), back.getLayoutY());
        this.setMinHeight(height);
        this.setMinWidth(width);

        this.widthProperty().addListener((obs,x1,x2) -> recalculateTransformation(angle.doubleValue()) );

        this.heightProperty().addListener((obs,x1,x2) -> recalculateTransformation(angle.doubleValue()) );
    }

    private SimpleDoubleProperty createAngleProperty() {
        // --------------------- <Angle> -----------------------

        final SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);

        angle.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(final ObservableValue<? extends Number> obsValue, final Number oldValue, final Number newValue) {
                recalculateTransformation(newValue.doubleValue());
            }
        });

        return angle;
    }

    private Timeline createAnimation() {
        return new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(angle, HALF_PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(final ActionEvent arg0) {
                        // TODO -- Do they another way or API to do this?
                        flippedProperty.set(flippedProperty.not().get());
                    }
                }),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION), new KeyValue(angle, HALF_PIE, Interpolator.EASE_OUT))
        );
    }

    public void flip() {
        if (animation == null) {
            animation = createAnimation();
        }

        animation.setRate(flippedProperty.get() ? ANIMATION_RATE : -ANIMATION_RATE);

        animation.play();
    }
    
    public void showBack() {
        if (flippedProperty.get())
            flip();
    }
    
    public void showFront() {
        if (!flippedProperty.get())
            flip();
    }

    private void recalculateTransformation(final double angle) {
        final double insetsTop = this.getInsets().getTop() * 2;
        final double insetsLeft = this.getInsets().getLeft() * 2;

        final double radius = this.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
        final double height = this.heightProperty().subtract(insetsTop).doubleValue();
        final double back = height / 10;

        /*
         * Compute transform.
         * 
         * Don't bother understanding these unless you're a math passionate.
         * 
         * You may Google "Affine Transformation - Rotation"
         */
        transform.setUlx(radius - Math.sin(angle) * radius);
        transform.setUly(0 - Math.cos(angle) * back);
        transform.setUrx(radius + Math.sin(angle) * radius);
        transform.setUry(0 + Math.cos(angle) * back);
        transform.setLrx(radius + Math.sin(angle) * radius);
        transform.setLry(height - Math.cos(angle) * back);
        transform.setLlx(radius - Math.sin(angle) * radius);
        transform.setLly(height + Math.cos(angle) * back);
    }

}

    /*
    private Integer msDuration = 750;
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
    */

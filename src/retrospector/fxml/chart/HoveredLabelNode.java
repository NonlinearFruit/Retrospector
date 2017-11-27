/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * https://gist.github.com/jewelsea/4681797
 * @author nonfrt
 */
public class HoveredLabelNode extends StackPane {

    HoveredLabelNode(String color, int value) {
        setPrefSize(15, 15);

        final Label label = createDataThresholdLabel(color, value);

        setOnMouseEntered(e -> {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
        });
        setOnMouseExited(e -> {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
        });
    }

    private Label createDataThresholdLabel(String color, int value) {
        final Label label = new Label(value + "");
        
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: "+color);

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}

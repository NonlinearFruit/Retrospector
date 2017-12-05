/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class FactoidChartPopupController implements Initializable {

    private Integer min = 0;
    private Integer max = 1000;
    private Runnable update;
    
    @FXML
    private Spinner<Integer> minCount;
    @FXML
    private Spinner<Integer> maxCount;

    public void setup(Runnable update) {
        this.update = update;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        minCount.setEditable(true);
        minCount.setValueFactory(new IntegerSpinnerValueFactory(min, max,min));
        minCount.valueProperty().addListener((observe, old, neo) -> update.run());
        maxCount.setEditable(true);
        maxCount.setValueFactory(new IntegerSpinnerValueFactory(min, max,max));
        maxCount.valueProperty().addListener((observe, old, neo) -> update.run());
    }    
    
    public Integer getMin() {
        return minCount.getValue();
    }
    
    public Integer getMax() {
        return maxCount.getValue();
    }
    
}

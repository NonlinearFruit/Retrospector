/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.chart;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ChartPopupController implements Initializable {

    public enum TimeFrame{ 
        OneMonth("One Month",1), ThreeMonths("Three Months",3), SixMonths("Six Months",6), OneYear("One Year",12), AllTime("All Time",-1);
        
        private String msg;
        private Integer months;
        
        TimeFrame(String msg, Integer months) { 
            this.msg = msg; 
            this.months = months; 
        }
        
        public LocalDate getEarliest(LocalDate fromHere) {
            if (months < 0)
                return LocalDate.MIN;
            return fromHere.minusMonths(months);
        }
        
        public LocalDate getEarliest() {
            return getEarliest(LocalDate.now());
        }
        
        @Override
        public String toString() { return msg; }
    }
    
    private Runnable updateChart;
    
    @FXML
    private ChoiceBox<TimeFrame> timeChoiceBox;

    public void setup(Runnable updateChart) {
        this.updateChart = updateChart;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeChoiceBox.setItems(FXCollections.observableArrayList(TimeFrame.values()));
        timeChoiceBox.getSelectionModel().select(TimeFrame.AllTime);
        timeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observe, old, neo) -> updateChart.run());
    }    
    
    public LocalDate getTimeFrame() {
        return timeChoiceBox.getSelectionModel().getSelectedItem().getEarliest();
    }
    
}

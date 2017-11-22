/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.dumpster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class PerformanceTesterController implements Initializable {

    @FXML
    private TextField numMediaTxt;
    @FXML
    private TextField numReviewTxt;
    @FXML
    private TextField numFactTxt;
    @FXML
    private TextField lenDescrTxt;
    @FXML
    private TextField lenReviewTxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addData(ActionEvent event) {
        int numMedia = Integer.parseInt(numMediaTxt.getText());
        int numReview = Integer.parseInt(numReviewTxt.getText());
        int numFact = Integer.parseInt(numFactTxt.getText());
        int lenDescr = Integer.parseInt(lenDescrTxt.getText());
        int lenReview = Integer.parseInt(lenReviewTxt.getText());
        if(new Alert(Alert.AlertType.WARNING,"Are you sure you want to inject random data?",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES))
            if(new Alert(Alert.AlertType.WARNING,"Absolutely positive? "+numMedia+" is a lot of cleanup",ButtonType.NO,ButtonType.YES).showAndWait().get().equals(ButtonType.YES))
                new Dumpster(numMedia,numReview,numFact,lenDescr,lenReview).dump();
    }

    @FXML
    private void close(ActionEvent event) {
        numMediaTxt.getScene().getWindow().hide();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.about;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import retrospector.util.GithubUtil;

/**
 * Let people know a little about Retrospector
 *
 * @author nonfrt
 */
public class AboutController implements Initializable {

    public static final String VERSION = "v1.2.3";
    
    @FXML
    private Text txtVersion;
    @FXML
    private Text txtLatestVersion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtVersion.setText(VERSION);
    }    
    
    public void checkUpdates(ActionEvent e) {
        String latest = GithubUtil.getLastestVersion();
        txtLatestVersion.setText(latest);
    }
    
}

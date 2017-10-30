/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.preferences;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import retrospector.util.PropertyManager;
import retrospector.util.PropertyManager.Configuration;
import retrospector.util.Toast;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class PreferencesController implements Initializable {

    @FXML
    private TextField txtDefaultUser;
    @FXML
    private TextField txtDefaultRating;
    @FXML
    private TextField txtCategories;
    @FXML
    private TextField txtFactoids;
    @FXML
    private TextField txtPastDays;
    @FXML
    private TextField txtGithubUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProperties(PropertyManager.loadProperties());
    }    
    
    public void setProperties(Configuration config) {
        txtDefaultUser.setText(config.getDefaultUser());
        txtDefaultRating.setText(config.getDefaultRating().toString());
        txtCategories.setText(String.join(",", config.getCategories()));
        txtFactoids.setText(String.join(",", config.getFactoids()));
        txtPastDays.setText(config.getViewPastDays().toString());
        txtGithubUser.setText(config.getGithubUser());
    }
    
    public Configuration getProperties() {
        Configuration config = new Configuration();
        config.setDefaultUser(txtDefaultUser.getText());
        config.setDefaultRating(Integer.parseInt(txtDefaultRating.getText()));
        config.setCategories(txtCategories.getText().split(","));
        config.setFactoids(txtFactoids.getText().split(","));
        config.setViewPastDays(Integer.parseInt(txtPastDays.getText()));
        config.setGithubUser(txtGithubUser.getText());
        return config;
    }
    
    public void exit() {
        Stage stage = (Stage) txtGithubUser.getScene().getWindow();
        stage.hide();
    }
    
    public void save(ActionEvent e) {
        try {
            PropertyManager.saveProperties(getProperties());
            PropertyManager.forceLoadProperties(true);
            Toast.makeText(new Stage(), "Saved!");
        } catch(IOException|URISyntaxException ex) {
            System.err.println("Saving properties failed: "+ex.getMessage());
            Toast.makeText(new Stage(), "Save failed");
        }
        exit();
    }
    
    public void cancel(ActionEvent e) {
        exit();
    }
    
}

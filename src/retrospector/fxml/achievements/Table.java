/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author nonfrt
 */
public abstract class Table<T> extends GridPane {
    
    public Table(String title, List<String> headers, List<T> scores) {
        super();
        setFormatting();
        setHeader(title, headers);
        for (int i = 0; i<scores.size(); i++) {
            addRow(scores.get(i),i+2);
        }
    }
    
    private void setFormatting() {
        this.setVgap(10);
        this.setHgap(5);
    }
    
    private void setHeader(String title, List<String> headers) {
        Text titleHeader = new Text(title);
        titleHeader.setStyle("-fx-font: 34 ariel");
        this.add(titleHeader, 0, 0, 3, 1);
        
        for (int i = 0; i<headers.size(); i++) {
            String header = headers.get(i);
            Text textHeader = new Text(header);
            textHeader.setStyle("-fx-font-weight: bold");
            this.add(textHeader, i, 1);
        }
    }
    
    public abstract void addRow(T score, Integer row);
}
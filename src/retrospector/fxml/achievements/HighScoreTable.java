/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import retrospector.fxml.chart.StatsTabController;
import retrospector.model.DataManager;

/**
 *
 * @author nonfrt
 */
public class HighScoreTable extends GridPane {
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yy");
    
    public HighScoreTable(List<HighScore> scores) {
        super();
        setFormatting();
        setHeader();
        for (int i = 0; i<scores.size(); i++) {
            addRow(scores.get(i),i+2);
        }
    }
    
    private void setFormatting() {
        this.setVgap(10);
        this.setHgap(5);
    }
    
    private void setHeader() {
        Text titleHeader = new Text("High Scores");
//        titleHeader.setStyle("-fx-font-weight: bold");
        titleHeader.setStyle("-fx-font: 34 ariel");
        this.add(titleHeader, 0, 0, 3, 1);
        
        Text categoryHeader = new Text("Category");
        categoryHeader.setStyle("-fx-font-weight: bold");
        this.add(categoryHeader, 0, 1);
        
        Text scoreHeader = new Text("Score");
        scoreHeader.setStyle("-fx-font-weight: bold");
        this.add(scoreHeader, 1, 1);
        
        Text dateHeader = new Text("Month");
        dateHeader.setStyle("-fx-font-weight: bold");
        this.add(dateHeader, 2, 1);
    }
    
    private void addRow(HighScore score, Integer row) {
        Text categoryTxt = new Text(score.getCategory());
        Text scoreTxt = new Text(score.getScore().toString());
        Text monthTxt = new Text(formatter.format(score.getMonth()));
        this.addRow(row, categoryTxt, scoreTxt, monthTxt);
    }
}

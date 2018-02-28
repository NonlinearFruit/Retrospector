/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.tables;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javafx.scene.text.Text;

/**
 *
 * @author nonfrt
 */
public class MediaStreakTable extends Table<MediaStreak>{
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yy");
    
    public MediaStreakTable(List<MediaStreak> scores) {
        super( "Media Streaks", Arrays.asList("Category", "Days", "Date"), scores );
    }
    
    @Override
    public void addRow(MediaStreak score, Integer row) {
        Text categoryTxt = new Text(score.getCategory());
        Text scoreTxt = new Text(score.getDays().toString());
        Text monthTxt = new Text(MediaStreakTable.formatter.format(score.getDate()));
        this.addRow(row, categoryTxt, scoreTxt, monthTxt);
    }
}

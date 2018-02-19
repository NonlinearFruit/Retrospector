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

/**`
 *
 * @author nonfrt
 */
public class HighScoreTable extends Table<HighScore> {
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yy");
    
    public HighScoreTable(List<HighScore> scores) {
        super( "High Scores", Arrays.asList("Category", "Score", "Month"), scores );
    }
    
    @Override
    public void addRow(HighScore score, Integer row) {
        Text categoryTxt = new Text(score.getCategory());
        Text scoreTxt = new Text(score.getScore().toString());
        Text monthTxt = new Text(HighScoreTable.formatter.format(score.getMonth()));
        this.addRow(row, categoryTxt, scoreTxt, monthTxt);
    }
}

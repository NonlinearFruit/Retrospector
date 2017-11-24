/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.util.Arrays;
import java.util.List;
import javafx.scene.text.Text;

/**
 *
 * @author nonfrt
 */
public class TopMediaTable extends Table<TopMedia>{

    public TopMediaTable(List<TopMedia> media) {
        super( "Top Media", Arrays.asList("Category", "Count", "Title"), media );
    }
    
    @Override
    public void addRow(TopMedia entry, Integer row) {
        Text category = new Text(entry.getCategory());
        Text count = new Text(entry.getCount()+"");
        Text title = new Text(entry.getTitle());
        this.addRow(row,category,count,title);
    }
    
}

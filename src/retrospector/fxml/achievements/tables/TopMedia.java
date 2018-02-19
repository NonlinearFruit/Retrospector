/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.tables;

/**
 *
 * @author nonfrt
 */
public class TopMedia {
    private String category;
    private String title;
    private Integer count;

    public TopMedia(String category) {
        this(category,"N/A",0);
    }
    
    public TopMedia(String category, String title, Integer count) {
        this.category = category;
        this.title = title;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
}

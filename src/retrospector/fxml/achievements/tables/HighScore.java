/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.tables;

import java.time.LocalDate;

/**
 *
 * @author nonfrt
 */
public class HighScore {
    private String category;
    private LocalDate month;
    private Integer score;
        
    public HighScore(String category) {
        this(category,LocalDate.now().withDayOfMonth(1),0);
    }

    public HighScore(String category, LocalDate month, Integer score) {
        this.category = category;
        this.month = month;
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return category+": "+score+" "+month.toString();
    }
}

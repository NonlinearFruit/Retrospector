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
public class MediaStreak {
    private LocalDate date;
    private String category;
    private Integer days;

    public MediaStreak(String category, LocalDate date, Integer days) {
        this.date = date;
        this.category = category;
        this.days = days;
    }

    public MediaStreak(String category) {
        this(category, LocalDate.now(), 0);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

/**
 *
 * @author nonfrt
 */
public class Factoid {
    private String title;
    private String content;
    private Integer id;
    private Integer mediaId;

    public Factoid() {
        this(DataManager.getFactiodTypes()[0]);
    }
    
    public Factoid(String title) {
        this(title,"");
    }
    
    public Factoid(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }
}

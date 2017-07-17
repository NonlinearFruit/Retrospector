/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import retrospector.Retrospector;

/**
 *
 * @author nonfrt
 */
public class AchievementFX {
    public static final String lockedImage = "";
    public static final Color lockedColor = new Color(.5,.5,.5,1);
    public static final Color firstColor = Color.GOLD;
    public static final Color secondColor = Color.SILVER;
    public static final Color thirdColor = new Color(205.0/255,127.0/255,50.0/255,1);
    private static final Double imageSize = 70.0;
    private static final Font fa = Font.loadFont(Retrospector.class.getResourceAsStream("res/fontawesome-webfont.ttf"), imageSize);
    
    public static Color tierColor(Integer tier) {
        if(tier==0)
            return lockedColor;
        if(tier==1)
            return firstColor;
        if(tier==2)
            return secondColor;
        
        return thirdColor;
    }
    
    public static Text trophyize(String text, Integer tier) {
        return trophyize(text,tier,imageSize);
    }
    
    public static Text trophyize(String text, Integer tier, Double size) {
        Text node = new Text(text);
        node.setFill(tierColor(tier));
        node.setFont(Font.font(fa.getFamily(), size));
        Lighting l = new Lighting();
        l.setSurfaceScale(5.0f);
        node.setEffect(l);
        return node;
    }
    
    private Achievement achievement;
    private Text image;
    
    public AchievementFX(Achievement achievement) {
        this.achievement = achievement;
        this.achievement.setDescription("taehc t'noD"); // For screenshots
        
        // Unlocked or Locked? that is the question
        if( !achievement.isUnlocked() ) {
            this.achievement.setTitle("");
            this.achievement.setDescription("");
            this.achievement.setSymbol(lockedImage);
            this.achievement.setTier(0);
        }
        
        // Create an JavaFX displayable version
        image = trophyize(this.achievement.getSymbol(),this.achievement.getTier());
    }
    
    public Achievement getAchievement() {
        return achievement;
    }
    
    public Node getDisplay() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        
        Text title = new Text(achievement.getTitle());
        title.setFont(Font.font(20));
        
        Text desc = new Text(achievement.getDescription());
        
        Integer progress = achievement.getProgress();
        ProgressBar bar = new ProgressBar(progress*1.0/100);
        Tooltip.install(bar,new Tooltip("["+progress+"%] "+achievement.getHint()));
        
        box.getChildren().add(title);
        box.getChildren().add(image);
        
        if(achievement.isUnlocked())
            box.getChildren().add(desc);
        else
            box.getChildren().add(bar);
        return box;
    }
}

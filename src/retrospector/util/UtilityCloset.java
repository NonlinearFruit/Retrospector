/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

/**
 *
 * @author nonfrt
 */
public class UtilityCloset {
    
    public static Label labelMeImpressed(Node node, String name) {
        return labelMeImpressed(node,name,ContentDisplay.RIGHT);
    }
    
    public static Label labelMeImpressed(Node node, String name, ContentDisplay side) {
        Label label = new Label(name,node);
        label.setContentDisplay(side);
        return label;
    }
    
}

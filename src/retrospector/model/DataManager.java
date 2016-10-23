/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import retrospector.util.PropertyManager;
import retrospector.util.PropertyManager.Configuration;

/**
 *
 * @author nonfrt
 */
public class DataManager {

    public static String getDefaultUser(){
        return PropertyManager.loadProperties().getDefaultUser();
    }
    
    public static Integer getMaxRating(){
        return PropertyManager.loadProperties().getMaxRating();
    }
    
    public static Integer getDefaultRating(){
        return PropertyManager.loadProperties().getDefaultRating();
    }
}

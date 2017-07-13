/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements.accumulators;

import java.io.File;
import java.util.List;
import retrospector.fxml.achievements.Accumulator;
import retrospector.fxml.achievements.Achievement;
import retrospector.model.DataManager;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public class FileSystem extends Accumulator {
    
    private Achievement paranoid;
    private Achievement inspector;
    
    public FileSystem() {
        paranoid = new Achievement("","Paranoia","Keep a backup",2);
        paranoid.setHint("Be prepared");
        inspector = new Achievement("","Inspector","Edit the .config file",2);
        inspector.setHint("Customize");
    }
    
    @Override
    public void accumulate(Object item) {
        Integer progress = 0;
        try {
            progress = new File(PropertyManager.retroFolder+"/Backup").list().length>0? 100:0;
        } catch(Exception ex) { progress = 0; }
        paranoid.setProgress(progress);
        
        PropertyManager.Configuration dusty = new PropertyManager.Configuration();
        if( dusty.getDefaultUser().equals(DataManager.getDefaultUser()) && 
                dusty.getCategories().length == DataManager.getCategories().length &&
                dusty.getFactoids().length == DataManager.getFactoids().size())
            progress = 0;
        else
            progress = 100;
        inspector.setProgress(progress);
    }

    @Override
    public List getShowableAchievements() {
        return super.getShowableAchievements(paranoid, inspector);
    }
    
}

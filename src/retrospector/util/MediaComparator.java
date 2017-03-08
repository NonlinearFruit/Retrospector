/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.Comparator;
import retrospector.model.Media;

/**
 * Compares media in a 'ranking' way. The ordering is as follows:
 * + Average of all Reviews comes first
 * + Number of Reviews comes second
 * + Alphabetical comes third
 *   - By title, creator, season, episode, category
 * @author nonfrt
 */
public class MediaComparator implements Comparator<Media>{

    @Override
    public int compare(Media o1, Media o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

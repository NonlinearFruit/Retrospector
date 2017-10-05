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
        int result;
        
        // Average Review [Larger==Better]
        result = o1.getAverageRating().compareTo(o2.getAverageRating())*-1;
        if(result!=0)
            return result;
                    
        // Number of Reviews [Larger==Better]
        result = Integer.compare(o1.getReviews().size(),o2.getReviews().size())*-1;
        if(result!=0)
            return result;
        
        // Current Review [Larger==Better]
        result = o1.getCurrentRating().compareTo(o2.getCurrentRating())*-1;
        if(result!=0)
            return result;
        
        // Title
        result = o1.getTitle().compareTo(o2.getTitle());
        if(result!=0)
            return result;
        
        // Creator
        result = o1.getCreator().compareTo(o2.getCreator());
        if(result!=0)
            return result;
        
        // Season
        result = o1.getSeason().compareTo(o2.getSeason());
        if(result!=0)
            return result;
        
        // Episode
        result = o1.getEpisode().compareTo(o2.getEpisode());
        if(result!=0)
            return result;
        
        // Category
        result = o1.getCategory().compareTo(o2.getCategory());
        if(result!=0)
            return result;
        
        return 0;
    }
    
}

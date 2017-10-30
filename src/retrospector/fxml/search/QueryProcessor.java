/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.search;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import retrospector.model.Factoid;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class QueryProcessor {
    
    public static enum Operator {
        CONTAINS('~'), LESS_THAN('<'), GREATER_THAN('>'), EQUAL('=');
        public final char symbol;
        Operator(char symbol){ this.symbol = symbol; }
        @Override
        public String toString(){ return ""+symbol; }
        public static Operator fromChar(char x){ 
            return Arrays.stream(Operator.values()).reduce((o1,o2)->o1.symbol==x?o1:o2).get();
        }
    }
    
    public static enum Logic {
        AND(':'), OR('|'), NOT('!'), CMD('`');
        public final char symbol;
        Logic(char symbol){ this.symbol = symbol; }
        @Override
        public String toString(){ return ""+symbol; }
        public static Logic fromChar(char x){ 
            return Arrays.stream(Logic.values()).reduce((c1,c2)->c1.symbol==x?c1:c2).get();
        }
    }
    
    public static enum Command {
        TITLE('T'), CREATOR('C'), SEASON('S'), EPISODE('E'), 
        CATEGORY('A'), FACTIOD('F'), RATING('#'), USER('U'), DATE('D');
        public final char symbol;
        Command(char symbol){ this.symbol = symbol; }
        @Override
        public String toString(){ return ""+symbol; }
        public static Command fromChar(char x){ 
            return Arrays.stream(Command.values()).reduce((c1,c2)->c1.symbol==x?c1:c2).get();
        }
    }
    
    public static String toCheatsheet(){
        return "";
    }
    
    private String query;
    private String description;
    private List<Media> allMedia;
    QueryProcessor(String query, List<Media> media){
        this.query = query;
        this.allMedia = media;
    }
    
    private static String cleanSearchable(String searchable) {
        return searchable.toLowerCase();
    }
    
    private static List<String> toSearchable(Media media) {
        List<String> searchables = new ArrayList<>();
        searchables.addAll(Arrays.asList(
                cleanSearchable(media.getTitle()),
                cleanSearchable(media.getCreator()),
                cleanSearchable(media.getSeason()),
                cleanSearchable(media.getEpisode()),
                cleanSearchable(media.getCategory())
        ));
        for (Factoid fact : media.getFactoids()) {
            searchables.add(cleanSearchable(fact.getContent()));
        }
        return searchables;
    }
    
    public static boolean isMatchForMedia(String query, Media media) {
        boolean passAND = true;
        if( 
            query.endsWith(""+Logic.AND) || 
            query.endsWith(""+Logic.OR)  || 
            query.endsWith(""+Logic.NOT) 
        )
                query = query.substring(0, query.length()-1);
        
        // Split on AND first
        String[] queries = query.split(Pattern.quote(""+Logic.AND));
        List<String> searchables = toSearchable(media);
        for (String q : queries) {
            // Split on OR second
            String[] optns = q.split(Pattern.quote(""+Logic.OR));
            boolean passOR = false;
            for (String optn : optns) {
                boolean hasNegator = optn.length()>1 && optn.startsWith(""+Logic.NOT);
                boolean hasCmd = optn.length()>0 && optn.startsWith(""+Logic.CMD);
                if ( !hasNegator && 
                        !hasCmd  &&
                        searchables.stream().anyMatch(s -> s.contains(cleanSearchable(optn))) )
                    passOR = true;
                else if ( hasNegator && !searchables.stream().anyMatch(s -> s.contains(optn.substring(1))) )
                    passOR = true;
                else if ( hasCmd && isMatchForCmd(optn,media) )
                    passOR = true;
            }
            if (!passOR) {
                passAND = false;
            }
        }
        return passAND;
    }
    
    /**
     * Takes a command and decides whether or not the media matches it
     * @param cmd ex `A~movie
     * @param media
     * @return 
     */
    private static boolean isMatchForCmd(String cmd, Media media) {
        // Get rid of CMD op
        if( cmd.startsWith(""+Logic.CMD))
            cmd = cmd.substring(1, cmd.length());
        // Make sure cmd has a Command, Operator, and at least 1 character
        if( cmd.length() < 3)
            return true;
        
        // Break it into pieces
        char command = cmd.charAt(0);
        char operator = cmd.charAt(1);
        String valueLookedFor = cmd.substring(2,cmd.length());
        
        // Find the value
        String valueFound; 
        switch(Command.fromChar(command)) {
            case TITLE:
                valueFound = media.getTitle();
                break;
            case CREATOR: 
                valueFound = media.getCreator();
                break;
            case SEASON: 
                valueFound = media.getSeason();
                break;
            case EPISODE: 
                valueFound = media.getEpisode();
                break;
            case CATEGORY: 
                valueFound = media.getCategory();
                break;
            case DATE:
            case RATING:
            case USER:
                return areReviewsMatch(command,operator,valueLookedFor,media.getReviews());
            case FACTIOD:
                return areFactoidsMatch(command,operator,valueLookedFor, media.getFactoids());
            default:
                valueFound = "";
        }
        
        valueFound = cleanSearchable(valueFound);
        valueLookedFor = cleanSearchable(valueLookedFor);
        
        // Compare with the operator
        boolean pass = false;
        switch(Operator.fromChar(operator)) {
            case CONTAINS:
                pass = valueFound.contains(valueLookedFor);
                break;
            case EQUAL:
                pass = valueFound.equals(valueLookedFor);
                break;
            default:
                pass = false;
        }
        
        return pass;
    }
    
    private static boolean areReviewsMatch(char command, char operator, String valueLookedFor, List<Review> reviews) {
        boolean pass = false;
        for (Review review : reviews) {
            if (isReviewMatch(command, operator, valueLookedFor, review))
                pass = true;
        }
        
        return pass;
    }
    
    /**
     * Takes a review and decides whether or not it matches the command.
     * @param cmd ex `U~Ben
     * @param reviews
     * @return 
     */
    private static Boolean isReviewMatch(char command, char operator, String valueLookedFor, Review review) {
        
        // Find the value
        valueLookedFor = cleanSearchable(valueLookedFor);
        boolean pass;
        switch(Command.fromChar(command)) {
            case DATE:
                // Compare with the operator
                LocalDate date;
                try{ date = LocalDate.parse(valueLookedFor, Review.FORMATTER); }
                catch(DateTimeParseException ex) {System.out.println("Bad date: "+valueLookedFor); return false;}
                pass = false;
                switch (Operator.fromChar(operator)) {
                    case EQUAL:
                        pass = review.getDate().isEqual(date);
                        break;
                    case GREATER_THAN:
                        pass = review.getDate().isAfter(date);
                        break;
                    case LESS_THAN:
                        pass = review.getDate().isBefore(date);
                        break;
                }

                return pass;
                
            case RATING:
                // Compare with the operator
                Integer rating = Integer.parseInt(valueLookedFor);
                pass = false;
                switch (Operator.fromChar(operator)) {
                    case EQUAL:
                        pass = review.getRating() == rating;
                        break;
                    case GREATER_THAN:
                        pass = review.getRating() > rating;
                        break;
                    case LESS_THAN:
                        pass = review.getRating() < rating;
                        break;
                }

                return pass;
                
            case USER:
                // Compare with the operator
                String user = cleanSearchable(review.getUser());
                pass = false;
                switch (Operator.fromChar(operator)) {
                    case CONTAINS:
                        pass = user.contains(valueLookedFor);
                        break;
                    case EQUAL:
                        pass = user.equals(valueLookedFor);
                        break;
                }

                return pass;
        }
        
        return false;
    }
    
    private static boolean areFactoidsMatch(char command, char operator, String valueLookedFor, List<Factoid> factoids) {
        boolean pass = false;
        for (Factoid factoid : factoids) {
            if (isFactoidMatch(command, operator, valueLookedFor, factoid))
                pass = true;
        }
        
        return pass;
    }
    
    /**
     * Takes a factoid and decides whether or not it matches the command.
     * @param cmd ex `F~Ben
     * @param reviews
     * @return 
     */
    private static Boolean isFactoidMatch(char command, char operator, String valueLookedFor, Factoid factoid) {
        
        
        return false;
    }
    
    public List<Media> getMatches() {
        return allMedia.stream()
                .filter(m->isMatchForMedia(query, m))
                .collect(Collectors.toList());
    }
    
    public String getDescription() {
        return description;
    }
}

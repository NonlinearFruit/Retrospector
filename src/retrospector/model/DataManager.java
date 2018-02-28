/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import retrospector.model.Media.Type;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public class DataManager {
    static String connString = "jdbc:hsqldb:file:"+PropertyManager.retroFolder;
    static Connection conn = null;

    // Config File Stuff
    public static String getDefaultUser(){
        return PropertyManager.loadProperties().getDefaultUser();
    }
    
    public static Integer getMaxRating(){
        return PropertyManager.loadProperties().getMaxRating();
    }
    
    public static Integer getDefaultRating(){
        return PropertyManager.loadProperties().getDefaultRating();
    }
    
    public static String[] getCategories(){
        return PropertyManager.loadProperties().getCategories();
    }
    
    public static String[] getFactiodTypes(){
        return PropertyManager.loadProperties().getFactoids();
    }
    
    public static String getGithubUser() {
        return PropertyManager.loadProperties().getGithubUser();
    }
    
    public static Integer getPastDays() {
        return PropertyManager.loadProperties().getViewPastDays();
    }
    
    
    public static Connection getConnection(){
        try{
            if(conn==null)
                conn = DriverManager.getConnection(connString,"SA","");
        } catch(SQLException e){System.err.println("Connection failed.");}
        return conn;
            
    }
    
    public static void makeBackup(){
        try{
            String backupDir = "'"+PropertyManager.retroFolder+"/Backup/'";
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("BACKUP DATABASE TO "+backupDir);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static ObservableList<Media> getWishlist() {
        return getMedia(true);
    }
    
    public static ObservableList<Media> getMedia() {
        return getMedia(false);
    }
    
    /**
     * Gets media objects from the database
     * @param wishlistOrNo [true: wishlist only, false: media only]
     * @return 
     */
    private static ObservableList<Media> getMedia(boolean wishlistOrNo){
        Statement stmt;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ObservableList<Media> list = FXCollections.observableArrayList();

        try{
            stmt = getConnection().createStatement();
            String equals = wishlistOrNo? "=" : "<>";
            rs = stmt.executeQuery("" +
                    "SELECT media.* \n" +
                    "FROM media \n" +
                    "LEFT JOIN review " +
                    "ON media.id = review.mediaID\n" +
                    "WHERE type"+equals+"'WISHLIST'" +
                    "GROUP BY media.id\n" +
                    "ORDER BY (CASE WHEN MAX(review.id) IS NULL THEN 1 ELSE 0 END) DESC, MAX(review.id) DESC\n" + // Nulls first then order by most recent review
                    "");
            while (rs.next()) {
                try{
                    Media medium = new Media();
                    medium.setId(rs.getInt("id"));
                    medium.setTitle(rs.getString("title"));
                    medium.setCreator(rs.getString("creator"));
                    medium.setSeason(rs.getString("season"));
                    medium.setEpisode(rs.getString("episode"));
                    medium.setDescription(rs.getString("description"));
                    medium.setCategory(rs.getString("category"));
                    medium.setType(Type.valueOf(rs.getString("type")));
                    try{
                        rs2 = stmt.executeQuery("select * from review where mediaID="+medium.getId());
                        while(rs2.next()){
                            try{
                                Review review = new Review();
                                review.setId(rs2.getInt("id"));
                                review.setMediaId(rs2.getInt("mediaID"));
                                review.setUser(rs2.getString("reviewer"));
                                review.setDate(rs2.getDate("date").toLocalDate());
                                review.setReview(rs2.getString("review"));
                                review.setRating(rs2.getInt("rate"));

                                medium.getReviews().add(review);
                            } catch(SQLException e){System.err.println("Get review failed. (getMedia)");}
                        }
                    } catch(SQLException e){System.err.println("Get review list failed. (getMedia)");}
                    try{
                        rs2 = stmt.executeQuery("select * from factoid where mediaID="+medium.getId());
                        while(rs2.next()){
                            try{
                                Factoid factoid = new Factoid();
                                factoid.setId(rs2.getInt("id"));
                                factoid.setMediaId(rs2.getInt("mediaID"));
                                factoid.setTitle(rs2.getString("title"));
                                factoid.setContent(rs2.getString("content"));

                                medium.getFactoids().add(factoid);
                            } catch(SQLException e){System.err.println("Get factoid failed. (getMedia)");}
                        }
                    } catch(SQLException e){System.err.println("Get factoid list failed. (getMedia)");}
                    list.add(medium);
                } catch(SQLException e){System.err.println("Get media failed. (getMedia)");}
            }
        } catch(SQLException e){System.err.println("Get media list failed. (getMedia)"); e.printStackTrace();}
        return  list;
    }
    
    public static ObservableList<Review> getReviews(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<Review> reviews = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from review");
            while (rs.next()) {
                try {
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setMediaId(rs.getInt("mediaID"));
                    review.setUser(rs.getString("reviewer"));
                    review.setDate(rs.getDate("date").toLocalDate());
                    review.setReview(rs.getString("review"));
                    review.setRating(rs.getInt("rate"));

                    reviews.add(review);
                } catch (SQLException e) {System.err.println("Get review failed. (getReviews)");}
            }
        } catch (SQLException e) {System.err.println("Get review list failed. (getReviews)");}
        return reviews;
    }
    
    public static ObservableList<Factoid> getFactoids(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<Factoid> factoids = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from factoid");
            while (rs.next()) {
                try {
                    Factoid factoid = new Factoid();
                    factoid.setId(rs.getInt("id"));
                    factoid.setMediaId(rs.getInt("mediaID"));
                    factoid.setTitle(rs.getString("title"));
                    factoid.setContent(rs.getString("content"));
                    factoids.add(factoid);
                } catch (SQLException e) {System.err.println("Get factoid failed. (getFactoids)");}
            }
        } catch (SQLException e) {System.err.println("Get factoid list failed. (getFactoids)");}
        return factoids;
    }
    
    public static ObservableList<Factoid> getFactoidsByType(String type){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<Factoid> factoids = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from factoid where title='"+type+"'");
            while (rs.next()) {
                try {
                    Factoid factoid = new Factoid();
                    factoid.setId(rs.getInt("id"));
                    factoid.setMediaId(rs.getInt("mediaID"));
                    factoid.setTitle(rs.getString("title"));
                    factoid.setContent(rs.getString("content"));
                    factoids.add(factoid);
                } catch (SQLException e) {System.err.println("Get factoid failed. (getFactoids)");}
            }
        } catch (SQLException e) {System.err.println("Get factoid list failed. (getFactoids)");}
        return factoids;
    }
    
    /**
     * Create a list of all unique users in the db so far
     * @return 
     */
    public static ObservableList<String> getUsers(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<String> users = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select distinct reviewer from review");
            while (rs.next()) {
                try {
                    users.add(rs.getString(1));
                } catch (SQLException e) {System.err.println("Get reviewer failed. (getUsers)");}
            }
        } catch (SQLException e) {System.err.println("Get reviewer list failed. (getUsers)");}
        return users;
    }
    
    /**
     * Create a list of all unique titles in the db so far
     * @return 
     */
    public static ObservableList<String> getTitles(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<String> titles = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select distinct title from media m where m.type<>'WISHLIST'");
            while (rs.next()) {
                try {
                    titles.add(rs.getString(1));
                } catch (SQLException e) {System.err.println("Get title failed. (getTitles)");}
            }
        } catch (SQLException e) {System.err.println("Get title list failed. (getTitles)");}
        return titles;
    }
    
    /**
     * Create a list of all unique creator in the db so far
     * @return 
     */
    public static ObservableList<String> getCreators(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<String> creators = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select distinct creator from media m where m.type<>'WISHLIST'");
            while (rs.next()) {
                try {
                    creators.add(rs.getString(1));
                } catch (SQLException e) {System.err.println("Get creator failed. (getCreators)");}
            }
        } catch (SQLException e) {System.err.println("Get creator list failed. (getCreators)");}
        return creators;
    }
    
    /**
     * Create a list of all unique season in the db so far
     * @return 
     */
    public static ObservableList<String> getSeasons(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<String> seasons = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select distinct season from media m where m.type<>'WISHLIST'");
            while (rs.next()) {
                try {
                    seasons.add(rs.getString(1));
                } catch (SQLException e) {System.err.println("Get season failed. (getSeasons)");}
            }
        } catch (SQLException e) {System.err.println("Get season list failed. (getSeasons)");}
        return seasons;
    }
    
    /**
     * Create a list of all unique episode in the db so far
     * @return 
     */
    public static ObservableList<String> getEpisodes(){
        Statement stmt;
        ResultSet rs = null;

        ObservableList<String> episodes = FXCollections.observableArrayList();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select distinct title from media m where m.type<>'WISHLIST'");
            while (rs.next()) {
                try {
                    episodes.add(rs.getString(1));
                } catch (SQLException e) {System.err.println("Get episode failed. (getEpisodes)");}
            }
        } catch (SQLException e) {System.err.println("Get episode list failed. (getEpisodes)");}
        return episodes;
    }
    
    /**
     * Start the DB. Also creates the DB if it is not found
     */
    public static void startDB(){
        String createMedia = ""
        + "create table if not exists media ("
        + "id integer not null generated always as identity (start with 1, increment by 1),   "
        + "title varchar(1000000),"
        + "creator varchar(1000000),"
        + "season varchar(1000000),"
        + "episode varchar(1000000),"
        + "description varchar(1000000),"
        + "category varchar(1000000),"
        + "type varchar(1000000),"
        + "constraint primary_key_media primary key (id))";
      
        String createReview = ""
        + "create table if not exists review ("
        + "id integer not null generated always as identity (start with 1, increment by 1),   "
        + "mediaID integer not null,   "
        + "reviewer varchar(1000000),"
        + "date date,"
        + "review varchar(1000000),"
        + "rate int,"
        + "constraint primary_key_review primary key (id),"
        + "constraint foreign_key_review foreign key (mediaID) references media (id) on delete cascade)";
        
        String createFactoid = ""
        + "create table if not exists factoid ("
        + "id integer not null generated always as identity (start with 1, increment by 1),   "
        + "mediaID integer not null,   "
        + "title varchar(1000000),"
        + "content varchar(1000000),"
        + "constraint primary_key_factoid primary key (id),"
        + "constraint foreign_key_factoid foreign key (mediaID) references media (id) on delete cascade)";
        
//        String createAchievement = ""
//        + "create table if not exists achievement ("
//        + "id integer not null generated always as identity (start with 1, increment by 1),   "
//        + "title varchar(1000000),"
//        + "description varchar(1000000),"
//        + "achieved boolean not null,   "
//        + "unlockFunction varchar(1000000),"
//        + "symbol varchar(1000000),"
//        + "constraint primary_key_factoid primary key (id)";
        
        try {
            connString += "/Retrospector";
            System.out.println(connString);
            Statement stmt;
        
            stmt = getConnection().createStatement();
            
            stmt.execute(createMedia);
            
            stmt.execute(createReview);
            
            stmt.execute(createFactoid);
            
        } catch (SQLException ex) {
            System.err.println("Create error in startDB in connection" + ex);
        }
    }
    
    /**
     * Close the DB connection
     */
    public static void endDB(){
        try {
            DriverManager.getConnection(connString+";shutdown=true","SA","");
            System.out.println("HSQLDB shut down normally");
        } catch (SQLException ex) {
            System.err.println("HSQLDB did not shut down normally");
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Create a new media in the DB
     * @param media 
     */
    public static int createDB(Media media){
        int id = -1;
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement(
                    "insert into media(title,creator,season,episode,description,category,type) values(?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, media.getTitle());
            pstmt.setString(2, media.getCreator());
            pstmt.setString(3, media.getSeason());
            pstmt.setString(4, media.getEpisode());
            pstmt.setString(5, media.getDescription());
            pstmt.setString(6, media.getCategory());
            pstmt.setString(7, media.getType().toString());
            int updated = pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            boolean key = rs.next();
            if(updated==1 && key){
                id = rs.getInt(1);
                for (Review review : media.getReviews()) {
                    review.setMediaId(id);
                    review.setId(createDB(review));
                }
                for (Factoid factoid : media.getFactoids()) {
                    factoid.setMediaId(id);
                    factoid.setId(createDB(factoid));
                }
            } else if(media.getFactoids().size()>0 || media.getReviews().size()>0) {
                System.err.println("Reviews/Factoids not Saved! :(");
            }
            
        } catch (SQLException ex) {
            System.err.println("createDB error in connection" + ex);
        }
        return id;
    }
    
    /**
     * Create a new review in the DB
     * @param review 
     */
    public static int createDB(Review review){
        int id = -1;
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement(
                    "insert into review(mediaId,reviewer,date,review,rate) values(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getMediaId());
            pstmt.setString(2, review.getUser());
            pstmt.setDate(3, Date.valueOf(review.getDate()));
            pstmt.setString(4, review.getReview());
            pstmt.setInt(5, review.getRating()); // If ratings ever get decimal again fix this!
            int updated = pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            boolean key = rs.next();
            if(updated==1 && key){
                id = rs.getInt(1);
            }
            
            
        } catch (SQLException ex) {
            System.err.println("createDB error in connection" + ex);
        }
        return id;
    }
    
    /**
     * Create a new factoid in the DB
     * @param factoid 
     */
    public static int createDB(Factoid factoid){
        int id = -1;
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement(
                    "insert into factoid(mediaId,title,content) values(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, factoid.getMediaId());
            pstmt.setString(2, factoid.getTitle());
            pstmt.setString(3, factoid.getContent());
            int updated = pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            boolean key = rs.next();
            if(updated==1 && key){
                id = rs.getInt(1);
            }
            
            
        } catch (SQLException ex) {
            System.err.println("createDB error in connection" + ex);
        }
        return id;
    }
    
    /**
     * Update an existing media in DB
     * @param media 
     */
    public static void updateDB(Media media){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("update media set title=?,creator=?,season=?,episode=?,description=?,category=?,type=? where id=?");
            pstmt.setString(1, media.getTitle());
            pstmt.setString(2, media.getCreator());
            pstmt.setString(3, media.getSeason());
            pstmt.setString(4, media.getEpisode());
            pstmt.setString(5, media.getDescription());
            pstmt.setString(6, media.getCategory());                  
            pstmt.setString(7, media.getType().toString());                  
            pstmt.setInt(8, media.getId());                  
            pstmt.executeUpdate();
            
            for (Review review : media.getReviews()) {
                review.setMediaId(media.getId());
                if (review.getId() != null && review.getId() != 0)
                    updateDB(review);
                else
                    createDB(review);
            }
            
            for (Factoid factoid : media.getFactoids()) {
                factoid.setMediaId(media.getId());
                if (factoid.getId() != null && factoid.getId() != 0)
                    updateDB(factoid);
                else
                    createDB(factoid);
            }
            
            if(media.getId()<1)
                System.err.println("Bad media update");
        } catch (SQLException ex) {
            System.err.println("updateDB error in connection" + ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Update an existing media in DB
     * @param review 
     */
    public static void updateDB(Review review){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("update review set mediaId=?,reviewer=?,date=?,review=?,rate=? where id=?");
            pstmt.setInt(1, review.getMediaId());
            pstmt.setString(2, review.getUser());
            pstmt.setDate(3, Date.valueOf(review.getDate()));
            pstmt.setString(4, review.getReview());
            pstmt.setInt(5, review.getRating()); // If ratings ever get decimal again fix this!
            pstmt.setInt(6, review.getId()); 
            pstmt.executeUpdate();
            
            if(review.getId()<1)
                System.err.println("Bad review update");
        } catch (SQLException ex) {
            System.err.println("in connection" + ex);
        }
    }
    
    /**
     * Update an existing factoid in DB
     * @param factoid 
     */
    public static void updateDB(Factoid factoid){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("update factoid set mediaId=?, title=?, content=? where id=?");
            pstmt.setInt(1, factoid.getMediaId());
            pstmt.setString(2, factoid.getTitle());
            pstmt.setString(3, factoid.getContent());
            pstmt.setInt(4, factoid.getId()); 
            pstmt.executeUpdate();
            
            if(factoid.getId()<1)
                System.err.println("Bad factoid update");
        } catch (SQLException ex) {
            System.err.println("in connection" + ex);
        }
    }
    
    /**
     * Delete media from DB
     * @param media 
     */
    public static void deleteDB(Media media){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("delete from media where id=?");
            pstmt.setInt(1, media.getId()); 
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.err.println("(deleteMedia) in connection" + ex);
        }
    }
    
    /**
     * Delete review from DB
     * @param review 
     */
    public static void deleteDB(Review review){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("delete from review where id=?");
            pstmt.setInt(1, review.getId()); 
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.err.println("(deleteReview) in connection" + ex);
        }
    }
    
    /**
     * Delete factoid from DB
     * @param factoid 
     */
    public static void deleteDB(Factoid factoid){
        try {
            PreparedStatement pstmt;

            pstmt = getConnection().prepareStatement("delete from factoid where id=?");
            pstmt.setInt(1, factoid.getId()); 
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.err.println("(deleteFactoid) in connection" + ex);
        }
    }
    
    /**
     * Gets the media from the observable list
     * @param id
     * @return 
     */
    public static Media getMedia(int id){
        Statement stmt;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Media medium = new Media();

        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from media where id="+id);
            rs.next();
            try{
                medium.setId(rs.getInt("id"));
                medium.setTitle(rs.getString("title"));
                medium.setCreator(rs.getString("creator"));
                medium.setSeason(rs.getString("season"));
                medium.setEpisode(rs.getString("episode"));
                medium.setDescription(rs.getString("description"));
                medium.setCategory(rs.getString("category"));
                medium.setType(Type.valueOf(rs.getString("type")));
                try{
                    rs2 = stmt.executeQuery("select * from review where mediaID="+medium.getId());
                    while(rs2.next()){
                        try{
                            Review review = new Review();
                            review.setId(rs2.getInt("id"));
                            review.setMediaId(rs2.getInt("mediaID"));
                            review.setUser(rs2.getString("reviewer"));
                            review.setDate(rs2.getDate("date").toLocalDate());
                            review.setReview(rs2.getString("review"));
                            review.setRating(rs2.getInt("rate"));

                            medium.getReviews().add(review);
                        } catch(SQLException e){System.err.println("Get review failed. (getMedia)");}
                    }
                } catch(SQLException e){System.err.println("Get review list failed. (getMedia)");}
                try{
                    rs2 = stmt.executeQuery("select * from factoid where mediaID="+medium.getId());
                    while(rs2.next()){
                        try{
                            Factoid factoid = new Factoid();
                            factoid.setId(rs2.getInt("id"));
                            factoid.setMediaId(rs2.getInt("mediaID"));
                            factoid.setTitle(rs2.getString("title"));
                            factoid.setContent(rs2.getString("content"));

                            medium.getFactoids().add(factoid);
                        } catch(SQLException e){System.err.println("Get factoid failed. (getMedia)");}
                    }
                } catch(SQLException e){System.err.println("Get factoid list failed. (getMedia)");}
            } catch(SQLException e){System.err.println("Get media failed. (getMedia)");}
        } catch (SQLException e) {System.err.println("Get media list failed. (getMedia)");}
        
        return medium;
    }
    
    /**
     * @param id
     * @return 
     */
    public static Review getReview(int id){
        Statement stmt;
        ResultSet rs = null;
        Review review = new Review();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from review where id="+id);
            rs.next();
            try {
                review.setId(rs.getInt("id"));
                review.setMediaId(rs.getInt("mediaID"));
                review.setUser(rs.getString("reviewer"));
                review.setDate(rs.getDate("date").toLocalDate());
                review.setReview(rs.getString("review"));
                review.setRating(rs.getInt("rate"));

            } catch (SQLException e) {System.err.println("Set review failed. (getReviews)");}
        } catch (SQLException e) {System.err.println("Get review from db failed. (getReviews)");}
        return review;
    }
    
    /**
     * @param id
     * @return 
     */
    public static Factoid getFactoid(int id){
        Statement stmt;
        ResultSet rs = null;
        Factoid factoid = new Factoid();
        try {
            stmt = getConnection().createStatement();       
            rs = stmt.executeQuery("select * from factoid where id="+id);
            rs.next();
            try {
                factoid.setId(rs.getInt("id"));
                factoid.setMediaId(rs.getInt("mediaID"));
                factoid.setTitle(rs.getString("title"));
                factoid.setContent(rs.getString("content"));

            } catch (SQLException e) {System.err.println("Set factoid failed. (getFactoids)");}
        } catch (SQLException e) {System.err.println("Get factoid from db failed. (getFactoids)");}
        return factoid;
    }
}

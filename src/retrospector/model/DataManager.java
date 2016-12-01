/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.model;

import java.net.URISyntaxException;
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
import retrospector.util.UtilityCloset;

/**
 *
 * @author nonfrt
 */
public class DataManager {
    static String connString = "jdbc:hsqldb:file:";
    static Connection conn = null;

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
    
    
    public static Connection getConnection(){
        try{
            if(conn==null)
                conn = DriverManager.getConnection(connString,"SA","");
        } catch(SQLException e){System.err.println("Connection failed.");}
        return conn;
            
    }
    
    public static ObservableList<Media> getMedia(){
        Statement stmt;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ObservableList<Media> list = FXCollections.observableArrayList();

        try{
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("select * from media");
            while (rs.next()) {
                try{
                    Media medium = new Media();
                    medium.setId(rs.getInt("id"));
                    medium.setTitle(rs.getString("title"));
                    medium.setCreator(rs.getString("creator"));
                    medium.setSeasonId(rs.getString("season"));
                    medium.setEpisodeId(rs.getString("episode"));
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
                                review.setRating(rs2.getBigDecimal("rate"));

                                medium.getReviews().add(review);
                            } catch(SQLException e){System.err.println("Get review failed. (getMedia)");}
                        }
                    } catch(SQLException e){System.err.println("Get review list failed. (getMedia)");}
                    list.add(medium);
                } catch(SQLException e){System.err.println("Get media failed. (getMedia)");}
            }
        } catch(SQLException e){System.err.println("Get media list failed. (getMedia)");}
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
                    review.setRating(rs.getBigDecimal("rate"));

                    reviews.add(review);
                } catch (SQLException e) {System.err.println("Get review failed. (getReviews)");}
            }
        } catch (SQLException e) {System.err.println("Get review list failed. (getReviews)");}
        return reviews;
    }
    
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
        
      
        try {
            connString += UtilityCloset.getPath2JarFolder()+"Retrospector";
            Statement stmt;
            stmt = getConnection().createStatement();
            stmt.execute(createMedia);
            stmt.execute(createReview);
        } catch (SQLException ex) {
            System.out.println("Create error in startDB in connection" + ex);
        } catch (URISyntaxException ex) {
            System.out.println("DataManager#startDB \t adding getPath2JarFolder to connString failed:: \n" + ex);
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
            pstmt.setString(3, media.getSeasonId());
            pstmt.setString(4, media.getEpisodeId());
            pstmt.setString(5, media.getDescription());
            pstmt.setString(6, media.getCategory());
            pstmt.setString(7, media.getType().toString());
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
            pstmt.setInt(5, review.getRating().intValueExact()); // If ratings ever get decimal again fix this!
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
            pstmt.setString(3, media.getSeasonId());
            pstmt.setString(4, media.getEpisodeId());
            pstmt.setString(5, media.getDescription());
            pstmt.setString(6, media.getCategory().toString());                  
            pstmt.setString(7, media.getType().toString());                  
            pstmt.setInt(8, media.getId());                  
            pstmt.executeUpdate();
             
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
            pstmt.setInt(5, review.getRating().intValueExact()); // If ratings ever get decimal again fix this!
            pstmt.setInt(6, review.getId()); 
            pstmt.executeUpdate();
            
            if(review.getId()==-1)
                System.err.println("Bad review update");
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
            System.err.println("in connection" + ex);
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
            System.err.println("in connection" + ex);
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
                medium.setSeasonId(rs.getString("season"));
                medium.setEpisodeId(rs.getString("episode"));
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
                            review.setRating(rs2.getBigDecimal("rate"));

                            medium.getReviews().add(review);
                        } catch(SQLException e){System.err.println("Get review failed. (getMedia)");}
                    }
                } catch(SQLException e){System.err.println("Get review list failed. (getMedia)");}
            } catch(SQLException e){System.err.println("Get media failed. (getMedia)");}
        } catch (SQLException e) {System.err.println("Get review list failed. (getReviews)");}
        
        return medium;
    }
    
    /**
     * Gets the review from the observable list
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
                review.setRating(rs.getBigDecimal("rate"));

            } catch (SQLException e) {System.err.println("Get review failed. (getReviews)");}
        } catch (SQLException e) {System.err.println("Get review list failed. (getReviews)");}
        return review;
    }
}

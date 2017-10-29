/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import retrospector.model.DataManager;

/**
 *
 * @author nonfrt
 */
public class GithubUtil {
    public static final String URL = "https://api.github.com/repos";
    public static final String USER = "NonlinearFruit";
    public static final String REPO = "Retrospector";
    public static final Integer TIMEOUT = 1000;
    
    public static String getLatestUrl() {
        return getLatestUrl(USER,REPO);
    }
    
    public static String getLatestUrl(String user, String repo) {
        return URL +"/" + user + "/" + repo + "/releases/latest";
    }
    
    public static String getStarsUrl() {
        return getStarsUrl(USER,REPO);
    }
    
    public static String getStarsUrl(String user, String repo) {
        return URL +"/" + user + "/" + repo + "/stargazers";
    }
    
    public static String getFromUrl(String url) {
        try {
            return SpeedRacer.go(() -> {
                try (Scanner scanner = new Scanner(new URL(url).openStream())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    return responseBody;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
                return "";
            }, TIMEOUT);
        } catch (SlowAsMolassesInJanuaryException ex) {
            System.err.println("api.github.com timed out");
        }
        return "";
    }
    
    public static String getLastestVersion() {
        String results = getFromUrl(getLatestUrl());
        String tags = "\"tag_name\": \"";
        int index = results.indexOf(tags);
        results = results.substring(index);
        index = results.indexOf("\"");
        results = results.substring(0, index);
        return results;
    }
    
    public static boolean isStarGazer(String user) {
        String results = getFromUrl(getStarsUrl());
        return Arrays.asList(results.split("\n")).stream().anyMatch(s->s.contains("login")&&s.contains(DataManager.getGithubUser())&&!DataManager.getGithubUser().isEmpty());
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.achievements;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import retrospector.fxml.chart.StatsTabController;
import retrospector.model.DataManager;
import retrospector.model.Review;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public class AchievementFactory {
    
    /* Unimplemented:
        Binge
        Spree
        Marathon
        Paranoia
        A True Masterpiece
        Inconsistency
    */

    
    public static List<Achievement> getAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        achievements.addAll(Arrays.asList(
                new Achievement("","Star Gazer","Star Retrospector",1,()->{
                    int result = 0;
                    try (Scanner scanner = new Scanner(new URL("https://api.github.com/repos/NonlinearFruit/Retrospector/stargazers").openStream())) {
                        String responseBody = scanner.useDelimiter("\\A").next();
                        result = Arrays.asList(responseBody.split("\n")).stream().anyMatch(s->s.contains("login")&&s.contains(DataManager.getGithubUser())&&!DataManager.getGithubUser().isEmpty())?100:0;
                    } catch(Exception ex) {
                        System.err.println(ex.getMessage());
                    }
                    return result;
                }),
                new Achievement("","Inspector","Edit the .config file",2,()->{
                    PropertyManager.Configuration dusty = new PropertyManager.Configuration();
                    if( dusty.getDefaultUser().equals(DataManager.getDefaultUser()) && 
                            dusty.getCategories().length == DataManager.getCategories().length)
                        return 0;
                    return 100;
                }),
                new Achievement("","Never Again","Give a 1 star review",3,()->DataManager.getReviews().stream()
                        .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                        .anyMatch(r->r.getRating().equals(BigDecimal.ONE))?
                        100:0
                ),
                new Achievement("","Spectrum","Have more categories than colors",1,()->(int)(DataManager.getCategories().length*100.0/(StatsTabController.colors.length+1))),
                new Achievement("","Trivial Pursuit","Have 5 factoids",3,()->DataManager.getFactiodTypes().length*20),
                new Achievement("","Diversify","Have 5 categories",3,()->DataManager.getCategories().length*20),
                new Achievement("","Social","Have 3 users",3,()->DataManager.getUsers().size()*34),
                new Achievement("","Rock","Media with Rock in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Rock"))?
                                100:0
                ),
                new Achievement("","Paper","Media with Paper in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Paper"))?
                                100:0
                ),
                new Achievement("","Scissors","Media with Scissors in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Scissor"))?
                                100:0
                ),
                new Achievement("","Lizard","Media with Lizard in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Lizard"))?
                                100:0
                ),
                new Achievement("","Spock","Media with Star Trek in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Star Trek"))?
                                100:0
                ),
                new Achievement("","Star Wars","Media with Star Wars in the title",3,()->
                        DataManager.getMedia().stream()
                        .anyMatch(m->m.getTitle().contains("Star Wars"))?
                                100:0
                ),
                new Achievement("","Master","Retrospect for 10 years",1,()->(int)Math.floor(
                        ChronoUnit.DAYS.between(
                                DataManager.getReviews().stream()
                                        .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                                        .reduce((a,b)->a.getDate().isBefore(b.getDate())?a:b)
                                        .get().getDate(),
                                LocalDate.now()
                        )*0.027)
                ),
                new Achievement("","Journeyman","Retrospect for 5 years",2,()->(int)Math.floor(
                        ChronoUnit.DAYS.between(
                                DataManager.getReviews().stream()
                                        .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                                        .reduce((a,b)->a.getDate().isBefore(b.getDate())?a:b)
                                        .get().getDate(),
                                LocalDate.now()
                        )*0.055)
                ),
                new Achievement("","Apprentice","Retrospect for 1 year",3,()->(int)Math.floor(
                        ChronoUnit.DAYS.between(
                                DataManager.getReviews().stream()
                                        .filter(r->r.getUser().equals(DataManager.getDefaultUser()))
                                        .reduce((a,b)->a.getDate().isBefore(b.getDate())?a:b)
                                        .get().getDate(),
                                LocalDate.now()
                        )*0.274)
                ),
                new Achievement("","Unemployed","10 review/day for 20 days",1,()->(int)(getLongestConsecutiveDays(10)*5)),
                new Achievement("","Vacation","10 review/day for a week",2,()->(int)(getLongestConsecutiveDays(10)*14.29)),
                new Achievement("","Weekend","10 review/day for 3 days",3,()->(int)(getLongestConsecutiveDays(10)*33.33)),
                new Achievement("","Academic","Category with 10,000 media",1,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .max()
                            .orElse(0)/100
                ),
                new Achievement("","Enthusiast","Category with 1,000 media",2,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .max()
                            .orElse(0)/10
                ),
                new Achievement("","Dabbler","Category with 100 media",3,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .max()
                            .orElse(0)
                ),
                new Achievement("","Iconic","Review 1 media 20 times",1,()->Math.toIntExact(
                        DataManager.getMedia().stream()
                            .mapToLong(m->m.getReviews().stream().filter(r->r.getUser().equals(DataManager.getDefaultUser())).count())
                            .max()
                            .getAsLong()*5
                )),
                new Achievement("","Classic","Review 1 media 10 times",2,()->Math.toIntExact(
                        DataManager.getMedia().stream()
                            .mapToLong(m->m.getReviews().stream().filter(r->r.getUser().equals(DataManager.getDefaultUser())).count())
                            .max()
                            .getAsLong()*10
                )),
                new Achievement("","Favorite","Review 1 media 5 times",3,()->Math.toIntExact(
                        DataManager.getMedia().stream()
                            .mapToLong(m->m.getReviews().stream().filter(r->r.getUser().equals(DataManager.getDefaultUser())).count())
                            .max()
                            .getAsLong()*20
                )),
                new Achievement("","Renaissance Man","1,000 reviews in each category",1,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .min()
                            .orElse(0)/10
                ),
                new Achievement("","Well Rounded","100 reviews in each category",2,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .min()
                            .orElse(0)
                ),
                new Achievement("","Jack of All Trades","10 reviews in each category",3,()->
                        Arrays.asList(DataManager.getCategories()).stream()
                            .mapToInt(c->getReviewsPerCategory(c))
                            .min()
                            .orElse(0)*10
                ),
                new Achievement("","Enigmatologist","Factoid with 10,000 facts",1,()->
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .max()
                            .orElse(0)/100
                ),
                new Achievement("","Riddler","Factoid with 1,000 facts",2,()->
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .max()
                            .orElse(0)/10
                ),
                new Achievement("","Puzzler","Factoid with 100 facts",3,()->
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .max()
                            .orElse(0)
                ),
                new Achievement("","Researcher","1,000 facts for each factoid",1,()->
                        DataManager.getFactiodTypes().length < 5?0:
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .min()
                            .orElse(0)/10
                ),
                new Achievement("","Scientist","100 facts for each factoid",2,()->
                        DataManager.getFactiodTypes().length < 5?0:
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .min()
                            .orElse(0)
                ),
                new Achievement("","Objective","10 facts for each factoid",3,()->
                        DataManager.getFactiodTypes().length < 5?0:
                        Arrays.stream(DataManager.getFactiodTypes())
                            .mapToInt(f->DataManager.getFactoidsByType(f).size())
                            .min()
                            .orElse(0)*10
                ),
                new Achievement("","Scholarly","Give 100 looong reviews",1,()->Math.toIntExact(
                        DataManager.getReviews()
                                .filtered(r->r.getUser().equals(DataManager.getDefaultUser()))
                                .stream()
                                .mapToInt(r->r.getReview().length())
                                .filter(i-> i>=1000)
                                .count()
                )),
                new Achievement("","Thoughtful","Give 10 looong reviews",2,()->
                        DataManager.getReviews()
                                .filtered(r->r.getUser().equals(DataManager.getDefaultUser()))
                                .stream()
                                .mapToInt(r->r.getReview().length())
                                .max()
                                .orElse(0)/10
                ),
                new Achievement("","Wordy","Give a looong review",3,()->
                        DataManager.getReviews()
                                .filtered(r->r.getUser().equals(DataManager.getDefaultUser()))
                                .stream()
                                .mapToInt(r->r.getReview().length())
                                .max()
                                .orElse(0)/10
                ),
                new Achievement("","BFFs","1,000 reviews from a user",1,()->
                        DataManager.getUsers().stream()
                                .filter(u->!u.equals(DataManager.getDefaultUser()))
                                .mapToInt(u->getReviewsPerUser(u))
                                .max()
                                .orElse(0)/10
                ),
                new Achievement("","Chums","100 reviews from a user",2,()->
                        DataManager.getUsers().stream()
                                .filter(u->!u.equals(DataManager.getDefaultUser()))
                                .mapToInt(u->getReviewsPerUser(u))
                                .max()
                                .orElse(0)
                ),
                new Achievement("","Friends","10 reviews from a user",3,()->
                        DataManager.getUsers().stream()
                                .filter(u->!u.equals(DataManager.getDefaultUser()))
                                .mapToInt(u->getReviewsPerUser(u))
                                .max()
                                .orElse(0)*10
                ),
                new Achievement("","Trivia Expert","Collect 10,000 factoids",1,()->DataManager.getFactoids().size()/100),
                new Achievement("","Trivia Pro","Collect 1,000 factoids",2,()->DataManager.getFactoids().size()/10),
                new Achievement("","Trivia Whiz","Collect 100 factoids",3,()->DataManager.getFactoids().size()),
                new Achievement("","Connoisseur","Collect 10,000 media",1,()->DataManager.getMedia().size()/100),
                new Achievement("","Collector","Collect 1,000 media",2,()->DataManager.getMedia().size()/10),
                new Achievement("","Hobbyist","Collect 100 media",3,()->DataManager.getMedia().size()),
                new Achievement("","Fanatic","1 review/day for a year",1,()->(int)(getLongestConsecutiveDays(1)*0.274)),
                new Achievement("","Obsessed","1 review/day for a 90 days",2,()->(int)(getLongestConsecutiveDays(1)*3.33)),
                new Achievement("","Hooked","1 review/day for a month",3,()->(int)(getLongestConsecutiveDays(1)*3.33)),
                new Achievement("","Anthropologist","10,000 reviews from users",1,()->(int)((DataManager.getReviews().size()-getReviewsPerUser(DataManager.getDefaultUser()))/100)),
                new Achievement("","Famous","1,000 reviews from users",2,()->(int)((DataManager.getReviews().size()-getReviewsPerUser(DataManager.getDefaultUser()))/10)),
                new Achievement("","Popular","100 reviews from users",3,()->(int)((DataManager.getReviews().size()-getReviewsPerUser(DataManager.getDefaultUser()))))
        ));
        return achievements;
    }
    
    public static Integer getReviewsPerUser(String user) {
        return Math.toIntExact(DataManager.getReviews().stream().filter(r->r.getUser().equals(user)).count());
    }
    
    /**
     * Only considers the default user
     * @param category
     * @return 
     */
    public static Integer getReviewsPerCategory(String category) {
        return Math.toIntExact(
                DataManager.getMedia().stream()
                        .filter(m->m.getCategory().equals(category))
                        .filter(m->m.getCurrentRating().compareTo(BigDecimal.ZERO)>0)
                        .count()
                
        );
    }
    
    public static HashMap<LocalDate,Integer> getDatesToReviews() {
        HashMap<LocalDate,Integer> map = new HashMap<>();
        for (Review review : DataManager.getReviews()) {
            if(review.getUser().equals(DataManager.getDefaultUser()))
                map.put(review.getDate(), map.getOrDefault(review.getDate(), 0)+1);
        }
        return map;
    }
    
    public static Integer getLongestConsecutiveDays(Integer reviewThreshold) {
        HashMap<LocalDate,Integer> map = getDatesToReviews();
        
        int maxLen = 0;
        for (LocalDate today : map.keySet()) {
            if (map.get(today)<reviewThreshold)
                continue;
            
            LocalDate yesterday = today.minus(1,ChronoUnit.DAYS);
            if (!map.containsKey(yesterday) || map.get(yesterday)<reviewThreshold)
            {
                // Then check for next elements in the
                // sequence
                int currentLen = 1;
                LocalDate tomorrow = today.plus(1,ChronoUnit.DAYS);
                while (map.getOrDefault(tomorrow, 0)>=reviewThreshold) {
                    tomorrow = tomorrow.plus(1,ChronoUnit.DAYS);
                    currentLen++;
                }
 
                // update  optimal length if this length
                // is more
                if (maxLen<currentLen)
                    maxLen = currentLen;
            }
        }
        return maxLen;
    }
}

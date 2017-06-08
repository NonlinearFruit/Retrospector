/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Takes a Racer and through an exception if the Racer take to long to 
 * execute.
 * @author nonfrt
 */
public class SpeedRacer {
    // How long processes get until timeout error
    public static final Integer TIMEOUT_MS = 500;
    
    
    public static <T> T go(Racer<T> racer, Integer timeToBeatInMS) throws SlowAsMolassesInJanuaryException {
        ExecutorService referee = Executors.newSingleThreadExecutor();
        Future<T> race = referee.submit(racer);
        
        T raceResults = null;
        
        try {
            raceResults = race.get(timeToBeatInMS, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            race.cancel(true);
            throw new SlowAsMolassesInJanuaryException(e.getMessage());
        } finally {
            referee.shutdownNow();
        }
        
        return raceResults;
    }
    
    public static <T> T go(Racer<T> racer) throws SlowAsMolassesInJanuaryException {
        return go(racer,TIMEOUT_MS);
    }
}

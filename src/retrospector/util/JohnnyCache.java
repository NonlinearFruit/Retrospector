/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.function.Supplier;

/**
 * This class acts as a wrapper for a race. It stores the value and only 
 * refreshes if the value is at least 'LIFESPAN_MS' milliseconds old.
 * @author nonfrt
 */
public class JohnnyCache<T> {
    public static final long LIFESPAN_MS = 500;
    private T value;
    private Supplier<T> function;
    private long lastAsked;
    
    /**
     * Racer contains the method that will be used
     * to get the value.
     * @param racer 
     */
    public JohnnyCache(Supplier<T> lambda){
        this.function = lambda;
        this.lastAsked = 0;
    }
    
    /**
     * Get the value and save time!
     * @return
     * @throws SlowAsMolassesInJanuaryException 
     */
    public T getValue()
    { 
        long now = System.currentTimeMillis();
        if(now-lastAsked < LIFESPAN_MS) // Has the life since last check, expired
        {
            if(value!=null)
                return value;
        }
        lastAsked = now;
        value = function.get();
        return value;
    }
}

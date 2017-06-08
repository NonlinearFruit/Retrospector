/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.concurrent.TimeoutException;

/**
 *
 * @author nonfrt
 */
public class SlowAsMolassesInJanuaryException extends TimeoutException {
    
    public SlowAsMolassesInJanuaryException(String msg)
    {super(msg);}
}

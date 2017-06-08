
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.concurrent.Callable;

/**
 *
 * @author nonfrt
 */
public interface Racer<T> extends Callable<T> {
    public T call();
}

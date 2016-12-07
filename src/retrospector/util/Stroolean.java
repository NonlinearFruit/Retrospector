/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An object for checklists that is simply a string and a boolean.
 * @author nonfrt
 */
public class Stroolean {

    public Stroolean(String s, boolean b){
        setString(s);
        setBoolean(b);
    }
    
    public Stroolean(String s){
        this(s,false);
    }
    
    public Stroolean(){
        this("");
    }
    
    private final StringProperty string = new SimpleStringProperty();

    public String getString() {
        return string.get();
    }

    public void setString(String value) {
        string.set(value);
    }

    public StringProperty stringProperty() {
        return string;
    }
    private final BooleanProperty bool = new SimpleBooleanProperty();

    public boolean isBoolean() {
        return bool.get();
    }

    public void setBoolean(boolean value) {
        bool.set(value);
    }

    public BooleanProperty booleanProperty() {
        return bool;
    }
    
    @Override
    public String toString(){
        return getString();
    }
    
}

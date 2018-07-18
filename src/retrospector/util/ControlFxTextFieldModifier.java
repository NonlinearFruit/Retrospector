/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;

/**
 * Applies various ControlsFX features to vanilla textfields
 * @author nonfrt
 */
public class ControlFxTextFieldModifier {
    private static final Integer maxAutocompleteResults = 5;
    private static final Integer minLengthForResults = 4;
    
    public static void autocompleteMe(TextField textfield, List<String> strings) {
        final List<String> options = strings.stream()
                .filter( option -> option.length() > minLengthForResults )
                .collect(Collectors.toList());
        
        TextFields.bindAutoCompletion(textfield, (x)->{
            if (x.isCancelled())
                return new ArrayList<>();
            List<String> matches = GetMatches(x.getUserText(), options);
            return matches;
        });
    }
    
    private static List<String> GetMatches(String query, List<String> options){
        if (query.length() < minLengthForResults)
            return new ArrayList<>();

        List<String> results = options.stream()
                .filter((y)->
                        y.toLowerCase().contains(query.toLowerCase()) &&
                        !y.equals(query)
                )
                .collect(Collectors.toList());

        if (results.size() > maxAutocompleteResults)
            return new ArrayList<>();

        return results;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Applies various ControlsFX features to vanilla textfields
 * @author nonfrt
 */
public class ControlFxTextFieldModifier {
    private static final Integer maxAutocompleteResults = 5;
    
    public static void autocompleteMe(TextField textfield, List<String> strings) {
//        TextFields.bindAutoCompletion(
//            textfield,
//            strings
//        );
        TextFields.bindAutoCompletion(textfield, (x)->{
            if (x.isCancelled())
                return new ArrayList<>();
            
            List<String> results = strings.stream()
                    .filter((y)->
                            y.toLowerCase().contains(x.getUserText().toLowerCase()) &&
                            !y.equals(x.getUserText())
                    )
                    .collect(Collectors.toList());
            
            if (results.size() > maxAutocompleteResults)
                return new ArrayList<>();
            
            return results;
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * A generic fx layout with functionality to allow for
 * easy implementation of a search operation.
 * @author nonfrt
 */
public class SearchPane<T> extends VBox{
    
    //-------------------------------------------------------------------------- POJO Fields
    
    /* Takes a T and returns a percent match. [0,1] 
       Results with percent==0 don't show */
    private BiFunction<String,T,Boolean> evaluator;
    /* All possible results */
    private List<T> searchSet;
    /* Results that match the current query */
    private ObservableList<T> results;
    /* Is called when a match is selected */
    private Consumer<T> callback;
    
    //-------------------------------------------------------------------------- JavaFX Fields
    
    private TextField searchBar;
    private TableView<T> resultsTable;
    
    
    private SearchPane(){
        super();
        
        // Pojo Fields
        evaluator = (s,t)->true;
        searchSet = new ArrayList<>();
        results = FXCollections.observableArrayList();
        
        // JavaFX Fields
        searchBar = new TextField();
        searchBar.textProperty().addListener((observableValue, old, neo)->{
                goGoGadgetSearch();
        });
        resultsTable = new TableView(results);
        resultsTable.setRowFactory(tv->{
            TableRow<T> row = new TableRow<>();
            row.selectedProperty().addListener((obv,o,n)->{
                if(n) // If selected
                    callback.accept(row.getItem());
            });
            return row;
        });
        
        // Add FX to self (VBox)
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(searchBar,resultsTable);
    }
    
    private SearchPane(BiFunction<String,T,Boolean> evaluator){
        this();
        this.evaluator = evaluator;
    }
    
    public SearchPane(BiFunction<String,T,Boolean> evaluator, List<T> searchSet, String... columns){
        this(evaluator);
        searchSet.forEach((t)->results.add(t));
        this.searchSet.clear();
        this.searchSet.addAll(searchSet);
        for (String column : columns) {
            TableColumn tc = new TableColumn(column);
            resultsTable.getColumns().add(tc);
            tc.setCellValueFactory(new PropertyValueFactory(column));
        }
    }
    
    public void setCallback(Consumer<T> callback){
        this.callback = callback;
    }
    
    private void goGoGadgetSearch(){
        
        // Update results
        results.clear();
        for (T t : searchSet) {
            if(evaluator.apply(getQuery(), t))
                results.add(t);
        }
        
        // Update resultsTable
        if(results.size()>0){
            showResultsTable(true);
            resultsTable.setItems(results);
            resultsTable.refresh();
        } else {
            showResultsTable(false);
        }
        
    }
    
    private void showResultsTable(Boolean tf){
        resultsTable.setVisible(tf);
    }
    
    private String getQuery(){
        return searchBar.getText();
    }
    
    /**
     * An ordered list of the results based on the current query.
     * @return 
     */
    public ObservableList<T> getResults(){
        return results;
    }
    
    /**
     * Update the search set to something new.
     * @param set 
     */
    public void setSearchSet(List<T> set){
        searchSet = set;
        results.clear();
        searchSet.forEach((t)->results.add(t));
        goGoGadgetSearch();
    }
    
    public void refresh(){
        resultsTable.refresh();
    }
    
    public void addToSearchSet(T t){
        searchSet.add(t);
        refresh();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.media;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import retrospector.model.DataManager;
import retrospector.model.Media;
import retrospector.util.PropertyManager;

/**
 *
 * @author nonfrt
 */
public abstract class JsDataScraper {
    
    private static ScriptEngineManager mgr = new ScriptEngineManager();
    private static ScriptEngine engine = mgr.getEngineByName("JavaScript");

    public abstract Media autocomplete(Media m);
    
    public static String getUrlContent(String url) {
        url = url.replaceAll(" ", "+");
        System.out.println(url);
        try (
            Scanner scanner = new Scanner(new URL(url).openStream())) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return "";
        }
    }
    
    // https://www.journaldev.com/1237/java-convert-string-to-xml-document-and-xml-document-to-string
    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try {  
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    public static String[] getJsFiles() {
        File directory = new File( PropertyManager.pluginPath );
        if (!directory.exists())
            directory.mkdir();
        
        return Arrays.stream( directory.listFiles() )
                .map( file -> file.getName() )
                .toArray( String[] :: new );
    }
    
    public static JsDataScraper getScraper(String file) throws ScriptException {
        JsDataScraper scraper = (JsDataScraper) engine.eval("load('" + PropertyManager.pluginPath + "/" + file + "');");
        return scraper;
    }
    
    public static void print(String msg) {
        System.out.println(msg);
    }
    
    public static String getDefaultCategory() {
        return DataManager.getCategories()[0];
    }
}

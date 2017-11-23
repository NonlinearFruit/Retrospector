/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.scraper;

import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import retrospector.model.Media;

/**
 *
 * @author nonfrt
 */
public abstract class JsDataScraper {
    public abstract List<ScrapeResult> search(String query);
    public abstract List<ScrapeResult> getGroup(ScrapeResult scrape);
    public abstract Media getMedia(ScrapeResult scrape);
    
    public static String getUrlContent(String url) {
        try (Scanner scanner = new Scanner(new URL(url).openStream())) {
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
}

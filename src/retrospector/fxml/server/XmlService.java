/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import retrospector.model.Media;
import retrospector.model.Review;

/**
 *
 * @author nonfrt
 */
public class XmlService {
    
    private static final String dtd = "retroXML.dtd";
    private static final Integer port = 60010;
    private static final String root = "data";
    private static final String media = "media";
        private static final String media_id = "mid";
        private static final String media_title = "title";
        private static final String media_creator = "creator";
        private static final String media_season = "season";
        private static final String media_episode = "episode";
        private static final String media_category = "category";
        private static final String media_description = "description";
    private static final String review = "review";
        private static final String review_id = "rid";
        private static final String review_mediaId = "mediaid";
        private static final String review_rating = "rating";
        private static final String review_user = "user";
        private static final String review_date = "date";
        private static final String review_review = "content";
        
    private static Thread serverThread;
    
    public static LocalDate getLocalDate(String milliseconds) {
        long ms = Long.parseLong(milliseconds);
        return Instant.ofEpochMilli(ms).atZone(ZoneOffset.UTC).toLocalDate();
    }
    
    public static java.util.Date getDate(String milliseconds) {
        long ms = Long.parseLong(milliseconds);
        return new java.util.Date(ms);
    }
    
    public static void sendDocument(final Document dom) {
        sendDocument(dom,x->{});
    }
    
    public static void killServer() {
        if (serverThread != null && serverThread.isAlive())
            serverThread.destroy();
    }
    
    public static void sendDocument(final Document dom, final Consumer<String> updater) {
        serverThread = new Thread() {
            private ServerSocket ss;
            private Socket s;
            
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(port);

                    updater.accept("Waiting for handshake . . .");
                    s = ss.accept();

                    // Start
                    Element e = null;
                    try {
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty(OutputKeys.INDENT, "yes");
                        tr.setOutputProperty(OutputKeys.METHOD, "xml");
                        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
                        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                        updater.accept("Sending Data . . .");
                        // send DOM to file
                        tr.transform(new DOMSource(dom),
                                new StreamResult(s.getOutputStream()));
                        updater.accept("Data Sent!!");

                    } catch (TransformerException te) {
                        updater.accept("Transformer Exception D:\n"+te.getMessage());
                        System.out.println(te.getMessage());
                    }
                    // Stop
                    
                    s.close();
                    ss.close();
                    updater.accept("Server Closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void destroy() {
                try{
                    if (s != null && s.isBound())
                        s.close();
                    if (ss != null && ss.isBound())
                        ss.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            
            
        };
        serverThread.start();
    }

    public static Document mediaListToDocument(List<Media> list) {
        Document dom = null;
        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement(root);

            for (Media m : list)
                rootEle.appendChild(mediaToElement(m,dom));

            dom.appendChild(rootEle);

        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
        
        return dom;
    }
    
    private static Element mediaToElement(Media m, Document dom) {
        Element element = dom.createElement(media);
        Element attribute;
        
        attribute = dom.createElement(media_id);
        attribute.appendChild(dom.createTextNode(m.getId()+""));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_title);
        attribute.appendChild(dom.createTextNode(m.getTitle()));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_creator);
        attribute.appendChild(dom.createTextNode(m.getCreator()));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_season);
        attribute.appendChild(dom.createTextNode(m.getSeason()));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_episode);
        attribute.appendChild(dom.createTextNode(m.getEpisode()));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_category);
        attribute.appendChild(dom.createTextNode(m.getCategory()));
        element.appendChild(attribute);
        
        attribute = dom.createElement(media_description);
        attribute.appendChild(dom.createTextNode(m.getCategory()));
        element.appendChild(attribute);
        
        for (Review r : m.getReviews()) 
            element.appendChild(reviewToElement(r,dom));
        
        return element;
    }
    
    private static Node reviewToElement(Review r, Document dom) {
        Element element = dom.createElement(review);
        Element attribute;

        attribute = dom.createElement(review_id);
        attribute.appendChild(dom.createTextNode(r.getId() + ""));
        element.appendChild(attribute);

        attribute = dom.createElement(review_mediaId);
        attribute.appendChild(dom.createTextNode(r.getMediaId()+""));
        element.appendChild(attribute);
        
        attribute = dom.createElement(review_rating);
        attribute.appendChild(dom.createTextNode(r.getRating()+""));
        element.appendChild(attribute);

        attribute = dom.createElement(review_user);
        attribute.appendChild(dom.createTextNode(r.getUser()));
        element.appendChild(attribute);

        attribute = dom.createElement(review_date);
        attribute.appendChild(dom.createTextNode(r.getDate().atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()+""));
        element.appendChild(attribute);

        attribute = dom.createElement(review_review);
        attribute.appendChild(dom.createTextNode(r.getReview()));
        element.appendChild(attribute);

        return element;
    }
    
    private static List<Media> parseDocument(Document dom) {
        Element elementRoot = dom.getDocumentElement();
        List<Media> list = new ArrayList<>();
        NodeList mediaNodes = elementRoot.getElementsByTagName(media);
        for (int i = 0; i<mediaNodes.getLength(); i++) {
            Node mediaNode = mediaNodes.item(i);
            Media newMedia = elementToMedia(mediaNode);
            list.add(newMedia);
        }
        return list;
    }
    
    private static Media elementToMedia(Node node) {
        NodeList attributes = node.getChildNodes();
        Media m = new Media();
        for (int i = 0; i<attributes.getLength(); i++) {
            Node attr = attributes.item(i);
            String attrName = attr.getNodeName();
            switch(attrName) {
                case media_id:
                    m.setId(Integer.parseInt(attr.getTextContent()));
                    break;
                case media_title:
                    m.setTitle(attr.getTextContent());
                    break;
                case media_creator:
                    m.setCreator(attr.getTextContent());
                    break;
                case media_season:
                    m.setSeason(attr.getTextContent());
                    break;
                case media_episode:
                    m.setEpisode(attr.getTextContent());
                    break;
                case media_category:
                    m.setCategory(attr.getTextContent());
                    break;
                case media_description:
                    m.setDescription(attr.getTextContent());
                    break;
                case review:
                    Review r = elementToReview(attr);
                    m.getReviews().add(r);
            }
        }
        return m;
    }
    
    private static Review elementToReview(Node node) {
        NodeList attributes = node.getChildNodes();
        Review r = new Review();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attr = attributes.item(i);
            String attrName = attr.getNodeName();
            switch (attrName) {
                case review_id:
                    r.setId(Integer.parseInt(attr.getTextContent()));
                    break;
                case review_mediaId:
                    r.setMediaId(Integer.parseInt(attr.getTextContent()));
                    break;
                case review_rating:
                    r.setRating(Integer.parseInt(attr.getTextContent()));
                    break;
                case review_user:
                    r.setUser(attr.getTextContent());
                    break;
                case review_date:
                    r.setDate(getLocalDate(attr.getTextContent()));
                    break;
                case review_review:
                    r.setReview(attr.getTextContent());
                    break;
            }
        }
        return r;
    }
    
    public static void recieveDocument(final String host) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(host, port);
                    
                    // Start
                    Document dom;
                    // Make an  instance of the DocumentBuilderFactory
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    try {
                        // use the factory to take an instance of the document builder
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        // parse using the builder to get the DOM mapping of the    
                        // XML stream
                        dom = db.parse(s.getInputStream());
                        for (Media m : parseDocument(dom)) {
                            System.out.println(m);
                            System.out.println(m.getCategory());
                            for (Review r : m.getReviews()) {
                                System.out.println(r);
                                System.out.println(r.getReview());
                            }
                        }

                    } catch (ParserConfigurationException pce) {
                        System.out.println(pce.getMessage());
                    } catch (SAXException se) {
                        System.out.println(se.getMessage());
                    } catch (IOException ioe) {
                        System.err.println(ioe.getMessage());
                    }
                    // Stop
                    
                    s.close();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.fxml.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import retrospector.model.DataManager;
import retrospector.model.Media;

/**
 * FXML Controller class
 *
 * @author nonfrt
 */
public class ServerTabController implements Initializable {

    private String moreInfoLink = "https://github.com/NonlinearFruit/Retrospector-Android";
    
    @FXML
    private Text ipAddr;
    @FXML
    private Button launchBtn;
    @FXML
    private TextArea statusArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ipAddr.setText(getIP());
        
        launchBtn.setOnAction(e->{
            overrideServerStatus("Running Server . . .\n");
            launch();
        });
    }    
    
    public String getIP() {
        String ip = "???.???.???.???";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            appendServerStatus("Failed to discern IP Address :(");
            throw new RuntimeException(e);
        }
        return ip;
    }
    
    public void appendServerStatus(String s) {
        statusArea.appendText("\n"+s+"\n");
    }
    
    public void overrideServerStatus(String s) {
        statusArea.setText(s);
    }
    
    public void launch() {
        appendServerStatus("\tGetting Media Info . . .");
        List<Media> list = DataManager.getMedia();
        appendServerStatus("\tConverting to XML . . .");
        Document dom = XmlService.mediaListToDocument(list);
        XmlService.sendDocument(dom,update->appendServerStatus("\t"+update));
    }
    
    
    
}

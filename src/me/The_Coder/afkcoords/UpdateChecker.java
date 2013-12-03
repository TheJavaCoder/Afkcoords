package me.The_Coder.afkcoords;

import com.sun.jndi.url.rmi.rmiURLContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bailey on 12/3/13.
 */
public class UpdateChecker {

    private  Main plugin;
    private URL filesFeed;
    private String version;
    private String link;

    public UpdateChecker(Main main, String url) {
        plugin = main;
        try {
            filesFeed = new URL(url);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateNeeded() {
        try {
            InputStream input = this.filesFeed.openConnection().getInputStream();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

            Node lastestfile = doc.getElementsByTagName("item").item(0);
            NodeList childern = lastestfile.getChildNodes();

            version = childern.item(1).getTextContent().replaceAll("[a-zA-Z]", "");
            link = childern.item(3).getTextContent();
            if(!plugin.pdfFile.getVersion().equals(version));
                return true;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getVerson() {
        return version;
    }

    public String getLink() {
        return link;
    }

}

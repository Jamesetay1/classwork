package pa1;

import org.jsoup.nodes.Document;

/**
 * Coupling object to hold
 * a string (url) and
 * J Soup document taken from URL
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class StringDocument {

    private String url;
    private Document doc;


    public StringDocument(String url, Document doc) {
        this.url = url;
        this.doc = doc;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public Document getDoc() {
        return doc;
    }


    public void setDoc(Document doc) {
        this.doc = doc;
    }


}

import java.io.*;
import java.util.*;

public class Link implements java.io.Serializable {

    private String URLAbsRoot;
    private String URLIn;
    private String title;
    private HashMap<String, Float> body;
    private LinkedList<String> linksTo;
    private LinkedList<String> linksFrom;
    private TreeMap<String, Float> tfidf;
    private float pageRank;

    //Link zero constructor
    public Link(String URL) {
        URLIn = URL;
        URLAbsRoot = "";
        title = "";
        body = new HashMap<>();
        linksTo = new LinkedList<>();
        linksFrom = new LinkedList<>();
        tfidf = new TreeMap<>();
        pageRank = 0;
    }

    //Link total constructor
    public Link(String URL, String t, HashMap<String, Float> b, LinkedList<String> l) {
        URLIn = URL;
        URLAbsRoot = getURLAbsRoot();
        title = t;
        body = b;
        linksTo = l;
        linksFrom = new LinkedList<>();
        tfidf = new TreeMap<>();
        pageRank = 0;
    }

    //get methods
    public String getURLIn() {return URLIn; }
    public String getTitle() { return title; }
    public HashMap<String, Float> getBody() { return body; }
    public LinkedList<String> getLinksTo() { return linksTo; }
    public LinkedList<String> getLinksFrom() {return linksFrom; }
    public String getURLAbsRoot() { return URLAbsRoot; }
    public TreeMap<String, Float> getTfidf() { return tfidf; }
    public float getPageRank() { return pageRank; }
    //set methods
    public void setPageRank(float rank) { pageRank = rank; }

    //writeFile method
    public void writeFile() {
        try {
            ObjectOutputStream linkStream = new ObjectOutputStream(new FileOutputStream("." + File.separator + "crawlData" + File.separator + title));
            linkStream.writeObject(this);
            linkStream.close();
        }
        catch (FileNotFoundException e) { System.out.println("Error: Cannot open file for writing"); }
        catch (IOException e) { System.out.println("Error: Cannot write to file"); }
    }

    //toString method
    public String toString() {
        return URLIn;
    }
}

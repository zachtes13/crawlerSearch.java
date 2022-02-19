import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class Search {

    private File crawlDir;
    private ArrayList<File> crawlFiles;

    //Search constructor
    public Search() {
        crawlDir = new File("crawlData");
        crawlFiles = new ArrayList<>();
    }

    //checkValidFile method, checks if the input filename is valid by comparing with list of filenames
    public boolean checkValidFile(String url) {

        getCrawlFileNames();
        boolean flag = false;

        for (File file : crawlFiles) {
            if (file.toString().compareTo("crawlData" + File.separator + parseLinkTitle(url)) == 0) {
                flag = true;
            }
        }
        return flag;
    }

    //getCrawlFileNames method, builds an array of the saved filenames
    public void getCrawlFileNames() {

        crawlFiles.clear();
        File[] fileArray = crawlDir.listFiles();

        for (int i = 0; i < fileArray.length; i++) {
            crawlFiles.add(fileArray[i]);
        }
    }

    //parseTitle method, parses the title of the link
    public String parseLinkTitle(String linkIn) {
        String pageTitle = new String();

        if (linkIn.contains("https:")) {
            pageTitle = linkIn.substring((linkIn.lastIndexOf("/") + 1), linkIn.lastIndexOf("."));
        }
        return pageTitle;
    }

    //getLinkFile method, finds and returns relevant Link file
    public File getLinkFile(String linkIn) {

        String linkInTitle = parseLinkTitle(linkIn);
        File thisLink = new File("");
        getCrawlFileNames();

        for (File file : crawlFiles) {
            if (file.toString().compareTo("crawlData" + File.separator + linkInTitle) == 0) {
                thisLink = file;
            }
        }
        return thisLink;
    }

    //getIdfFile method, finds and returns IDF file
    public File getIdfFile() {
        File thisIdf = new File("");
        getCrawlFileNames();

        for (File file : crawlFiles) {
            if (file.toString().compareTo("crawlData" + File.separator + "idf") == 0) {
                thisIdf = file;
            }
        }
        return thisIdf;
    }

    //readFileToLink method, converts Link file to Link object instance
    public Link readFileToLink(File inFile) {

        Link linkOut = new Link("");
        getCrawlFileNames();

        try {
            ObjectInputStream linkStream = new ObjectInputStream(new FileInputStream(inFile.toString()));
            linkOut = (Link)linkStream.readObject();
            linkStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class doesn't match");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return linkOut;
    }

    //readFileToIdf method, converts IDF file to hash map instance
    public HashMap<String, Float> readFileToIdf(File inFile) {

        HashMap<String, Float> idfOut = new HashMap<>();
        getCrawlFileNames();

        try {
            ObjectInputStream linkStream = new ObjectInputStream(new FileInputStream(inFile.toString()));
            idfOut = (HashMap<String, Float>) linkStream.readObject();
            linkStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class doesn't match");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }
        return idfOut;
    }

    //getAllLinks method, builds and returns a list of all saved links
    public ArrayList<Link> getAllLinks() {

        getCrawlFileNames();

        ArrayList<Link> linksList = new ArrayList<>();
        for (int i = 0; i < crawlFiles.size(); i++) {
            if (crawlFiles.get(i).toString().compareTo("crawlData" + File.separator + "idf") != 0) {
                Link linkToAdd = readFileToLink(crawlFiles.get(i));
                linksList.add(linkToAdd);
            }
        }

        return linksList;
    }

    //getOutgoingLinks method, gets and returns  outgoing link information for given Link
    public LinkedList<String> getOutgoingLinks(String url) {

        getCrawlFileNames();
        Link returnLink;

        if (checkValidFile(url)) { returnLink = readFileToLink(getLinkFile(url)); }
        else { return null; }

        return returnLink.getLinksTo();
    }

    //getIncomingLinks method, gets and returns  incoming link information for given Link
    public LinkedList<String> getIncomingLinks(String url) {

        getCrawlFileNames();
        Link returnLink;

        if (checkValidFile(url)) { returnLink = readFileToLink(getLinkFile(url)); }
        else { return null; }

        return returnLink.getLinksFrom();
    }

    //getPageRank method, gets and returns  page rank information for given Link
    public double getPageRank(String url) {

        getCrawlFileNames();
        Link returnLink;

        if (checkValidFile(url)) { returnLink = readFileToLink(getLinkFile(url)); }
        else { return -1.0; }

        return returnLink.getPageRank();
    }

    //getTF method, gets and returns term frequency data for given word in a given Link
    public double getTF(String url, String word) {

        getCrawlFileNames();

        if (checkValidFile(url)) {
            Link queryLink = readFileToLink(getLinkFile(url));
            HashMap<String, Float> wordTf = queryLink.getBody();
            if (wordTf.containsKey(word)) { return wordTf.get(word); }
        }

        return 0;
    }

    //getTFIDF method, gets and returns  TFIDF info for a given word in a given Link
    public double getTFIDF(String url, String word) {

        getCrawlFileNames();

        if (checkValidFile(url)) {
            Link queryLink = readFileToLink(getLinkFile(url));
            TreeMap<String, Float> wordTfidf = queryLink.getTfidf();
            if (wordTfidf.containsKey(word)) { return wordTfidf.get(word); }
        }

        return 0;
    }

    //getIDFMap method, retrieves and returns  IDF mapping
    public HashMap<String, Float> getIDFMap() {

        getCrawlFileNames();

        HashMap<String, Float> queryIdf = readFileToIdf(getIdfFile());

        return queryIdf;
    }

    //getIDF method, retrieves and returns IDF data
    public double getIDF(String word) {

        getCrawlFileNames();

        HashMap<String, Float> queryIdf = readFileToIdf(getIdfFile());
        if (queryIdf.containsKey(word)) { return queryIdf.get(word); }

        return 0;
    }
}

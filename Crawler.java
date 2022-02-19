import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class Crawler implements java.io.Serializable {

    private Queue<String> linksQueue;
    private HashSet<String> linksVisitedString;
    private HashSet<Link> linksVisitedLink;
    private HashMap<String, Integer> pagesWordIn;
    private HashMap<String, Float> idf;
    private URLMapping matMap;

    //Crawler constructor
    public Crawler() {
        linksQueue = new LinkedList<>();
        linksVisitedString = new HashSet<>();
        linksVisitedLink = new HashSet<>();
        pagesWordIn = new HashMap<>();
        idf = new HashMap<>();
        matMap = new URLMapping(new HashSet<>());
    }

    //readLink method, calls WebRequester
    public String readLink(String linkIn) {
        String pageInfo = "";
        try {
            pageInfo = WebRequester.readURL(linkIn);
            return pageInfo;
        } catch (MalformedURLException e) {
            System.out.println("Invalid or Broken URL: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading URL: ");
            e.printStackTrace();
            System.out.println("Trying to read again...");
            linksQueue.add(linkIn);
        }
        return pageInfo;
    }

    //parseURLAbsRoot method, parses the absolute URL root
    public String parseURLAbsRoot(String linkIn) { return linkIn.substring(0, (linkIn.lastIndexOf("/") + 1)); }

    //parseTitle method, parses and returns title
    public String parseTitle(String linkIn) {
        String pageInfo = readLink(linkIn);
        String pageTitle = "";

        if (pageInfo.contains("<title>")) {
            String[] split1 = pageInfo.split("<title>", 2);
            String[] split2 = split1[1].split("</title>", 2);
            pageTitle = (split2[0]);
        }
        return pageTitle;
    }

    //parseBody method, parses and returns body text as a hash map with term frequencies
    public HashMap<String, Float> parseBody(String linkIn) {
        String pageInfo = readLink(linkIn);
        HashMap<String, Integer> pageBodyHash = new HashMap<>();
        String[] bodyArr = new String[]{};

        if (pageInfo.contains("<p>")) {
            String[] split1 = pageInfo.split("<p>", 2);
            String[] split2 = split1[1].split("</p>", 2);
            bodyArr = split2[0].strip().split("\n");

            for (String word : bodyArr) {
                if (pageBodyHash.containsKey(word.toLowerCase())) {
                    Integer freq = pageBodyHash.get(word.toLowerCase());
                    pageBodyHash.put(word.toLowerCase(), freq + 1);
                } else {
                    pageBodyHash.put(word.toLowerCase(), 1);
                }
            }
        }
        HashMap<String, Float> wordsList = new HashMap<>();
        for (HashMap.Entry<String, Integer> keyVal : pageBodyHash.entrySet()) {
            wordsList.put(keyVal.getKey(), ((float) keyVal.getValue() / (float)bodyArr.length));
        }
        return wordsList;
    }

    //parseOutgoingLinks method, parses and returns list of outgoing links
    public LinkedList<String> parseOutgoingLinks(String linkIn) {
        String pageInfo = readLink(linkIn);
        LinkedList<String> pageLinksList = new LinkedList<>();

        if (pageInfo.contains("<a")) {
            String[] split1 = pageInfo.split("</p>", 2);
            String[] split2 = split1[1].split("</body>", 2);
            String[] linksArr = split2[0].strip().split("\n");

            for (String link : linksArr) {
                if (link.contains("href=\"./")) {
                    String[] split3 = link.split("href=\"./", 2);
                    String[] split4 = split3[1].split("\">", 2);
                    pageLinksList.add(parseURLAbsRoot(linkIn) + split4[0]);
                } else if (link.contains("href=\"http://")) {
                    String[] split3 = link.split("href=\"./", 2);
                    String[] split4 = split3[1].split("\">", 2);
                    pageLinksList.add(parseURLAbsRoot(linkIn) + split4[0]);
                }
            }
        }
        return pageLinksList;
    }

    //addPagesWordIn method, builds a hash map of the total pages a given word is found in
    public void addPagesWordIn(Link link) {
        for (HashMap.Entry<String, Float> keyVal : link.getBody().entrySet()) {
            if (pagesWordIn.containsKey(keyVal.getKey())) {
                Integer freq = pagesWordIn.get(keyVal.getKey());
                pagesWordIn.put(keyVal.getKey(), freq + 1);
            } else {
                pagesWordIn.put(keyVal.getKey(), 1);
            }
        }
    }

    //findAndSetIncomingLinks method, finds incoming links for a given link and sets that link's incoming links attribute
    public void findAndSetIncomingLinks(Link link) {
        for (Link linkTo : linksVisitedLink) {
            if (link.getLinksTo().contains(linkTo.getURLIn())) {
                link.getLinksFrom().add(linkTo.getURLIn());
            }
        }
    }

    //findIdf method, finds the idf value and saves as Crawler attribute
    public void findIdf() {

        for (HashMap.Entry<String, Integer> keyVal : pagesWordIn.entrySet()) {
            Maths calculator = new Maths();
            idf.put((keyVal.getKey()), calculator.log2((float) (linksVisitedString.size()) / (1 + keyVal.getValue())));
        }
    }

    //findAndSetTfidf method, finds TFIDF values for a link and sets that link's TFIDF attribute accordingly
    public void findAndSetTfidf(Link link) {

        Maths calculator = new Maths();

        for (HashMap.Entry<String, Float> keyVal : idf.entrySet()) {
            if (!link.getBody().containsKey(keyVal.getKey())) {
                link.getTfidf().put(keyVal.getKey(), (float) 0);
            } else {
                link.getTfidf().put(keyVal.getKey(), (calculator.log2((float) 1 + link.getBody().get(keyVal.getKey())) * idf.get(keyVal.getKey())));
            }
        }
    }

    //getPageRank method, gets page rank by calling PageRankCalculator and sets that link's page rank attribute accordingly
    public void getPageRank() {
        URLMapping matMap = new URLMapping(linksVisitedLink);

        PageRankCalculator findPageRank = new PageRankCalculator(matMap, linksVisitedLink.size());
        float[] pageRankVect = findPageRank.pageRank();
        HashMap<Link, Integer> pageRankMap = findPageRank.getMap().getURLtoID();

        for (Map.Entry<Link, Integer> keyVal : pageRankMap.entrySet()) {
            keyVal.getKey().setPageRank(pageRankVect[keyVal.getValue()]);
        }
    }

    //writeIdfToFile, writes the crawl's IDF data to a single file
    public void writeIdfToFile() {
        try {
            ObjectOutputStream idfStream = new ObjectOutputStream(new FileOutputStream("." + File.separator + "crawlData" + File.separator + "idf"));
            idfStream.writeObject(idf);
            idfStream.close();
        }
        catch (FileNotFoundException e) { System.out.println("Error: Cannot open file for writing"); }
        catch (IOException e) { System.out.println("Error: Cannot write to file"); }
    }

    //clear method, clears previous crawl data by clearing lists and deleting files
    public void clear() {

        linksQueue.clear();
        linksVisitedString.clear();
        pagesWordIn.clear();


        File crawlDir = new File("crawlData");
        File[] fileArray = crawlDir.listFiles();

        if (fileArray != null) {
            for (int i = 0; i < fileArray.length; i++) {
                fileArray[i].delete();
            }
            if (crawlDir.listFiles().length == 0) {
                crawlDir.delete();
            }
        }
    }

    //crawl method, calling the crawl method enacts the entire crawl by calling various methods and collecting the relevant data
    public void crawl(String seed) {

        Link currLink;

        //initialize queue with seed link
        Link seedLink = new Link(seed, parseTitle(seed), parseBody(seed), parseOutgoingLinks(seed));
        currLink = seedLink;
        linksQueue.add(currLink.getURLIn());
        linksVisitedString.add(currLink.getURLIn());
        linksVisitedLink.add(currLink);

        while (!linksQueue.isEmpty()) {

            String tempLink = linksQueue.remove();

            //creates new link object
            if (tempLink.compareTo(currLink.getURLIn()) != 0) {
                currLink = new Link(parseURLAbsRoot(tempLink) + parseTitle(tempLink) + ".html", parseTitle(tempLink), parseBody(tempLink), parseOutgoingLinks(tempLink));
            }

            addPagesWordIn(currLink);

            //populate queue and pages visited
            linksVisitedString.add(currLink.getURLIn());
            linksVisitedLink.add(currLink);
            for (String link : currLink.getLinksTo()) {
                if (!linksVisitedString.contains(link) && !linksQueue.contains(link)) { linksQueue.add(link); }
            }
        }

        findIdf();

        //save crawl data to file
        File crawl = new File("crawlData");
        if (!crawl.exists()) { crawl.mkdirs(); }
        writeIdfToFile();

        for (Link link : linksVisitedLink) {

            findAndSetTfidf(link);
            findAndSetIncomingLinks(link);
            getPageRank();

            link.writeFile();
        }
    }
}
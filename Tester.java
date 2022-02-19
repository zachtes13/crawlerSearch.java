import java.util.List;

public class Tester implements ProjectTester {

    Crawler webCrawler;
    Search searchEngine;

    @Override
    public void initialize() {

        webCrawler = new Crawler();
        searchEngine = new Search();

        //comment out when running TesterController
        webCrawler.clear();
    }

    @Override
    public void crawl(String seedURL) {

        webCrawler.crawl(seedURL);
    }

    @Override
    public List<String> getOutgoingLinks(String url) {
        return searchEngine.getOutgoingLinks(url);
    }

    @Override
    public List<String> getIncomingLinks(String url) {
        return searchEngine.getIncomingLinks(url);
    }

    @Override
    public double getPageRank(String url) {
        return searchEngine.getPageRank(url);
    }

    @Override
    public double getIDF(String word) {
        return searchEngine.getIDF(word);
    }

    @Override
    public double getTF(String url, String word) {
        return searchEngine.getTF(url, word);
    }

    @Override
    public double getTFIDF(String url, String word) {
        return searchEngine.getTFIDF(url, word);
    }

    @Override
    public List<SearchResult> search(String query, boolean boost, int X) {

        UserInput newQuery = new UserInput(query);
        newQuery.queryTfidf(searchEngine.getIDFMap());


        return newQuery.scoreList(searchEngine.getAllLinks(), boost, X);
    }
}

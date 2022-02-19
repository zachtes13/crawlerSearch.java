import java.util.*;

public class UserInput {

    String query;
    HashMap<String, Float> queryMap;
    float numTermsInQuery;
    TreeMap<String, Float> queryTfidf;

    //UserInput constructor
    public UserInput(String q) {
        query = q;
        queryMap = queryToMap();
        numTermsInQuery = 0;
        queryTfidf = new TreeMap<>();
    }

    //queryToMap method, converts users string query to a hash map, with corresponding term frequency values
    public HashMap<String, Float> queryToMap() {

        HashMap<String, Float> queryMap = new HashMap<>();

        String[] tempQueryList = query.split(" ");
        numTermsInQuery = tempQueryList.length;
        for (int i = 0; i < tempQueryList.length; i++) {
            if (!queryMap.containsKey(tempQueryList[i].toLowerCase())) {
                queryMap.put(tempQueryList[i].toLowerCase(), (float)1);
            } else {
                float freq = queryMap.get(tempQueryList[i].toLowerCase());
                queryMap.put(tempQueryList[i].toLowerCase(), freq + 1);
            }
        }
        return queryMap;
    }

    //queryTfidf method, finds the TFIDF values of the words in the query
    public void queryTfidf(HashMap<String, Float> idfMapIn) {

        queryToMap();
        TreeMap<String, Float> queryTfidfMap = new TreeMap<>();
        Maths calculator = new Maths();

        for (HashMap.Entry<String, Float> keyVal : queryMap.entrySet()) {
            if ((idfMapIn.containsKey(keyVal.getKey())) && (idfMapIn.get(keyVal.getKey()) > 0) && (!queryTfidfMap.containsKey(keyVal.getKey()))) {
                queryTfidfMap.put(keyVal.getKey(), (calculator.log2(1 + (queryMap.get(keyVal.getKey()) / numTermsInQuery)) * idfMapIn.get(keyVal.getKey())));
            }
        }
        queryTfidf.clear();
        queryTfidf = queryTfidfMap;
    }

    //tfidfVect method, creates a TFIDF Vector to be processed by ScoreList
    public float[] tfidfVect(TreeMap<String, Float> tfidfIn) {
        float[] vect = new float[tfidfIn.size()];
        int index = -1;

        for (Map.Entry<String, Float> keyVal : tfidfIn.entrySet()) {
            index++;
            vect[index] = keyVal.getValue();
        }
        return vect;
    }

    //scoreList method, finds and returns relevant search info given query and boost values
    public ArrayList<SearchResult> scoreList(ArrayList<Link> allLinks, boolean boost, int X) {

        ArrayList<SearchResult> cossimLinks = new ArrayList<>();
        Maths calculator = new Maths();


        for (Link link : allLinks) {
            TreeMap<String, Float> stripLinkTfidf = new TreeMap<>();
            for (Map.Entry<String, Float> keyVal : queryTfidf.entrySet()) {
                if (link.getTfidf().containsKey(keyVal.getKey())) {
                    stripLinkTfidf.put(keyVal.getKey(), link.getTfidf().get(keyVal.getKey()));
                }
            }
                float[] linkVect = tfidfVect(stripLinkTfidf);
                float[] queryVect = tfidfVect(queryTfidf);

                float cossim = calculator.cossineSim(queryVect, linkVect);
                SearchScore searchResult;
                if (!boost) { searchResult = new SearchScore(cossim, link.getTitle()); }
                else { searchResult = new SearchScore(cossim * link.getPageRank(), link.getTitle()); }

                cossimLinks.add((SearchResult)searchResult);
        }

        Collections.sort(cossimLinks, new comparatorHelper());
        return cossimLinks;
    }
}
